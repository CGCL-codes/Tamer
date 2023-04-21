package com.sts.webmeet.tests.client;

import com.sts.webmeet.content.client.appshare.*;
import com.sts.webmeet.content.common.appshare.*;
import java.awt.*;
import java.awt.image.*;
import java.util.zip.*;
import java.io.*;

public class AppViewer extends Frame implements Runnable {

    public AppViewer(int width, int height) {
        setTitle("App Viewer");
        show();
        setSize(width, height);
        view = new AppView(width, height);
        add(view);
        pack();
        invalidate();
        repaint();
    }

    public void run() {
        try {
            while (bContinue) {
                ScreenScrapeData data = (ScreenScrapeData) ois.readObject();
                System.out.println(getClass().getName() + ".run() " + "returned from readObject: " + data.getBytes().length);
                view.pixelUpdate(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInputStream(InputStream is) {
        try {
            ois = new ObjectInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startViewing() {
        thread = new Thread(this);
        bContinue = true;
        thread.start();
    }

    public void stopViewing() {
        bContinue = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AppView view;

    private boolean bContinue;

    private Thread thread;

    private ObjectInputStream ois;
}
