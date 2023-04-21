package com.commonsware.android.SquirrelCam;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SquirrelCam extends Activity implements View.OnClickListener {

    private static void waiting(int n) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while (t1 - t0 < n);
    }

    private Dialog savedialog;

    private Button btn;

    private Button vbtn;

    private Button sbtn;

    private ImageView img;

    private Bitmap bmn = null;

    private TextView txt;

    private FileOutputStream fos;

    private Socket socket;

    private PrintWriter printWriter;

    private Boolean vfirst = true;

    private Boolean vloop = false;

    private Boolean connected = false;

    private final String request = "GET / WCS/0.50";

    private final String mimeType = "image/jpeg";

    private String sharedfilename = "";

    private final int sharebtnid = 4050;

    public static final int WEB_ID = Menu.FIRST + 1;

    public static final int GALLERY_ID = Menu.FIRST + 2;

    public static final int DONATE_ID = Menu.FIRST + 3;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            setProgressBarIndeterminateVisibility(false);
        }
    };

    private boolean applyMenuChoice(MenuItem item) {
        switch(item.getItemId()) {
            case WEB_ID:
                String str = getString(R.string.about_squirrels);
                TextView wv = new TextView(this);
                wv.setPadding(16, 0, 16, 16);
                wv.setText(str);
                ScrollView scwv = new ScrollView(this);
                scwv.addView(wv);
                Dialog dialog = new Dialog(this) {

                    @Override
                    public boolean onKeyDown(int keyCode, KeyEvent event) {
                        if (keyCode != KeyEvent.KEYCODE_DPAD_LEFT) this.dismiss();
                        return true;
                    }
                };
                dialog.setTitle("About the Squirrels");
                dialog.addContentView(scwv, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                dialog.show();
                return (true);
            case GALLERY_ID:
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jdeslip.com/squirrel_gallery/index.php#thumbs"));
                startActivity(myIntent);
                return (true);
            case DONATE_ID:
                Intent myIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jdeslip.com/squirrel_gallery2/index.php#thumbs"));
                startActivity(myIntent2);
                return (true);
        }
        return (false);
    }

    private boolean connect() {
        try {
            socket = new Socket("civet.berkeley.edu", 8889);
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            txt.post(new Runnable() {

                public void run() {
                    txt.setText(R.string.camera_offline);
                }
            });
            return false;
        }
        return true;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            txt.post(new Runnable() {

                public void run() {
                    txt.setText(R.string.cant_connect);
                }
            });
        }
        return bm;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                pressedMainButton(btn);
                break;
            case R.id.buttonsave:
                pressedSave(sbtn);
                break;
            case R.id.buttonvideo:
                pressedVideo(vbtn);
                break;
            case sharebtnid:
                savedialog.dismiss();
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType(mimeType);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.share_header);
                getString(R.string.share_body);
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(sharedfilename));
                startActivity(Intent.createChooser(i, getString(R.string.share_image)));
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.main);
        btn = (Button) findViewById(R.id.button);
        vbtn = (Button) findViewById(R.id.buttonvideo);
        sbtn = (Button) findViewById(R.id.buttonsave);
        img = (ImageView) findViewById(R.id.icon);
        txt = (TextView) findViewById(R.id.tbox);
        if (vloop) {
            vbtn.setText(R.string.stop_video);
        }
        if (bmn != null) {
            try {
                img.setImageBitmap(bmn);
                txt.setText(R.string.connected);
            } catch (Exception ef) {
                updateImage();
            }
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        setProgressBarIndeterminateVisibility(false);
        btn = (Button) findViewById(R.id.button);
        vbtn = (Button) findViewById(R.id.buttonvideo);
        sbtn = (Button) findViewById(R.id.buttonsave);
        img = (ImageView) findViewById(R.id.icon);
        txt = (TextView) findViewById(R.id.tbox);
        btn.setOnClickListener(this);
        vbtn.setOnClickListener(this);
        sbtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        populateMenu(menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vloop = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vloop) {
            vbtn.setText(R.string.paused);
            vloop = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (vloop) {
            vbtn.setText(R.string.paused);
            vloop = false;
        }
    }

    private void populateMenu(Menu menu) {
        menu.add(Menu.NONE, WEB_ID, Menu.NONE, "About Our Squirrels");
        menu.add(Menu.NONE, GALLERY_ID, Menu.NONE, "Gallery 1");
        menu.add(Menu.NONE, DONATE_ID, Menu.NONE, "Gallery 2");
    }

    public void pressedMainButton(View button) {
        updateImage();
    }

    public void pressedSave(View buttonsave) {
        saveImage();
    }

    public void pressedVideo(View buttonvideo) {
        if (vloop) {
            vbtn.setText(R.string.start_video);
            vloop = false;
        } else {
            vloop = true;
            updateImage();
            updateLoop();
            vbtn.setText(R.string.stop_video);
        }
    }

    private void saveImage() {
        Boolean vstorage = false;
        String filedir = "";
        String mdir = "" + Environment.getExternalStorageDirectory() + "/SquirrelCam";
        File msdir = new File(mdir);
        msdir.mkdir();
        if (msdir.exists()) {
            filedir = mdir + "/";
            vstorage = true;
        } else {
            File sddir = new File("/sdcard/SquirrelCam");
            sddir.mkdir();
            if (sddir.exists()) {
                filedir = "/sdcard/SquirrelCam/";
                vstorage = true;
            } else {
                File emdir = new File("/emmc/EMusic");
                emdir.mkdir();
                if (emdir.exists()) {
                    filedir = "/emmc/SquirrelCam/";
                    vstorage = true;
                } else {
                    emdir = new File("/media/EMusic");
                    emdir.mkdir();
                    if (emdir.exists()) {
                        filedir = "/media/SquirrelCam/";
                        vstorage = true;
                    } else {
                        txt.setText(R.string.no_storage);
                    }
                }
            }
        }
        if (vstorage) {
            try {
                Date dateNow = new Date();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String filename = new String(dateformat.format(dateNow));
                filename = filedir + filename + ".jpg";
                fos = new FileOutputStream(filename);
                bmn.compress(CompressFormat.JPEG, 95, fos);
                fos.flush();
                fos.close();
                new MediaScannerNotifier(this, filename, mimeType);
                String str = "Wrote " + filename;
                TextView wv = new TextView(this);
                wv.setPadding(12, 0, 0, 0);
                wv.setText(str);
                sharedfilename = "file://" + filename;
                Bitmap bmn = getImageBitmap("file://" + filename);
                ImageView simg = new ImageView(this);
                simg.setImageBitmap(bmn);
                Button sharebutton = new Button(this);
                sharebutton.setText("Share?");
                sharebutton.setId(sharebtnid);
                sharebutton.setOnClickListener(this);
                ScrollView scwv = new ScrollView(this);
                LinearLayout linlay = new LinearLayout(this);
                linlay.setOrientation(1);
                linlay.setPadding(12, 0, 16, 16);
                LinearLayout linlayh = new LinearLayout(this);
                linlayh.setPadding(7, 0, 0, 12);
                linlayh.addView(simg, new LayoutParams(88, 66));
                linlayh.addView(wv);
                linlay.addView(linlayh);
                linlay.addView(sharebutton);
                scwv.addView(linlay);
                savedialog = new Dialog(this) {

                    @Override
                    public boolean onKeyDown(int keyCode, KeyEvent event) {
                        if (keyCode != KeyEvent.KEYCODE_DPAD_LEFT) this.dismiss();
                        return true;
                    }
                };
                savedialog.setTitle("Saved Image");
                savedialog.addContentView(scwv, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                savedialog.show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.cant_save, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateImage() {
        setProgressBarIndeterminateVisibility(true);
        Thread t2 = new Thread() {

            @Override
            public void run() {
                txt.post(new Runnable() {

                    public void run() {
                        txt.setText(R.string.refreshing);
                    }
                });
                if (vfirst) {
                    vfirst = false;
                    connected = connect();
                }
                if (connected) {
                    try {
                        printWriter.println(request);
                        printWriter.flush();
                    } catch (Exception e) {
                    }
                }
                try {
                    bmn = getImageBitmap("http://civet.berkeley.edu:8889");
                    if (bmn != null) {
                        img.post(new Runnable() {

                            public void run() {
                                img.setImageBitmap(bmn);
                            }
                        });
                        txt.post(new Runnable() {

                            public void run() {
                                txt.setText(R.string.connected);
                            }
                        });
                    } else {
                        txt.post(new Runnable() {

                            public void run() {
                                txt.setText(R.string.no_frame);
                            }
                        });
                    }
                } catch (Exception e) {
                    txt.post(new Runnable() {

                        public void run() {
                            txt.setText(R.string.no_frame);
                        }
                    });
                }
                handler.sendEmptyMessage(0);
            }
        };
        t2.start();
    }

    private void updateLoop() {
        Thread t = new Thread() {

            @Override
            public void run() {
                while (vloop) {
                    if (connected) {
                        try {
                            printWriter.println(request);
                            printWriter.flush();
                        } catch (Exception e) {
                        }
                    }
                    try {
                        final Bitmap tbmn = getImageBitmap("http://civet.berkeley.edu:8889");
                        if (tbmn != null) {
                            bmn = tbmn;
                            img.post(new Runnable() {

                                public void run() {
                                    img.setImageBitmap(tbmn);
                                }
                            });
                        } else {
                            vloop = false;
                            vbtn.post(new Runnable() {

                                public void run() {
                                    vbtn.setText(R.string.start_video);
                                }
                            });
                            txt.post(new Runnable() {

                                public void run() {
                                    txt.setText(R.string.no_video);
                                }
                            });
                        }
                        waiting(10);
                    } catch (Exception e) {
                        txt.post(new Runnable() {

                            public void run() {
                                txt.setText(R.string.no_video);
                            }
                        });
                        vloop = false;
                        vbtn.post(new Runnable() {

                            public void run() {
                                vbtn.setText(R.string.start_video);
                            }
                        });
                    }
                }
            }
        };
        t.start();
    }
}
