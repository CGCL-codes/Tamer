package edu.vub.at.demo.pixee;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import edu.vub.at.demo.pixee.adapters.PixAdapter;
import edu.vub.at.demo.pixee.at.PixeeHub;
import edu.vub.at.demo.pixee.db.PixeeDB;
import edu.vub.at.demo.pixee.util.Pix;

public class PixGrid extends Activity {

    private static final int ACTIVITY_SELECT_IMAGE = 0;

    protected static final int PICTURE_INFO_DIALOG = 0;

    private GridView pixGrid_;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pixgrid);
        pixGrid_ = (GridView) findViewById(R.id.pixgrid);
        final View emptyView = getLayoutInflater().inflate(R.layout.no_pix_found, null);
        addContentView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        pixGrid_.setEmptyView(emptyView);
        pixGrid_.setAdapter(new PixAdapter(this));
        getPixeeHub().setPixAdapter(this.getAdapter());
        pixGrid_.setScrollingCacheEnabled(true);
        pixGrid_.setOnScrollListener(new AbsListView.OnScrollListener() {

            private boolean atBottom = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (!atBottom) {
                        atBottom = true;
                        Log.d("Pixee", "MyScrollView: Bottom has been reached, try to load moar images");
                        getAdapter().loadMore();
                    }
                } else {
                    atBottom = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
        registerForContextMenu(pixGrid_);
        pixGrid_.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id) {
                showPix(position);
            }
        });
        pixGrid_.setLongClickable(false);
        pixGrid_.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("uri", getAdapter().getItem(position));
                showDialog(PICTURE_INFO_DIALOG, bundle);
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if (id == PICTURE_INFO_DIALOG) {
            Dialog d = new Dialog(this);
            d.setContentView(R.layout.picture_info_dialog);
            d.setCancelable(true);
            d.setCanceledOnTouchOutside(true);
            d.setTitle("Image information");
            return d;
        }
        return super.onCreateDialog(id, args);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        if (id == PICTURE_INFO_DIALOG) {
            Uri uri = args.getParcelable("uri");
            ImageView iv = (ImageView) dialog.findViewById(R.id.picture);
            TextView date = (TextView) dialog.findViewById(R.id.date);
            TextView pixeer = (TextView) dialog.findViewById(R.id.pixeer);
            iv.setImageBitmap(getAdapter().getBitmap(uri));
            String[] info = getAdapter().getInfo(uri);
            date.setText(info[3]);
            String pixeer_id = info[2];
            if (pixeer_id.equals(getPixeeHub().getMe().getId())) {
                pixeer.setText("You");
            } else {
                pixeer.setText(info[1]);
            }
        }
        super.onPrepareDialog(id, dialog, args);
    }

    public void onDestroy() {
        this.getAdapter().close();
        super.onDestroy();
    }

    private PixAdapter getAdapter() {
        return (PixAdapter) pixGrid_.getAdapter();
    }

    public SQLiteDatabase getWritablePixeeDB() {
        PixeeDB db = new PixeeDB(this);
        return db.getWritableDatabase();
    }

    public SQLiteDatabase getReadablePixeeDB() {
        PixeeDB db = new PixeeDB(this);
        return db.getReadableDatabase();
    }

    private void showPix(int position) {
        Uri imageUri = getAdapter().getItem(position);
        startActivity(new Intent(Intent.ACTION_VIEW, imageUri));
    }

    private PixeeHub getPixeeHub() {
        return ((Pixee) this.getParent()).getPixeeHub();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pixgrid, menu);
        getParent().onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addpix:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                return true;
            default:
                return getParent().onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Pix newPix = getAdapter().share(selectedImage, getPixeeHub().getMe());
                    getPixeeHub().getMain().sharePix(newPix);
                }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.pixgrid) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.pixdetail, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch(item.getItemId()) {
            case R.id.removepix:
                PixAdapter pixAdapter = getAdapter();
                pixAdapter.removeUri(pixAdapter.getItem(position));
                return true;
            case R.id.viewpix:
                showPix(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
