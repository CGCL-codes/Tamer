package br.unb.unbiquitous.ubiquitos.app.hydra.screen;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.StringTokenizer;

public class LiveStream implements PushBufferStream, Runnable {

    private static Logger logger = Logger.getLogger(LiveStream.class);

    protected ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);

    protected int maxDataLength;

    protected Dimension size;

    protected RGBFormat rgbFormat;

    protected boolean started;

    protected Thread thread;

    protected float frameRate = 1f;

    protected BufferTransferHandler transferHandler;

    protected Control[] controls = new Control[0];

    protected int x, y, width, height;

    protected Robot robot = null;

    public LiveStream(MediaLocator locator) {
        try {
            parseLocator(locator);
        } catch (Exception e) {
            logger.error(e);
        }
        size = new Dimension(width, height);
        try {
            robot = new Robot();
        } catch (AWTException awe) {
            logger.error(awe.getMessage());
        }
        maxDataLength = size.width * size.height * 2;
        rgbFormat = new RGBFormat(size, maxDataLength, Format.intArray, frameRate, 32, 0xFF0000, 0xFF00, 0xFF, 1, size.width, VideoFormat.FALSE, Format.NOT_SPECIFIED);
        thread = new Thread(this, "Screen Grabber");
    }

    protected void parseLocator(MediaLocator locator) {
        String rem = locator.getRemainder();
        while (rem.startsWith("/") && rem.length() > 1) rem = rem.substring(1);
        StringTokenizer st = new StringTokenizer(rem, "/");
        if (st.hasMoreTokens()) {
            String position = st.nextToken();
            StringTokenizer nums = new StringTokenizer(position, ",");
            String stX = nums.nextToken();
            String stY = nums.nextToken();
            String stW = nums.nextToken();
            String stH = nums.nextToken();
            x = Integer.parseInt(stX);
            y = Integer.parseInt(stY);
            width = Integer.parseInt(stW);
            height = Integer.parseInt(stH);
        }
        if (st.hasMoreTokens()) {
            String stFPS = st.nextToken();
            frameRate = (Double.valueOf(stFPS)).floatValue();
        }
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
            Object outdata = buffer.getData();
            if (outdata == null || !(outdata.getClass() == Format.intArray) || ((int[]) outdata).length < maxDataLength) {
                outdata = new int[maxDataLength];
                buffer.setData(outdata);
            }
            buffer.setFormat(rgbFormat);
            buffer.setTimeStamp((long) (seqNo * (1000 / frameRate) * 1000000));
            BufferedImage bi = robot.createScreenCapture(new Rectangle(x, y, width, height));
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
                        logger.error(ie.getMessage());
                    }
                }
            }
            if (started && transferHandler != null) {
                transferHandler.transferData(this);
                try {
                    Thread.currentThread();
                    Thread.sleep(10);
                } catch (InterruptedException ise) {
                    logger.error(ise.getMessage());
                }
            }
        }
    }

    public Object[] getControls() {
        return controls;
    }

    public Object getControl(String controlType) {
        try {
            @SuppressWarnings("rawtypes") Class cls = Class.forName(controlType);
            Object cs[] = getControls();
            for (int i = 0; i < cs.length; i++) {
                if (cls.isInstance(cs[i])) return cs[i];
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
