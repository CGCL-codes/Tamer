package jvidcap;

import javax.media.*;
import javax.media.protocol.*;
import javax.media.format.*;
import javax.media.control.*;
import java.util.Vector;

public class CaptureUtil {

    public static DataSource getCaptureDS(VideoFormat vf, AudioFormat af) {
        DataSource dsVideo = null;
        DataSource dsAudio = null;
        DataSource ds = null;
        if (vf != null) {
            dsVideo = createDataSource(vf);
            if (dsVideo == null) return null;
        }
        if (af != null) {
            dsAudio = createDataSource(af);
        }
        if (dsVideo != null) {
            dsVideo = new MonitorCDS(dsVideo);
            if (dsAudio == null) return dsVideo;
            ds = dsVideo;
        } else if (dsAudio != null) {
            return dsAudio;
        } else return null;
        try {
            ds = Manager.createMergingDataSource(new DataSource[] { dsAudio, dsVideo });
        } catch (IncompatibleSourceException ise) {
            return null;
        }
        return ds;
    }

    static DataSource createDataSource(Format format) {
        DataSource ds;
        Vector devices;
        CaptureDeviceInfo cdi;
        MediaLocator ml;
        devices = CaptureDeviceManager.getDeviceList(format);
        if (devices.size() < 1) {
            System.err.println("! No Devices for " + format);
            return null;
        }
        cdi = (CaptureDeviceInfo) devices.elementAt(0);
        ml = cdi.getLocator();
        try {
            ds = Manager.createDataSource(ml);
            ds.connect();
            if (ds instanceof CaptureDevice) {
                setCaptureFormat((CaptureDevice) ds, format);
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return ds;
    }

    static void setCaptureFormat(CaptureDevice cdev, Format format) {
        FormatControl[] fcs = cdev.getFormatControls();
        if (fcs.length < 1) return;
        FormatControl fc = fcs[0];
        Format[] formats = fc.getSupportedFormats();
        for (int i = 0; i < formats.length; i++) {
            if (formats[i].matches(format)) {
                format = formats[i].intersects(format);
                System.out.println("Setting format " + format);
                fc.setFormat(format);
                break;
            }
        }
    }
}
