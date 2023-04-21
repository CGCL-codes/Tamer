package orxatas.travelme.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.markupartist.android.widget.ActionBar;
import orxatas.travelme.R;
import orxatas.travelme.databases.exceptions.PhotoNoInLocal;
import orxatas.travelme.databases.exceptions.PlaceNotInLocal;
import orxatas.travelme.entity.Entry;
import orxatas.travelme.entity.Geopoint;
import orxatas.travelme.entity.Group;
import orxatas.travelme.entity.Photo;
import orxatas.travelme.entity.Place;
import orxatas.travelme.manager.DataManager;
import orxatas.travelme.manager.EntryManager;
import orxatas.travelme.manager.GroupManager;
import orxatas.travelme.manager.PhotoManager;
import orxatas.travelme.manager.PlaceManager;
import orxatas.travelme.manager.exceptions.CantLoadPhotoFromFile;
import orxatas.travelme.sync.AsyncNoticeCode;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class NewEntryActivity extends Activity implements AsyncActivity {

    /**
	 * Entry's local id.
	 * */
    private int idOfflineEntry = -1;

    /**
	 * Last key inserted from image array.
	 * */
    private int nImag = 0;

    /**
	 * Images Uri array.
	 * */
    ArrayList<Uri> imageUris;

    private Group group;

    private Entry entry;

    /**
	 * Intent identificator of take photo from camera.
	 * */
    private static final int CAMERA_PIC_REQUEST = 1337;

    /**
	 * Unix date.
	 * */
    private Date date;

    /**
	 * Intent identificator of get photo from gallery.
	 * */
    private static final int SELECT_PICTURE = 1338;

    private static final String PICTURES_DIR = "/Pictures/";

    private static final String TRAVELME_PREFIX_IMAGE = "travelme_";

    private static final String JPG_SUFIX = ".jpg";

    protected static final int DATE_DIALOG_ID = 0;

    protected static final int FEELING_SAD = 0;

    protected static final int FEELING_NORMAL = 1;

    protected static final int FEELING_HAPPY = 2;

    /**
	 * Resource type.
	 * */
    private static final int PHOTOS = 0;

    private static final int FEELING = 1;

    private static final int TEXT = 2;

    /**
	 * Width and Height of photos linked to entry and showed in activity.
	 * */
    private final int widthIV = 100, heightIV = 100;

    /**
	 * Text linked to entry.
	 * */
    private String textEntry;

    /**
	 * Add text Dialog.
	 * */
    private Dialog addTextDialog;

    /**
	 * Process Dialog.
	 * */
    private ProgressDialog dialog = null;

    /**
	 * ActionBar
	 * */
    private ActionBar actionBar;

    /**
	 * Entry Manager.
	 * */
    private EntryManager entryManager;

    /**
	 * Photo Manager.
	 * */
    private PhotoManager photoManager;

    /**
	 * Place Manager.
	 * */
    private PlaceManager placeManager;

    /**
	 * Group Manager.
	 * */
    private GroupManager groupManager;

    /**
	 * View where entry linked photos appear.
	 * */
    private LinearLayout takenPhotos;

    /**
	 * View where entry linked text appear.
	 * */
    private TextView textNE, placeVT, groupVT, textDateVT;

    private Uri outputFileUriImageTaken = null;

    /**
	 * Place geoposition.
	 * */
    private Geopoint p;

    private int idplace, idgroup;

    /**
	 * Feeling buttons
	 * */
    private ImageButton feelingSadB, feelingNormalB, feelingHappyB;

    /**
	 * Gallery where display taken photos
	 * */
    private Gallery galleryTakenPhotos;

    /**
	 * Manage galleryTakenPhotos.
	 * */
    private ImageAdapter galleryAdapter;

    private Place place;

    private ArrayList<Integer> photos;

    /**
	 * Manager executed when a Intent action finishes.
	 * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CAMERA_PIC_REQUEST:
                if (resultCode == RESULT_OK && outputFileUriImageTaken != null) {
                    Bitmap imageBitmap1;
                    imageBitmap1 = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), getIDFromUriImage(outputFileUriImageTaken), MediaStore.Images.Thumbnails.MICRO_KIND, (BitmapFactory.Options) null);
                    imageUris.add(outputFileUriImageTaken);
                    imageBitmap1.recycle();
                    String realPath1 = (String) getRealPathFromURI(outputFileUriImageTaken);
                    galleryAddPic(realPath1);
                    String realPath = (String) getRealPathFromURI(outputFileUriImageTaken);
                    entryManager.addPhotoToEntry(idOfflineEntry, realPath);
                    entry = entryManager.getEntryNotEnded(idOfflineEntry);
                    drawPhotosOnActivity();
                } else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
                }
                break;
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        imageUris.add(imageUri);
                        String realPath1 = (String) getRealPathFromURI(imageUri);
                        entryManager.addPhotoToEntry(idOfflineEntry, realPath1);
                        entry = entryManager.getEntryNotEnded(idOfflineEntry);
                        drawPhotosOnActivity();
                    } else {
                        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
                    }
                } else break;
            default:
        }
    }

    /**
	 * Generates a image and shows it in a view.
	 * @param imageBitmap Imagen en bitmap a generar y mostrar en la interfaz.
	 * */
    public void genImageView(Bitmap imageBitmap) {
        ImageView image = new ImageView(this);
        image.setImageBitmap(imageBitmap);
        image.setAdjustViewBounds(true);
        image.setMaxWidth(widthIV);
        image.setMaxHeight(heightIV);
        takenPhotos.addView(image);
    }

    public class ImageAdapter extends ArrayAdapter<Integer> {

        private int mGalleryItemBackground;

        public ImageAdapter(Context context, int textViewResourceId, List<Integer> objects) {
            super(context, textViewResourceId, objects);
            TypedArray attr = context.obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground, 0);
            attr.recycle();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image = new ImageView(getContext());
            try {
                String path = photoManager.getPhoto(getItem(position)).getLocalPath();
                Bitmap bMap = photoManager.path2Bitmap(path);
                image.setLayoutParams(new Gallery.LayoutParams(200, 200));
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setImageBitmap(bMap);
                image.setBackgroundResource(mGalleryItemBackground);
            } catch (PhotoNoInLocal e) {
                e.printStackTrace();
            } catch (CantLoadPhotoFromFile e) {
                e.printStackTrace();
            }
            return image;
        }
    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
	 * And to convert the image URI to the direct file system path of the image file
	 * */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int getIDFromUriImage(Uri Image) {
        String[] proj = { MediaStore.Images.Media._ID };
        Cursor cursor = managedQuery(Image, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        cursor.moveToFirst();
        return cursor.getInt(column_index);
    }

    private OnClickListener takePhotoBListener = new OnClickListener() {

        public void onClick(View v) {
            outputFileUriImageTaken = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUriImageTaken);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    };

    private OnClickListener newEntryText = new OnClickListener() {

        public void onClick(View v) {
            EditText text = (EditText) addTextDialog.findViewById(R.id.newentry_addText_editText);
            textEntry = text.getText().toString();
            entryManager.setOptoinalText(idOfflineEntry, textEntry);
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            drawTextOnActivity(entry.getText());
            addTextDialog.dismiss();
        }
    };

    private OnClickListener addTextBListener = new OnClickListener() {

        public void onClick(View v) {
            addTextDialog = new Dialog(NewEntryActivity.this);
            addTextDialog.setContentView(R.layout.newentry_addtext);
            addTextDialog.setTitle(getResources().getString(R.string.newentry_addtext_title));
            Button newEntryTextB = (Button) addTextDialog.findViewById(R.id.newentry_addText_button);
            newEntryTextB.setOnClickListener(newEntryText);
            EditText text = (EditText) addTextDialog.findViewById(R.id.newentry_addText_editText);
            text.setText(textEntry);
            addTextDialog.show();
        }
    };

    private OnClickListener importImagesBListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
    };

    private void finishEntry() {
        entryManager.sendEntry(idOfflineEntry);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textEntry);
        startActivity(Intent.createChooser(shareIntent, "Share entry to.."));
        finish();
    }

    private OnClickListener feelingSadBListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            entryManager.addFeelingToEntry(idOfflineEntry, FEELING_SAD);
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            drawFeelingOnActivity(entry.getFeeling());
        }
    };

    private void drawTextOnActivity(String text) {
        textNE.setText(text);
    }

    private void drawDateOnActivity() {
        DateFormat df = DateFormat.getDateInstance();
        textDateVT.setText(df.format(date));
    }

    private void drawFeelingOnActivity(int FEELING) {
        switch(FEELING) {
            case FEELING_SAD:
                feelingSadB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_sad));
                disableOtherFeelingButtons(FEELING_SAD);
                break;
            case FEELING_NORMAL:
                feelingNormalB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_normal));
                disableOtherFeelingButtons(FEELING_NORMAL);
                break;
            case FEELING_HAPPY:
                feelingHappyB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_happy));
                disableOtherFeelingButtons(FEELING_HAPPY);
                break;
            default:
        }
    }

    private OnClickListener feelingNormalBListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            entryManager.addFeelingToEntry(idOfflineEntry, FEELING_NORMAL);
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            drawFeelingOnActivity(entry.getFeeling());
        }
    };

    private OnClickListener feelingHappyBListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            entryManager.addFeelingToEntry(idOfflineEntry, FEELING_HAPPY);
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            drawFeelingOnActivity(entry.getFeeling());
        }
    };

    /**
	 * Disable other buttons than selected.
	 * */
    private void disableOtherFeelingButtons(int feelingSelected) {
        switch(feelingSelected) {
            case FEELING_SAD:
                feelingHappyB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_happy_notselected));
                feelingNormalB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_normal_notselected));
                break;
            case FEELING_NORMAL:
                feelingSadB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_sad_notselected));
                feelingHappyB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_happy_notselected));
                break;
            case FEELING_HAPPY:
                feelingSadB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_sad_notselected));
                feelingNormalB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_normal_notselected));
                break;
            default:
                feelingSadB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_sad_notselected));
                feelingNormalB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_normal_notselected));
                feelingHappyB.setImageDrawable(getResources().getDrawable(R.drawable.ic_entry_feeling_happy_notselected));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private OnClickListener changeDateBListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            date = c.getTime();
            addDateToEntry();
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            drawDateOnActivity();
        }
    };

    @Override
    public void asyncNotice(AsyncNoticeCode code) {
        switch(code) {
            case GROUPS_CHANGED:
                group = groupManager.getGroup(idgroup);
                if (group != null) {
                    groupVT.setText(" " + group.getName());
                    if (idOfflineEntry == -1 && place != null) addNewEntry();
                }
                break;
            case PLACES_CHANGED:
                try {
                    place = placeManager.getPlace(idplace);
                    p = place.getPosition();
                    if (idOfflineEntry == -1 && group != null) {
                        addNewEntry();
                    }
                    placeVT.setText(" " + place.getName() + " ");
                } catch (PlaceNotInLocal e) {
                    e.printStackTrace();
                }
                break;
            case PHOTO_CHANGES:
                drawPhotosOnActivity();
                break;
            default:
        }
    }

    public void drawPhotosOnActivity() {
        photos = entry.getPhotos();
        galleryAdapter = new ImageAdapter(this, R.layout.gallery_element, photos);
        galleryTakenPhotos.setAdapter(galleryAdapter);
    }

    /**
	 * Restore application state
	 * */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        idOfflineEntry = savedInstanceState.getInt("idOfflineEntry");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
	 * Store application state.
	 * */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("idOfflineEntry", idOfflineEntry);
        savedInstanceState.putInt("idgroup", idgroup);
        savedInstanceState.putInt("idplace", idplace);
        if (outputFileUriImageTaken != null) savedInstanceState.putParcelable("uriPhotoTaken", outputFileUriImageTaken);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void addNewEntry() {
        entry = entryManager.addNewEntry(idgroup, idplace);
        idOfflineEntry = entry.getIdEntryOffline();
        addDateToEntry();
        entryManager.addFeelingToEntry(idOfflineEntry, -1);
    }

    public void addDateToEntry() {
        entryManager.addDateToEntry(idOfflineEntry, (int) (date.getTime() / 1000));
    }

    public void discardEntry() {
        entryManager.discartEntry(idOfflineEntry);
        finish();
    }

    public boolean entryIsComplete() {
        if (entry.getIdAutor() == 0 || entry.getDate() == 0 || entry.getIdGroup() == 0 || entry.getIdEntryOffline() == 0 || entry.getIdGroup() == -0 || entry.getIdPlace() == 0 || entry.getFeeling() == -1) return false;
        return true;
    }

    public void loadViews() {
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.addAction(new ActionBar.Action() {

            @Override
            public void performAction(View view) {
                if (entryIsComplete()) {
                    finishEntry();
                } else {
                    Toast.makeText(NewEntryActivity.this, "Faltan campos por a�adir.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public int getDrawable() {
                return R.drawable.ic_entry_finishentry;
            }
        });
        actionBar.addAction(new ActionBar.Action() {

            @Override
            public void performAction(View view) {
                discardEntry();
            }

            @Override
            public int getDrawable() {
                return R.drawable.ic_entry_discardentry;
            }
        });
        takenPhotos = (LinearLayout) findViewById(R.id.newentry_takenPhotos);
        Button changeDateB = (Button) findViewById(R.id.newentry_changeDate);
        changeDateB.setOnClickListener(changeDateBListener);
        textNE = (TextView) findViewById(R.id.newentry_textViewAdded);
        textDateVT = (TextView) findViewById(R.id.newentry_textDate);
        placeVT = (TextView) findViewById(R.id.newentry_placeName);
        groupVT = (TextView) findViewById(R.id.newentry_groupName);
        ImageButton takePhotoB = (ImageButton) findViewById(R.id.newentry_takePhotoIcon);
        takePhotoB.setOnClickListener(takePhotoBListener);
        ImageButton addTextB = (ImageButton) findViewById(R.id.newentry_addTextIcon);
        addTextB.setOnClickListener(addTextBListener);
        ImageButton importImagesB = (ImageButton) findViewById(R.id.newentry_importImages);
        importImagesB.setOnClickListener(importImagesBListener);
        feelingSadB = (ImageButton) findViewById(R.id.newentry_feeling_sad);
        feelingSadB.setOnClickListener(feelingSadBListener);
        feelingNormalB = (ImageButton) findViewById(R.id.newentry_feeling_normal);
        feelingNormalB.setOnClickListener(feelingNormalBListener);
        feelingHappyB = (ImageButton) findViewById(R.id.newentry_feeling_happy);
        feelingHappyB.setOnClickListener(feelingHappyBListener);
        galleryTakenPhotos = (Gallery) findViewById(R.id.newentry_galleryTakenPhotos);
        galleryAdapter = new ImageAdapter(this, R.layout.gallery_element, photos);
        galleryTakenPhotos.setAdapter(galleryAdapter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newentry);
        if (savedInstanceState != null) {
            idgroup = savedInstanceState.getInt("idgroup");
            idplace = savedInstanceState.getInt("idplace");
            idOfflineEntry = savedInstanceState.getInt("idOfflineEntry");
            outputFileUriImageTaken = savedInstanceState.getParcelable("uriPhotoTaken");
        }
        photos = new ArrayList<Integer>();
        imageUris = new ArrayList<Uri>();
        loadViews();
        idgroup = getIntent().getExtras().getInt("idgroup");
        idplace = getIntent().getExtras().getInt("idplace");
        photoManager = new PhotoManager(this);
        entryManager = new EntryManager(this);
        groupManager = new GroupManager(this);
        placeManager = new PlaceManager(this);
        actionBar.setTitle(getResources().getString(R.string.newentry_actionbar_text), getAssets(), "Ubuntu-M.ttf");
        group = groupManager.getGroup(idgroup);
        if (group != null) groupVT.setText(" " + group.getName());
        try {
            place = placeManager.getPlace(idplace);
            p = place.getPosition();
            placeVT.setText(" " + place.getName() + " ");
        } catch (PlaceNotInLocal e) {
            placeVT.setText(" " + (new Integer(idplace).toString()) + " ");
            e.printStackTrace();
        }
        date = new Date();
        date.getTime();
        if (idOfflineEntry != -1) {
            entry = entryManager.getEntryNotEnded(idOfflineEntry);
            if (entry != null) {
                date = new Date();
                long datel = ((long) entry.getDate()) * 1000;
                date.setTime(datel);
                drawFeelingOnActivity(entry.getFeeling());
                drawTextOnActivity(entry.getText());
                drawPhotosOnActivity();
            }
        } else if (group != null) {
            addNewEntry();
        }
        drawDateOnActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (addTextDialog != null) addTextDialog.dismiss();
    }

    @Override
    public void syncStarted() {
        actionBar.setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void syncEnded() {
        actionBar.setProgressBarVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new DataManager(this).activityEnds(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void syncAndWaitCallStarted() {
        if (dialog == null) {
            dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
            dialog.setCancelable(false);
        }
    }

    @Override
    public void syncAndWaitCallEnded(Object o, int syncAction) {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}
