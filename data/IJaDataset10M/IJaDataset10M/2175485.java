package kobetool.dshowwrapper;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * DirectShow video capture java binding<br>
 * Require Win32 DirectX9+ & java1.5+<br>
 * All APIs must be used in one thread. Including the initialization of class of CaptureDevice.<br>
 * Any feedback is welcome.
 * e-mail: kobevaliant@gmail.com
 * @author kobe
 * @see Test
 */
public class CaptureDevice extends Canvas {

    static {
        try {
            System.loadLibrary("awt");
        } catch (UnsatisfiedLinkError e) {
        }
        try {
            System.loadLibrary("jawt");
        } catch (UnsatisfiedLinkError e) {
        }
        System.loadLibrary("dshowwrap");
    }

    private String friendlyName, description, devicePath;

    private int pSrcFilter;

    private CaptureDevice(String friendlyName, String description, String devicePath, int pSrcFilter) {
        this.friendlyName = friendlyName;
        this.description = description;
        this.devicePath = devicePath;
        this.pSrcFilter = pSrcFilter;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getDescription() {
        return description;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public int getPSrcFilter() {
        return pSrcFilter;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaptureDevice that = (CaptureDevice) o;
        if (pSrcFilter != that.pSrcFilter) return false;
        return true;
    }

    public int hashCode() {
        return pSrcFilter;
    }

    public String toString() {
        return "CaptureDevice{" + "friendlyName='" + friendlyName + '\'' + ", description='" + description + '\'' + ", devicePath='" + devicePath + '\'' + '}';
    }

    public static native List<CaptureDevice> enumDevices();

    public native boolean start(VideoFormat format, boolean hFlip);

    public native boolean stop();

    public native boolean destroy();

    /**
     * Get a frame of video.<br>
     * The format is described by VideoFormat.getFormat()
     * @see #getFormat()
     * @param buf
     * @return return parameter buf if its capacity is enough. Otherwise return a new ByteBuffer.
     */
    public native ByteBuffer grab(ByteBuffer buf);

    public native void paint(Graphics g);

    public native void setBounds(int x, int y, int width, int height);

    public native boolean isRunning();

    public native VideoFormat getFormat();

    public native List<VideoFormat> queryFormats();
}
