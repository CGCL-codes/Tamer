package com.gampire.pc.capture;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;

@SuppressWarnings("deprecation")
public class QuickTimeCapture {

    int frameCount = 0;

    SequenceGrabber sg;

    QDRect cameraImageSize;

    QDGraphics gWorld;

    public int[] pixelData;

    BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    /**
	 * flag, indicating that all capture and display tasks should continue or
	 * not
	 */
    boolean cameraActive = true;

    /**
	 * Creates the LiveCam object.
	 */
    public QuickTimeCapture() {
        initSequenceGrabber();
        initBufferedImage();
    }

    /**
	 * Initializes the SequenceGrabber. Gets it's source video bounds, creates a
	 * gWorld with that size. Configures the video channel for grabbing,
	 * previewing and playing during recording.
	 */
    private void initSequenceGrabber() {
        try {
            sg = new SequenceGrabber();
            SGVideoChannel vc = new SGVideoChannel(sg);
            cameraImageSize = vc.getSrcVideoBounds();
            cameraImageSize.resize(640, 480);
            gWorld = new QDGraphics(cameraImageSize);
            sg.setGWorld(gWorld, null);
            vc.setBounds(cameraImageSize);
            vc.setUsage(quicktime.std.StdQTConstants.seqGrabRecord | quicktime.std.StdQTConstants.seqGrabPreview | quicktime.std.StdQTConstants.seqGrabPlayDuringRecord);
            vc.setFrameRate(0);
            vc.setCompressorType(quicktime.std.StdQTConstants.kComponentVideoCodecType);
        } catch (QTException e) {
            e.printStackTrace();
        }
    }

    /**
	 * This initializes the buffered image. First if determines the size of the
	 * data. Finds the number of ints per row, sets up the int array, where we
	 * can put the pixel data in. A DataBuffer is defined, using that int array.
	 * Based on this DataBuffer the BufferedImage is defined. The BufferedImage
	 * definition is not needed when the OpenGL view is used. There we only need
	 * the int-array pixelData. But I do not expect that defining the Buffered
	 * image has negative influence on OpenGLs performance.
	 */
    private void initBufferedImage() {
        int size = gWorld.getPixMap().getPixelData().getSize();
        int intsPerRow = gWorld.getPixMap().getPixelData().getRowBytes() / 4;
        size = intsPerRow * cameraImageSize.getHeight();
        pixelData = new int[size];
        DataBuffer db = new DataBufferInt(pixelData, size);
        ColorModel colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
        int[] masks = { 0x00ff0000, 0x0000ff00, 0x000000ff };
        WritableRaster raster = Raster.createPackedRaster(db, cameraImageSize.getWidth(), cameraImageSize.getHeight(), intsPerRow, masks, null);
        image = new BufferedImage(colorModel, raster, false, null);
    }

    /**
	 * This is a bit tricky. We do not start Previewing, but recording. By
	 * setting the output to a dummy file (which will never be created (hope
	 * so)) with the quicktime.std.StdQTConstants.seqGrabDontMakeMovie flag set.
	 * This seems to be equivalent to preview mode with the advantage, that it
	 * refreshes correctly.
	 */
    public void startPreviewing() throws Exception {
        @SuppressWarnings("unused") QTFile movieFile = new QTFile(new java.io.File("NoFile"));
        sg.setDataOutput(null, quicktime.std.StdQTConstants.seqGrabDontMakeMovie);
        sg.prepare(true, true);
        sg.startRecord();
        Runnable idleCamera = new Runnable() {

            int taskingDelay = 25;

            public void run() {
                try {
                    while (cameraActive) {
                        Thread.sleep(taskingDelay);
                        synchronized (sg) {
                            sg.idleMore();
                            sg.update(null);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        (new Thread(idleCamera)).start();
    }

    /**
	 * This creates a Panel, which displays the buffered image using awt. A
	 * Thread is started, copying the pixel data from the sequence grabbers
	 * gWorld to the data buffer of the BufferedImage. Then the image is
	 * repainted.
	 */
    public Component getVisualComponent() {
        final Component ret = new Component() {

            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                frameCount++;
            }

            ;
        };
        Runnable imageUpdate = new Runnable() {

            int taskingDelay = 100;

            public void run() {
                try {
                    while (cameraActive) {
                        synchronized (sg) {
                            gWorld.getPixMap().getPixelData().copyToArray(0, pixelData, 0, pixelData.length);
                            ret.repaint();
                        }
                        Thread.sleep(taskingDelay);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        (new Thread(imageUpdate)).start();
        return ret;
    }

    public QDRect getImageSize() {
        return cameraImageSize;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public static void main(String args[]) {
        try {
            QTSession.open();
            QuickTimeCapture myCam = new QuickTimeCapture();
            Frame cameraFrame = new Frame("LiveCam");
            myCam.startPreviewing();
            Component imagePanel = myCam.getVisualComponent();
            cameraFrame.add(imagePanel);
            cameraFrame.setBounds(100, 100, myCam.getImageSize().getWidth(), myCam.getImageSize().getHeight());
            cameraFrame.show();
            System.out.println("Counting frames:");
            Thread.sleep(5000);
            int fc = myCam.getFrameCount();
            Thread.sleep(10000);
            System.out.println("Frame rate:" + 0.1 * (myCam.getFrameCount() - fc));
        } catch (Exception ex) {
            ex.printStackTrace();
            QTSession.close();
        }
    }
}
