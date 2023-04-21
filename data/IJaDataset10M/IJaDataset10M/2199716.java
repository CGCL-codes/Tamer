package br.ufal.tci.nexos.arcolive.service.conference;

import java.awt.Dimension;
import javax.media.Codec;
import javax.media.Control;
import javax.media.Controller;
import javax.media.Format;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.QualityControl;
import javax.media.format.VideoFormat;

/**
 * CLASSNAME.java
 *
 * CLASS DESCRIPTION
 *
 * @see CLASSNAME
 *
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 *
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class ConferenceUtil {

    /**
     * For JPEG and H263, we know that they only work for particular
     * sizes.  So we'll perform extra checking here to make sure they
     * are of the right sizes.
     */
    public static Format checkForVideoSizes(Format original, Format supported) {
        int width, height;
        Dimension size = ((VideoFormat) original).getSize();
        Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
        Format h263Fmt = new Format(VideoFormat.H263_RTP);
        if (supported.matches(jpegFmt)) {
            width = (size.width % 8 == 0 ? size.width : (size.width / 8) * 8);
            height = (size.height % 8 == 0 ? size.height : (size.height / 8) * 8);
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
    public static void setJPEGQuality(Player p, float val) {
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
    private static Integer stateLock = new Integer(0);

    private static boolean failed = false;

    public static Integer getStateLock() {
        return stateLock;
    }

    public static void setFailed() {
        failed = true;
    }

    public static synchronized boolean waitForState(Processor p, int state) {
        p.addControllerListener(new ProcessorStateListener());
        failed = false;
        if (state == Processor.Configured) {
            p.configure();
        } else if (state == Controller.Realized) {
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
        return !failed;
    }
}
