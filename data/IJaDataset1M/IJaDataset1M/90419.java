package org.chemlab.dealdroidapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
 * A simple ContentProvider that takes a site template, fills some values from
 * an Item object, and returns that reference using a content:// URI.
 * 
 * @author shade
 * @version $Id: SiteContentProvider.java 170 2011-04-08 00:42:11Z steve.kondik $
 */
public class SiteContentProvider extends ContentProvider {

    private static final String LOG_TAG = "DealDroid";

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        final Site site = Site.valueOf(uri.getPathSegments().get(0));
        final String outName = site.name().toLowerCase(Locale.getDefault()) + ".html";
        final Context c = getContext();
        c.deleteFile(outName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(c.openFileOutput(outName, Context.MODE_PRIVATE)), 8192);
            final String data = readAsset(outName);
            final String populated = populate(site, data);
            writer.write(populated);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return ParcelFileDescriptor.open(c.getFileStreamPath(outName), ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
	 * @param site
	 * @param template
	 * @return the template with fields populated with the current item data
	 */
    private String populate(final Site site, final String template) {
        final Database db = new Database(getContext());
        String p = template;
        try {
            db.open();
            final Item item = db.getCurrentItem(site);
            if (item == null) {
                throw new IllegalStateException("No data found for " + site.name());
            }
            final Uri link = site.applyAffiliation(item.getLink());
            p = p.replaceAll("\\{title\\}", item.getTitle());
            p = p.replaceAll("\\{buy_url\\}", link.toString().replace("steepcheap%2Findex.html", ""));
            p = p.replaceAll("\\{description\\}", item.getDescription());
            if (item.getShortDescription() != null) {
                p = p.replaceAll("\\{short_description\\}", item.getShortDescription());
            }
            if (item.getImageLink() != null) {
                p = p.replaceAll("\\{image_url\\}", item.getImageLink().toString());
            }
            if (item.getSalePrice() != null) {
                p = p.replaceAll("\\{price\\}", item.getSalePrice());
            }
            if (item.getSavings() != null) {
                p = p.replaceAll("\\{savings\\}", item.getSavings());
            }
        } finally {
            db.close();
        }
        return p;
    }

    /**
	 * @param assetName
	 * @return the requested asset as a String
	 */
    private String readAsset(final String assetName) {
        BufferedReader reader = null;
        final StringBuffer sb = new StringBuffer();
        try {
            final InputStream asset = this.getContext().getAssets().open(assetName);
            reader = new BufferedReader(new InputStreamReader(asset), 8192);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Log.e(LOG_TAG, ex.getMessage(), ex);
                }
            }
        }
        return sb.toString();
    }
}
