package org.nees.iSight;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTRuntimeHandler;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.sg.SequenceGrabber;
import quicktime.std.sg.SGVideoChannel;
import quicktime.util.RawEncodedImage;

public class ISightToByteStream {

    private SequenceGrabber grabber;

    private SGVideoChannel channel;

    private RawEncodedImage rowEncodedImage;

    private int width;

    private int height;

    private int videoWidth;

    private int[] pixels;

    private BufferedImage image;

    private WritableRaster raster;

    public ISightToByteStream(int width, int height) throws QTException {
        this.width = width;
        this.height = height;
        QTSessionCheck.check();
        QDRect bounds = new QDRect(width, height);
        QDGraphics graphics = new QDGraphics(bounds);
        grabber = new SequenceGrabber();
        grabber.setGWorld(graphics, null);
        channel = new SGVideoChannel(grabber);
        channel.setBounds(bounds);
        channel.setUsage(StdQTConstants.seqGrabPreview);
        grabber.prepare(true, false);
        grabber.startPreview();
        PixMap pixMap = graphics.getPixMap();
        rowEncodedImage = pixMap.getPixelData();
        videoWidth = width + (rowEncodedImage.getRowBytes() - width * 4) / 4;
        pixels = new int[videoWidth * height];
        image = new BufferedImage(videoWidth, height, BufferedImage.TYPE_INT_RGB);
        raster = WritableRaster.createPackedRaster(DataBuffer.TYPE_INT, videoWidth, height, new int[] { 0x00ff0000, 0x0000ff00, 0x000000ff }, null);
        raster.setDataElements(0, 0, videoWidth, height, pixels);
        image.setData(raster);
        QTRuntimeException.registerHandler(new QTRuntimeHandler() {

            public void exceptionOccurred(QTRuntimeException e, Object eGenerator, String methodNameIfKnown, boolean unrecoverableFlag) {
                System.out.println("what should i do?");
            }
        });
    }

    public void dispose() {
        try {
            grabber.stop();
            grabber.release();
            grabber.disposeChannel(channel);
            image.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return height;
    }

    private BufferedImage getNextImage() throws StdQTException {
        grabber.idle();
        rowEncodedImage.copyToArray(0, pixels, 0, pixels.length);
        raster.setDataElements(0, 0, videoWidth, height, pixels);
        image.setData(raster);
        return image;
    }

    private void writeNextImageToOutputStream(String formatName, OutputStream theStream) throws IllegalArgumentException, IOException, StdQTException {
        if (!canWriteFormat(formatName)) throw new IllegalArgumentException("Unrecognized format type for image write: " + formatName);
        BufferedImage im = getNextImage();
        ImageIO.write(im, formatName, theStream);
    }

    private boolean canWriteFormat(String formatName) {
        Iterator iter = ImageIO.getImageWritersByFormatName(formatName);
        return iter.hasNext();
    }

    OutputStreamByteCollector theByteCollector = new OutputStreamByteCollector();

    public byte[] getNextByteArray() throws IllegalArgumentException, StdQTException, IOException {
        theByteCollector.reset();
        writeNextImageToOutputStream("jpeg", (OutputStream) theByteCollector);
        return theByteCollector.getBufferCopy();
    }

    public static void main(String[] args) {
        try {
            byte[] x;
            ISightToByteStream v = new ISightToByteStream(640, 480);
            long now, time = System.currentTimeMillis();
            for (int i = 1; i < 11; i++) {
                x = v.getNextByteArray();
                now = System.currentTimeMillis();
                System.out.println("Array " + i + " size = " + x.length + "; took " + (time - now) + " milliseconds.");
                time = now;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
