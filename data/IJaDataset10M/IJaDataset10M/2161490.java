package pnc.fractal.ui.video;

import java.io.*;
import java.util.*;
import java.awt.Dimension;
import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.datasink.*;
import javax.media.format.VideoFormat;

/**
 * This program takes a list of JPEG image files and convert them into
 * a QuickTime movie.
 */
public class MovieCreator implements ControllerListener, DataSinkListener {

    public boolean createVideo(String videoPath, float frameRate, List imageList) {
        MediaLocator outML = new MediaLocator("file:" + videoPath);
        DataSource ids = new pnc.fractal.ui.video.DataSource(frameRate, imageList);
        try {
            ids.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Processor p;
        try {
            System.err.println("- create processor for the image datasource ...");
            p = Manager.createProcessor(ids);
        } catch (Exception e) {
            System.err.println("Yikes!  Cannot create a processor from the data source.");
            return false;
        }
        p.addControllerListener(this);
        p.configure();
        if (!waitForState(p, p.Configured)) {
            System.err.println("Failed to configure the processor.");
            return false;
        }
        p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.MSVIDEO));
        TrackControl tcs[] = p.getTrackControls();
        Format f[] = tcs[0].getSupportedFormats();
        if (f == null || f.length <= 0) {
            System.err.println("The mux does not support the input format: " + tcs[0].getFormat());
            return false;
        }
        tcs[0].setFormat(f[0]);
        System.err.println("Setting the track format to: " + f[0]);
        p.realize();
        if (!waitForState(p, p.Realized)) {
            System.err.println("Failed to realize the processor.");
            return false;
        }
        DataSink dsink;
        if ((dsink = createDataSink(p, outML)) == null) {
            System.err.println("Failed to create a DataSink for the given output MediaLocator: " + outML);
            return false;
        }
        dsink.addDataSinkListener(this);
        fileDone = false;
        System.err.println("start processing...");
        try {
            p.setRate(frameRate);
            System.out.println("duration : " + ids.getDuration().getSeconds());
            p.start();
            dsink.start();
        } catch (IOException e) {
            System.err.println("IO error during processing");
            return false;
        }
        waitForFileDone();
        try {
            dsink.close();
        } catch (Exception e) {
        }
        p.removeControllerListener(this);
        System.err.println("...done processing.");
        return true;
    }

    /**
   * Create the DataSink.
   */
    DataSink createDataSink(Processor p, MediaLocator outML) {
        javax.media.protocol.DataSource ds;
        if ((ds = p.getDataOutput()) == null) {
            System.err.println("Something is really wrong: the processor does not have an output DataSource");
            return null;
        }
        DataSink dsink;
        try {
            System.err.println("- create DataSink for: " + outML);
            dsink = Manager.createDataSink(ds, outML);
            dsink.open();
        } catch (Exception e) {
            System.err.println("Cannot create the DataSink: " + e);
            e.printStackTrace();
            return null;
        }
        return dsink;
    }

    Object waitSync = new Object();

    boolean stateTransitionOK = true;

    /**
   * Block until the processor has transitioned to the given state.
   * Return false if the transition failed.
   */
    boolean waitForState(Processor p, int state) {
        synchronized (waitSync) {
            try {
                while (p.getState() < state && stateTransitionOK) waitSync.wait();
            } catch (Exception e) {
            }
        }
        return stateTransitionOK;
    }

    /**
   * Controller Listener.
   */
    public void controllerUpdate(ControllerEvent evt) {
        System.out.println("evt" + evt.getClass().getName());
        if (evt instanceof ConfigureCompleteEvent || evt instanceof RealizeCompleteEvent || evt instanceof PrefetchCompleteEvent) {
            synchronized (waitSync) {
                stateTransitionOK = true;
                waitSync.notifyAll();
            }
        } else if (evt instanceof ResourceUnavailableEvent) {
            synchronized (waitSync) {
                stateTransitionOK = false;
                waitSync.notifyAll();
            }
        } else if (evt instanceof EndOfMediaEvent) {
            evt.getSourceController().stop();
            evt.getSourceController().close();
            fileDone = true;
            System.out.println("evt EOM");
        } else if (evt instanceof StopAtTimeEvent) {
            evt.getSourceController().stop();
            evt.getSourceController().close();
            System.out.println("time over");
            fileDone = true;
        }
    }

    Object waitFileSync = new Object();

    boolean fileDone = false;

    boolean fileSuccess = true;

    /**
   * Block until file writing is done. 
   */
    boolean waitForFileDone() {
        synchronized (waitFileSync) {
            try {
                while (!fileDone) waitFileSync.wait();
            } catch (Exception e) {
            }
        }
        return fileSuccess;
    }

    /**
   * Event handler for the file writer.
   */
    public void dataSinkUpdate(DataSinkEvent evt) {
        if (evt instanceof EndOfStreamEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                waitFileSync.notifyAll();
            }
        } else if (evt instanceof DataSinkErrorEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                fileSuccess = false;
                waitFileSync.notifyAll();
            }
        }
    }

    /**
   * Create a media locator from the given string.
   */
    static MediaLocator createMediaLocator(String url) {
        MediaLocator ml;
        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null) return ml;
        if (url.startsWith(File.separator)) {
            if ((ml = new MediaLocator("file:" + url)) != null) return ml;
        } else {
            String file = "file:" + System.getProperty("user.dir") + File.separator + url;
            if ((ml = new MediaLocator(file)) != null) return ml;
        }
        return null;
    }
}
