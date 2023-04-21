package org.openscience.jmolandroid.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;

public class Downloader {

    private Context context;

    private Handler handler;

    ProgressDialog progressDialog;

    File file;

    public Downloader(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public static File getAppDir(Context context) {
        File path = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = new File(Environment.getExternalStorageDirectory(), "/Android/data/org.openscience.jmolandroid/files/");
        } else {
            path = context.getFilesDir();
        }
        if (!path.exists()) path.mkdirs();
        return path;
    }

    public void download(final String id) {
        File path = Downloader.getAppDir(context);
        file = new File(path, id + ".pdb.gz");
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("File " + id + " already exists. Download again?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    file.delete();
                    dialog.cancel();
                    Downloader.this.startDownload(file);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            startDownload(file);
        }
    }

    private void startDownload(final File file) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        });
        try {
            URL url = new URL("http://www.pdb.org/pdb/files/" + file.getName());
            InputStream stream = url.openStream();
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = stream.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
