package org.jsynthlib.synthdrivers.YamahaTG100;

import java.util.prefs.Preferences;
import org.jsynthlib.core.Device;

/**
 *
 * @author Joachim Backhaus
 */
public class YamahaTG100Device extends Device {

    private static final String INFO_TEXT = "Librarian & editor for the Yamaha TG-100.";

    /**
     * Constructor for DeviceListWriter.
     */
    public YamahaTG100Device() {
        super("Yamaha", "TG100", "F07E..06020001050000040001002100F7", INFO_TEXT, "Joachim Backhaus");
    }

    /**
     * Constructor for the actual work.
     *
     * @param prefs The Preferences for this device
     */
    public YamahaTG100Device(final Preferences prefs) {
        this();
        this.prefs = prefs;
        addDriver(new YamahaTG100AllConverter(this));
        addDriver(new YamahaTG100SingleDriver(this));
        addDriver(new YamahaTG100BankDriver(this));
    }
}
