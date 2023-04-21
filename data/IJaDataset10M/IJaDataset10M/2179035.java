package com.peterhi.client.ui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author YUN TAO
 */
public class Snippet141 {

    static Display display;

    static Shell shell;

    static GC shellGC;

    static Color shellBackground;

    static ImageLoader loader;

    static ImageData[] imageDataArray;

    static Thread animateThread;

    static Image image;

    static final boolean useGIFBackground = false;

    static int imageDataIndex = 0;

    static Image offScreenImage;

    static GC offScreenImageGC;

    static ImageData imageData;

    static int repeatCount;

    static ScheduledExecutorService se = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        display = new Display();
        shell = new Shell(display);
        shell.setSize(300, 300);
        shell.open();
        shellGC = new GC(shell);
        shellBackground = shell.getBackground();
        FileDialog dialog = new FileDialog(shell);
        dialog.setFilterExtensions(new String[] { "*.gif" });
        String fileName = dialog.open();
        if (fileName != null) {
            loader = new ImageLoader();
            try {
                imageDataArray = loader.load(fileName);
                if (imageDataArray.length > 1) {
                    animateThread = new Thread("Animation") {

                        @Override
                        public void run() {
                            offScreenImage = new Image(display, loader.logicalScreenWidth, loader.logicalScreenHeight);
                            offScreenImageGC = new GC(offScreenImage);
                            offScreenImageGC.setBackground(shellBackground);
                            offScreenImageGC.fillRectangle(0, 0, loader.logicalScreenWidth, loader.logicalScreenHeight);
                            try {
                                render();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                            }
                        }
                    };
                    animateThread.setDaemon(true);
                    animateThread.start();
                }
            } catch (SWTException ex) {
                System.out.println("There was an error loading the GIF");
            }
        }
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static void render() {
        imageData = imageDataArray[imageDataIndex];
        if (image != null && !image.isDisposed()) {
            image.dispose();
        }
        image = new Image(display, imageData);
        offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);
        render2();
    }

    private static void render2() {
        repeatCount = loader.repeatCount;
        switch(imageData.disposalMethod) {
            case SWT.DM_FILL_BACKGROUND:
                Color bgColor = null;
                if (useGIFBackground && loader.backgroundPixel != -1) {
                    bgColor = new Color(display, imageData.palette.getRGB(loader.backgroundPixel));
                }
                offScreenImageGC.setBackground(bgColor != null ? bgColor : shellBackground);
                offScreenImageGC.fillRectangle(imageData.x, imageData.y, imageData.width, imageData.height);
                if (bgColor != null) {
                    bgColor.dispose();
                }
                break;
            case SWT.DM_FILL_PREVIOUS:
                offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);
                break;
        }
        imageDataIndex = (imageDataIndex + 1) % imageDataArray.length;
        imageData = imageDataArray[imageDataIndex];
        image.dispose();
        image = new Image(display, imageData);
        offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);
        shellGC.drawImage(offScreenImage, 0, 0);
        int ms = imageData.delayTime * 10;
        if (ms < 20) {
            ms += 30;
        }
        if (ms < 30) {
            ms += 10;
        }
        se.schedule(new Runnable() {

            public void run() {
                render2();
            }
        }, ms, TimeUnit.MILLISECONDS);
        if (imageDataIndex == imageDataArray.length - 1) {
            repeatCount--;
        }
    }
}
