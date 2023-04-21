package android.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.tiffviewer.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public abstract class FileDialog extends ListActivity {

    private static final String LOG_TAG = "FileDialog";

    /**
     * text we use for the parent directory
     */
    private static final String PARENT_DIR = "..";

    /**
     * Currently displayed files
     */
    private final List<String> mCurrentFiles = new ArrayList<String>();

    /**
     * Currently displayed directory
     */
    private File mCurrentDir = null;

    public void onPostFileAccessIntent(final File file) {
    }

    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        try {
            showDirectory("/");
        } catch (NullPointerException e) {
            showDirectory("/");
        }
        getListView().setSelector(getResources().getDrawable(R.drawable.bk_listitem_focus));
    }

    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        if (position == 0 && PARENT_DIR.equals(this.mCurrentFiles.get(0))) {
            showDirectory(this.mCurrentDir.getParent());
        } else {
            final File file = new File(this.mCurrentFiles.get(position));
            if (file.isDirectory()) {
                showDirectory(file.getAbsolutePath());
            } else {
                onPostFileAccessIntent(file);
            }
        }
    }

    /**
     * Show the contents of a given directory as a selectable list
     * 
     * @param path	the directory to display
     */
    private void showDirectory(final String path) {
        this.mCurrentFiles.clear();
        this.mCurrentDir = new File(path);
        if (this.mCurrentDir.getParentFile() != null) {
            this.mCurrentFiles.add(PARENT_DIR);
        }
        final File[] files = this.mCurrentDir.listFiles();
        final Set<String> sorted = new TreeSet<String>();
        if (null != files) {
            for (final File file : files) {
                final String name = file.getAbsolutePath();
                if (file.isDirectory()) {
                    sorted.add(name);
                } else {
                    final String extension = name.indexOf('.') > 0 ? name.substring(name.lastIndexOf('.') + 1) : "";
                    if (null == getAcceptedFileTypes() || getAcceptedFileTypes().contains(extension.toLowerCase())) {
                        sorted.add(name);
                    }
                }
            }
            this.mCurrentFiles.addAll(sorted);
        }
        final Context context = this;
        ArrayAdapter<String> filenamesAdapter = new ArrayAdapter<String>(this, getTextView(), this.mCurrentFiles) {

            public View getView(final int position, final View convertView, final ViewGroup parent) {
                return new IconifiedTextLayout(context, getItem(position), position);
            }
        };
        setListAdapter(filenamesAdapter);
    }

    class IconifiedTextLayout extends LinearLayout {

        public IconifiedTextLayout(final Context context, final String path, final int position) {
            super(context);
            setOrientation(HORIZONTAL);
            final ImageView imageView = new ImageView(context);
            final File file = new File(path);
            if (position == 0 && PARENT_DIR.equals(path)) {
                imageView.setImageResource(getParentFolderImage());
            } else {
                if (file.isDirectory()) {
                    if (file.getName().equalsIgnoreCase("system") || file.getName().equalsIgnoreCase("data")) imageView.setImageResource(getInternalStorageImage()); else if (file.getName().equalsIgnoreCase("sdcard")) imageView.setImageResource(getExternalStorageImage()); else imageView.setImageResource(getFolderImage());
                } else {
                    imageView.setImageResource(getFileImage());
                }
            }
            imageView.setPadding(0, 1, 5, 0);
            final TextView textView = new TextView(context);
            textView.setText(file.getName());
            Resources resources = getBaseContext().getResources();
            textView.setTextColor(resources.getColor(R.color.tab_tag_text_focus));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(24);
            addView(imageView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            addView(textView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
        }
    }

    /**
     * Get the list of file extensions which are accepted by the file dialog
     * 
     * @return	null to accept all files, or a list of lowercase file extensions
     */
    public abstract List<String> getAcceptedFileTypes();

    /**
     * Get the TextView resource used for the TextView inside the list
     * 
     * @return	layout resource id
     */
    public abstract int getTextView();

    /**
     * Get the image used for navigating to the parent folder
     * 
     * @return	image resource id
     */
    public abstract int getParentFolderImage();

    /**
     * Get the image denoting a folder
     * 
     * @return	image resource id
     */
    public abstract int getFolderImage();

    /**
     * Get the image denoting a file to select
     * 
     * @return	image resource id
     */
    public abstract int getFileImage();

    public abstract int getExternalStorageImage();

    public abstract int getInternalStorageImage();
}
