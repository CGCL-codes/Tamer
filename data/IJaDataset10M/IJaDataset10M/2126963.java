package it.uniroma1.dis.omega.upnpqosmedia.video;

import it.uniroma1.dis.omega.upnpqosmedia.video.adapter.RTPSocketAdapter;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import javax.media.Codec;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.RemoteListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.RemoteEvent;
import javax.media.rtp.event.SessionEvent;
import javax.media.rtp.rtcp.SourceDescription;

public class AVTransmit implements ControllerListener, ReceiveStreamListener {

    private MediaLocator locator;

    private String ipAddress;

    private int portBase;

    private int tos;

    private boolean enabled = true;

    private Processor processor = null;

    private RTPManager rtpMgrs[];

    private DataSource dataOutput = null;

    /**
	 * Constructor of an AVTransmit object specifying a Type of Service of the flow.
	 * 
	 * @param locator
	 * @param ipAddress
	 * @param portBase
	 * @param tos
	 * @param format
	 */
    public AVTransmit(MediaLocator locator, String ipAddress, int portBase, int tos, Format format) {
        this.locator = locator;
        this.ipAddress = ipAddress;
        this.tos = tos;
        this.portBase = portBase;
    }

    /**
	 * Constructor of an AVTransmit object.
	 * 
	 * @param locator
	 * @param ipAddress
	 * @param portBase
	 * @param tos
	 * @param format
	 */
    public AVTransmit(MediaLocator locator, String ipAddress, int portBase, Format format) {
        this.locator = locator;
        this.ipAddress = ipAddress;
        this.portBase = portBase;
        this.tos = -1;
    }

    /**
	 * Starts the transmission. Returns null if transmission started ok.
	 * Otherwise it returns a string with the reason why the setup failed.
	 */
    public synchronized String start() {
        String result;
        result = createProcessor();
        if (result != null) return result;
        result = createTransmitter();
        if (result != null) {
            processor.close();
            processor = null;
            return result;
        }
        processor.start();
        enabled = true;
        return null;
    }

    /**
	 * Stops the transmission if already started
	 */
    public void stop() {
        synchronized (this) {
            if (processor != null) {
                processor.stop();
                processor.close();
                processor = null;
                for (int i = 0; i < rtpMgrs.length; i++) {
                    rtpMgrs[i].removeTargets("Session ended.");
                    rtpMgrs[i].dispose();
                    System.out.println("RTPManager " + i + " disposed.");
                }
            }
            enabled = false;
        }
    }

    private String createProcessor() {
        if (locator == null) return "Locator is null";
        DataSource ds;
        DataSource clone;
        try {
            ds = javax.media.Manager.createDataSource(locator);
        } catch (Exception e) {
            return "Couldn't create DataSource";
        }
        try {
            processor = javax.media.Manager.createProcessor(ds);
        } catch (NoProcessorException npe) {
            return "Couldn't create processor";
        } catch (IOException ioe) {
            return "IOException creating processor";
        }
        boolean result = waitForState(processor, Processor.Configured);
        if (result == false) return "Couldn't configure processor";
        TrackControl[] tracks = processor.getTrackControls();
        System.out.println("Number of tracks in processor: " + tracks.length);
        if (tracks == null || tracks.length < 1) return "Couldn't find tracks in processor";
        ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
        processor.setContentDescriptor(cd);
        Format supported[];
        Format chosen;
        boolean atLeastOneTrack = false;
        for (int i = 0; i < tracks.length; i++) {
            Format format = tracks[i].getFormat();
            if (tracks[i].isEnabled()) {
                System.out.println("Track " + i + " is enabled.");
                supported = tracks[i].getSupportedFormats();
                System.out.println("Format of track " + i + " =" + format.toString());
                if (supported.length > 0) {
                    System.out.println("Supported.length > 0");
                    if (supported[0] instanceof VideoFormat) {
                        chosen = checkForVideoSizes(tracks[i].getFormat(), supported[0]);
                    } else chosen = supported[0];
                    tracks[i].setFormat(chosen);
                    System.err.println("Track " + i + " is set to transmit as:");
                    System.err.println("  " + chosen);
                    atLeastOneTrack = true;
                } else tracks[i].setEnabled(false);
            } else tracks[i].setEnabled(false);
        }
        if (!atLeastOneTrack) return "Couldn't set any of the tracks to a valid RTP format";
        result = waitForState(processor, Controller.Realized);
        if (result == false) return "Couldn't realize processor";
        setJPEGQuality(processor, 0.5f);
        dataOutput = processor.getDataOutput();
        return null;
    }

    /**
	 * Use the RTPManager API to create sessions for each media 
	 * track of the processor.
	 */
    private String createTransmitter() {
        PushBufferDataSource pbds = (PushBufferDataSource) dataOutput;
        PushBufferStream pbss[] = pbds.getStreams();
        rtpMgrs = new RTPManager[pbss.length];
        SendStream sendStream;
        int port;
        SourceDescription srcDesList[];
        for (int i = 0; i < pbss.length; i++) {
            try {
                rtpMgrs[i] = RTPManager.newInstance();
                port = portBase + 2 * i;
                if (tos >= 0) {
                    rtpMgrs[i].initialize(new RTPSocketAdapter(InetAddress.getByName(ipAddress), port, tos));
                } else {
                    rtpMgrs[i].initialize(new RTPSocketAdapter(InetAddress.getByName(ipAddress), port));
                }
                System.err.println("Created RTP session: " + ipAddress + " " + port);
                sendStream = rtpMgrs[i].createSendStream(dataOutput, i);
                rtpMgrs[i].addReceiveStreamListener(this);
                sendStream.start();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return null;
    }

    /**
	 * For JPEG and H263, we know that they only work for particular
	 * sizes.  So we'll perform extra checking here to make sure they
	 * are of the right sizes.
	 */
    private Format checkForVideoSizes(Format original, Format supported) {
        int width, height;
        Dimension size = ((VideoFormat) original).getSize();
        Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
        Format h263Fmt = new Format(VideoFormat.H263_RTP);
        if (supported.matches(jpegFmt)) {
            width = (size.width % 8 == 0 ? size.width : (int) (size.width / 8) * 8);
            height = (size.height % 8 == 0 ? size.height : (int) (size.height / 8) * 8);
        } else if (supported.matches(h263Fmt)) {
            if (size.width < 128) {
                width = 128;
                height = 96;
            } else if (size.width < 176) {
                width = 176;
                height = 144;
            } else {
                width = 352;
                height = 288;
            }
        } else {
            return supported;
        }
        return (new VideoFormat(null, new Dimension(width, height), Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED)).intersects(supported);
    }

    /**
	 * Setting the encoding quality to the specified value on the JPEG encoder.
	 * 0.5 is a good default.
	 */
    private void setJPEGQuality(Player p, float val) {
        Control cs[] = p.getControls();
        QualityControl qc = null;
        VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
                Object owner = ((Owned) cs[i]).getOwner();
                if (owner instanceof Codec) {
                    Format fmts[] = ((Codec) owner).getSupportedOutputFormats(null);
                    for (int j = 0; j < fmts.length; j++) {
                        if (fmts[j].matches(jpegFmt)) {
                            qc = (QualityControl) cs[i];
                            qc.setQuality(val);
                            System.err.println("- Setting quality to " + val + " on " + qc);
                            break;
                        }
                    }
                }
                if (qc != null) break;
            }
        }
    }

    /****************************************************************
	 * Convenience methods to handle processor's state changes.
	 ****************************************************************/
    private Integer stateLock = new Integer(0);

    private boolean failed = false;

    private Integer getStateLock() {
        return stateLock;
    }

    private void setFailed() {
        failed = true;
    }

    private synchronized boolean waitForState(Processor p, int state) {
        p.addControllerListener(this);
        failed = false;
        if (state == Processor.Configured) {
            p.configure();
        } else if (state == Processor.Realized) {
            p.realize();
        }
        while (p.getState() < state && !failed) {
            synchronized (getStateLock()) {
                try {
                    getStateLock().wait();
                } catch (InterruptedException ie) {
                    return false;
                }
            }
        }
        if (failed) return false; else return true;
    }

    public void controllerUpdate(ControllerEvent ce) {
        if (ce instanceof EndOfMediaEvent) {
            System.out.println("EndOfMediaEvent received");
            this.stop();
        } else if (ce instanceof ControllerClosedEvent) setFailed(); else if (ce instanceof ControllerEvent) {
            synchronized (getStateLock()) {
                getStateLock().notifyAll();
            }
        } else {
            System.out.println("Event received: " + ce.toString());
        }
    }

    public synchronized void update(ReceiveStreamEvent evt) {
        if (evt instanceof ByeEvent) {
            System.out.println("ByeEvent received from " + ((ByeEvent) evt).getParticipant().getCNAME() + ". Stopping transmission...");
            this.stop();
            System.out.println("Transmission stopped.");
        }
    }

    public static void main(String[] args) {
        transmitExample(args);
    }

    private static void prUsage() {
        System.err.println("Usage: AVTransmit3 <sourceURL> <destIP> <destPortBase>");
        System.err.println("     <sourceURL>: input URL or file name");
        System.err.println("     <destIP>: multicast, broadcast or unicast IP address for the transmission");
        System.err.println("     <destPortBase>: network port numbers for the transmission.");
        System.err.println("                     The first track will use the destPortBase.");
        System.err.println("                     The next track will use destPortBase + 2 and so on.\n");
        System.err.println("     <typeOfService [OPTIONAL]>: Type of Service for the data RTP connection flow.");
        System.exit(0);
    }

    private static void transmitExample(String[] args) {
        AVTransmit at = null;
        if (args.length < 3) {
            prUsage();
            return;
        } else if (args.length <= 4) {
            Format format = null;
            int portBase;
            Integer integerPortBase = Integer.valueOf(args[2]);
            if (integerPortBase != null) {
                portBase = integerPortBase.intValue();
            } else {
                prUsage();
                return;
            }
            String ipAddress = args[1];
            if (args.length == 4) {
                int tos;
                Integer integerTos = Integer.valueOf(args[3]);
                if (integerTos != null) {
                    tos = integerTos.intValue();
                } else {
                    prUsage();
                    return;
                }
                at = new AVTransmit(new MediaLocator(args[0]), ipAddress, portBase, tos, format);
            } else {
                at = new AVTransmit(new MediaLocator(args[0]), ipAddress, portBase, format);
            }
        } else {
            prUsage();
            return;
        }
        String result = at.start();
        if (result != null) {
            System.err.println("Error : " + result);
            System.exit(0);
        }
        System.err.println("Start transmission...");
    }

    public boolean isEnabled() {
        return enabled;
    }
}
