package org.jsynthlib.synthdrivers.TCElectronicGMajor;

import java.util.prefs.Preferences;
import org.jsynthlib.core.Device;

public class TCElectronicGMajorDevice extends Device {

    private static final String INFO_TEXT = "The librarian and editor are for the TC Electronic G-Major version 1.27 firmware.\n" + "At the moment there is no fader support.\n" + "It is fully functional, except for the bank driver. I have trouble getting the timing right.\n" + "Not all patches in the G-Major are overwritten, when you do a bank dump.\n\n" + "Have fun!\n" + "Ton Holsink\n" + "a.j.m.holsink@chello.nl";

    /** Constructor for DeviceListWriter. */
    public TCElectronicGMajorDevice() {
        super("TC Electronic", "G-Major", "F07E00060200201F480000000000010BF7", INFO_TEXT, "Ton Holsink");
    }

    /** Constructor for for actual work. */
    public TCElectronicGMajorDevice(final Preferences prefs) {
        this();
        this.prefs = prefs;
        TCElectronicGMajorSingleDriver singleDriver = new TCElectronicGMajorSingleDriver(this);
        addDriver(singleDriver);
        addDriver(new TCElectronicGMajorBankDriver(singleDriver));
    }
}
