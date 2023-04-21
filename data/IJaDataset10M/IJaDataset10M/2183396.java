package pnc.fractal.ui.video;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;
import java.io.IOException;
import java.util.*;

public class LiveStream implements PushBufferStream, Runnable {

    private ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW);

    private int maxDataLength;

    private int[] data;

    private Dimension size;

    private RGBFormat rgbFormat;

    private boolean started;

    private Thread thread;

    private float frameRate = 1f;

    private BufferTransferHandler transferHandler;

    private Control[] controls = new Control[0];

    private int currentFrameIndex = 0;

    private java.util.List imageList;

    private int width;

    private int height;

    public LiveStream(float frameRate, java.util.List imageList) {
        this.frameRate = frameRate;
        this.imageList = imageList;
        width = ((BufferedImage) imageList.get(0)).getWidth();
        height = ((BufferedImage) imageList.get(0)).getHeight();
        size = new Dimension(width, height);
        maxDataLength = size.width * size.height * 3;
        rgbFormat = new RGBFormat(size, maxDataLength, Format.intArray, frameRate, 32, 0xFF0000, 0xFF00, 0xFF, 1, size.width, VideoFormat.FALSE, Format.NOT_SPECIFIED);
        data = new int[maxDataLength];
        thread = new Thread(this, "Screen Grabber");
        System.out.println("LiveStream of " + imageList.size() + " images created.");
        System.out.println(" Frame Rate " + frameRate);
    }

    /***************************************************************************
   * SourceStream
   ***************************************************************************/
    public ContentDescriptor getContentDescriptor() {
        return cd;
    }

    public long getContentLength() {
        return LENGTH_UNKNOWN;
    }

    public boolean endOfStream() {
        return false;
    }

    /***************************************************************************
   * PushBufferStream
   ***************************************************************************/
    int seqNo = 0;

    public Format getFormat() {
        return rgbFormat;
    }

    public void read(Buffer buffer) throws IOException {
        synchronized (this) {
            if (currentFrameIndex == imageList.size()) {
                buffer.setFlags(Buffer.FLAG_EOM);
                System.out.println("Last Frame");
                return;
            }
            System.out.println("Adding frame " + currentFrameIndex);
            Object outdata = buffer.getData();
            if (outdata == null || !(outdata.getClass() == Format.intArray) || ((int[]) outdata).length < maxDataLength) {
                outdata = new int[maxDataLength];
                buffer.setData(outdata);
            }
            buffer.setFormat(rgbFormat);
            buffer.setTimeStamp((long) (seqNo * (1000 / frameRate) * 1000000));
            BufferedImage bi = (BufferedImage) imageList.get(currentFrameIndex);
            currentFrameIndex++;
            bi.getRGB(0, 0, width, height, (int[]) outdata, 0, width);
            buffer.setSequenceNumber(seqNo);
            buffer.setLength(maxDataLength);
            buffer.setFlags(Buffer.FLAG_KEY_FRAME);
            buffer.setHeader(null);
            seqNo++;
        }
    }

    public void setTransferHandler(BufferTransferHandler transferHandler) {
        synchronized (this) {
            this.transferHandler = transferHandler;
            notifyAll();
        }
    }

    void start(boolean started) {
        synchronized (this) {
            this.started = started;
            if (started && !thread.isAlive()) {
                thread = new Thread(this);
                thread.start();
            }
            notifyAll();
        }
    }

    /***************************************************************************
   * Runnable
   ***************************************************************************/
    public void run() {
        while (started) {
            synchronized (this) {
                while (transferHandler == null && started) {
                    try {
                        wait(1000);
                    } catch (InterruptedException ie) {
                    }
                }
            }
            if (started && transferHandler != null) {
                transferHandler.transferData(this);
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException ise) {
                }
            }
        }
    }

    public Object[] getControls() {
        return controls;
    }

    public Object getControl(String controlType) {
        try {
            Class cls = Class.forName(controlType);
            Object cs[] = getControls();
            for (int i = 0; i < cs.length; i++) {
                if (cls.isInstance(cs[i])) return cs[i];
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
