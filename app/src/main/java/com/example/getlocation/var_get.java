package com.example.getlocation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class var_get extends AppCompatActivity{
    String girlUniqueIdplusDate,girlUniqueId, boyUniqueId;
    String peopleHelpingNow;
    Double v1,v2;
    int num=0;
    int temp=0;
    String category;
    private TextView number_save,uniqueid_save,age_save,name_save,refresh_save,safeNo_save,distanceAndDuration;
    private Button back_btn;
    private ImageView image_save;

    //Latitude and longitude.................
    double girlLat1=0, girlLon1=0, boyLat2=0, boyLon2=0;
    double lat1,long1;
    int flag=0;
    String sType;


    //Firebase wale....
    FirebaseDatabase database;
    DatabaseReference rootReference,dangerReference, userReference, girlReference;
    private FirebaseStorage storage;
    private StorageReference storageReferenceFetchImage;

    //------------------------------------Latitude, Longitude Vars--------------------------------------
    public String latitude1, longitude1;
    public String latitude2, longitude2;
    public String locationLink;

    public void reciveLatLong(Intent intent){

        intent.getAction().equals("act_location");
        lat1 = intent.getDoubleExtra("Latitude", 0);
        long1 = intent.getDoubleExtra("Longitude", 0);
        latitude1 = Double.toString(lat1);
        longitude1 = Double.toString(long1);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_var_get);


        //


//....................................................value dalo..........................................
        distanceAndDuration=findViewById(R.id.distanceAndDuration);
        number_save = findViewById(R.id.number_save);
        uniqueid_save = findViewById(R.id.uniqueid_save);
        name_save=findViewById(R.id.name_save);
        age_save=findViewById(R.id.age_save);
        image_save=findViewById(R.id.image_save);
        refresh_save=findViewById(R.id.refresh_save);
        safeNo_save=findViewById(R.id.SafeNumber_save);
        back_btn =findViewById(R.id.var_btn);
        boyUniqueId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Button iwillHelp=findViewById(R.id.iWillHelp);
        Button Link= findViewById(R.id.go_to_link);


        if (getIntent().hasExtra("category")) {
            String smslink = getIntent().getStringExtra("link");
            category = getIntent().getStringExtra("category");
            girlUniqueIdplusDate= getIntent().getStringExtra("UniqueId");
            girlUniqueId = girlUniqueIdplusDate.substring(0, girlUniqueIdplusDate.length() - 20);
            locationLink = smslink;
            Link.setText(smslink);
            }



        //............
        database=FirebaseDatabase.getInstance();
        rootReference=database.getReference();
        dangerReference=rootReference.child("inDanger").child(girlUniqueIdplusDate).child(boyUniqueId);
        userReference=rootReference.child("Users");
        girlReference=rootReference.child("inDanger").child(girlUniqueIdplusDate);

    //.........................................Add Functions from below................................................................


     //............................location nikalne ke function se variable v1 aur v2 ki value is comment lines ke bivh daal...............................






     //............................location nikalne ke function se variable v1 aur v2 ki value is comment lines ke bivh daal...............................






        girlLat1=lat1;
        girlLon1=long1;
        boyLat2=30.2909096;//my coordinate
        boyLon2=78.0017146;



        Toast.makeText(var_get.this,"GIRL : Lattitude ="+girlLat1+" Longitude= "+girlLon1+"\n BOY  : Lattitude ="+boyLat2+" Longitude= "+boyLon2,Toast.LENGTH_LONG).show();
        getDistance(girlLat1,girlLon1,30.2789104,77.9644066);











        iwillHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                willYouHelp();
                Toast.makeText(var_get.this,"The distance shown here is Approximate distance",Toast.LENGTH_LONG).show();
            }
        });
       refresh_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               isSheSafe();
           }
       });
       //if (!(girlUniqueId.equals(boyUniqueId)))
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            exit();
            }
        });

        refresh();


    }

    //----------------------------------------------------------webview


    //-----------------------------------------------------------end vebview



    public void willYouHelp(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(var_get.this);
        builder.setMessage("Will You HELP ?");

        // Set Alert Title
        builder.setTitle("Someone is in Danger!!");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                temp=1;
                                userReference.child(boyUniqueId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String name=snapshot.child("name").getValue().toString();
                                    dangerReference.child("Name").setValue(name);
                                    dangerReference.child("latt2").setValue(boyLat2);
                                    dangerReference.child("long2").setValue(boyLon2);
                                    addDistanceToTextView();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                                // When the user click yes button
                                // then app will close
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
    public void isSheSafe(){
        AlertDialog.Builder builder // Create the object of AlertDialog Builder class
                = new AlertDialog
                .Builder(var_get.this);
        builder.setMessage("Do you confirm that she is safe now?");  // Set the message show for the Alert time
        // Set Alert Title
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Confirm",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            { markSafe();
                                markSafe();
                            }
                        });
        builder
                .setNegativeButton(
                        "Don't Know",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create(); // Create the Alert dialog
        alertDialog.show();  // Show the Alert Dialog box
    }
    public void exit(){
        AlertDialog.Builder builder // Create the object of AlertDialog Builder class
                = new AlertDialog
                .Builder(var_get.this);
        builder.setMessage("Do you confirm that she is safe now?");  // Set the message show for the Alert time
        // Set Alert Title
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            { markSafe();
                                Intent ii=new Intent(var_get.this,MainActivity.class);
                                startActivity(ii);
                            }
                        });
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create(); // Create the Alert dialog
        alertDialog.show();  // Show the Alert Dialog box
    }
    public void checkNoOfPeopleHelpingNow(){
        rootReference.child("inDanger").child(girlUniqueIdplusDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num=((int) snapshot.getChildrenCount())-3;
                number_save.setText(Integer.toString(num));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void getNoOfPeopleWhoMarkedSafe(){
        girlReference.child("Safe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num=((int) snapshot.getChildrenCount());
                safeNo_save.setText(Integer.toString(num));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void addGirlData(){
        userReference.child(girlUniqueId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                String age=snapshot.child("age").getValue().toString();
                name_save.setText("Name : "+name);
                age_save.setText("Age : "+age);
                uniqueid_save.setText(girlUniqueId);
                fetchImage();

            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void refresh(){
        checkNoOfPeopleHelpingNow();
        addGirlData();
        getNoOfPeopleWhoMarkedSafe();
        if (temp==1) {
            setCoordinatesOfBoy();
        }
        //getDistance(30.2909096,78.0017146,30.2789104,77.9644066);
    }
    public void fetchImage() {
        storage= FirebaseStorage.getInstance();
        storageReferenceFetchImage=storage.getReferenceFromUrl("gs://shieldappsih.appspot.com/Profile/"+girlUniqueId+"/").child("profile_pic.jpg");

        final File file;
        try {
            file = File.createTempFile("image","jpg");
            storageReferenceFetchImage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    image_save.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void markSafe(){
        userReference.child(boyUniqueId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                girlReference.child("Safe").child(boyUniqueId).setValue(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public float getDistance(double lat1, double lon1, double lat2, double lon2) {
        android.location.Location homeLocation = new android.location.Location("");
        homeLocation .setLatitude(lat1);
        homeLocation .setLongitude(lon1);

        android.location.Location targetLocation = new android.location.Location("");
        targetLocation .setLatitude(lat2);
        targetLocation .setLongitude(lon2);

        float distanceInMeters =  targetLocation.distanceTo(homeLocation);

        return (distanceInMeters/1000) ;
    }
    public void addDistanceToTextView(){
        girlReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String latt1=snapshot.child("latt1").getValue().toString();
                final String longg1=snapshot.child("long1").getValue().toString();


                dangerReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String lat2=snapshot.child("latt2").getValue().toString();
                        String lon2=snapshot.child("long2").getValue().toString();

                        Float f=getDistance(Double.parseDouble(latt1),Double.parseDouble(longg1),Double.parseDouble(lat2),Double.parseDouble(lon2));
                        BigDecimal bd = new BigDecimal(Float.toString(f));
                        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
                        distanceAndDuration.setText("Distance : "+bd+ " Kilometers");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setCoordinatesOfBoy(){
        dangerReference.child("latt2").setValue(boyLat2);
        dangerReference.child("long2").setValue(boyLon2);
    }



    //Bhajan tera code he ye
    //ek function bana jo current user ka latitude and longitude dono de is Line ke baad
    //bas isi current daal de user ki coordinate dhoondh aur line 37 ke variables me
    //aur line 120 aur 127 wali lines ke beech apna code daalna aur wo dono line mitana mat



}


