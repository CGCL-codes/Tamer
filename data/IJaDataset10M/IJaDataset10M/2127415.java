package org.jsynthlib.synthdrivers.EnsoniqVFX;

import java.util.prefs.Preferences;
import org.jsynthlib.core.Device;

/**
 * Ensoniq VFX device. Copied from the ESQ1.
 * My VFX version seems to be 2.10 (02 0A in the inquiry result ID message).
 * Unfortunately, my documentation is not up to date so if this driver does not work
 * with yours, check the sysex size first and then the message types.
 * I can't deal with several version of VFX because I have only one. This could be
 * made with first getting the version and then choosing the good parameters in a
 * table with the version as the key.
 *
 * @author     <a href="mailto:dqueffeulou@free.fr">Denis Queffeulou</a>  (created 17 Sep 2002)
 */
public class EnsoniqVFXDevice extends Device {

    private static final String infoText = "JSynthLib supports librarian functions on VFX single/bank/multi patches.\n" + "This driver has been tested with VFX 2.10 version,  " + "older versions could not work if different sysex length are used.\n" + "The patch store send the patch in the edit buffer.";

    /**
         *  Creates new EnsoniqVFXDevice
         */
    public EnsoniqVFXDevice() {
        super("Ensoniq", "VFX", "F07E..06020F05000000000002..F7", infoText, "Denis Queffeulou");
    }

    /** Constructor for for actual work. */
    public EnsoniqVFXDevice(final Preferences prefs) {
        this();
        this.prefs = prefs;
        addDriver(new EnsoniqVFXBankDriver(this));
        addDriver(new EnsoniqVFXSingleDriver(this));
        addDriver(new EnsoniqVFXMultiDriver(this));
    }
}
