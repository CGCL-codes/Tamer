package net.sf.smartcrib.dmx;

import java.util.Comparator;

/**
 * A DMX devices comparator based on their names.
 */
public class DMXDeviceNameComparator implements Comparator<DMXDevice> {

    public int compare(DMXDevice o1, DMXDevice o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
