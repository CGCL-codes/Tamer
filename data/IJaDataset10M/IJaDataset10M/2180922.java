package rsdowloader.gui.renderer;

import java.awt.Color;
import rsdowloader.RSLink;

/**
 *
 * @author Michal Blinkiewicz
 */
public class Utils {

    static Color getColorFromStatus(int status) {
        switch(status) {
            case RSLink.STATUS_NOTHING:
                return new Color(0xff918c);
            case RSLink.STATUS_WAITING:
                return new Color(0xffdf8c);
            case RSLink.STATUS_DOWNLOADING:
                return new Color(0xacff8c);
            case RSLink.STATUS_DONE:
                return new Color(0x8ccdff);
            default:
                return Color.lightGray;
        }
    }

    static String getSizeStringFromSize(long size) {
        if (size > 0) {
            if ((size / 1024) > 0) {
                if ((size / 1024 / 1024) > 0) {
                    return "" + (size / 1024 / 1024) + " MiB";
                } else {
                    return "" + (size / 1024) + " KiB";
                }
            } else {
                return "" + size + " B";
            }
        } else {
            return "not known";
        }
    }

    static String getTimeString(RSLink link) {
        switch(link.getStatus()) {
            case RSLink.STATUS_NOTHING:
                return "-";
            case RSLink.STATUS_WAITING:
                return "" + ((link.getWait() > 0) ? link.getWait() : "~") + " s";
            case RSLink.STATUS_DOWNLOADING:
                return getSizeStringFromSize(link.getDown());
            case RSLink.STATUS_DONE:
                return "-";
            default:
                return "???";
        }
    }

    static String getPriotityString(int priority) {
        switch(priority) {
            case RSLink.PRIORITY_DONOT_DOWNLOAD:
                return "Do not download";
            case RSLink.PRIORITY_NORMAL:
                return "Normal";
            case RSLink.PRIORITY_HIGH:
                return "High";
            case RSLink.PRIORITY_LOW:
                return "Low";
            default:
                return "???";
        }
    }

    static int getProgresFromSizeAndDown(long size, long down) {
        if (size > 0) {
            return (int) (100L * down / size);
        } else {
            return 0;
        }
    }
}
