package fr.esrf.tangoatk.core;

import fr.esrf.Tango.*;

public class DeviceException extends ATKException {

    public DeviceException(DevFailed e) {
        super(e);
    }

    public DeviceException(String s) {
        super(s);
    }

    public String getVersion() {
        return "$Id: DeviceException.java 12968 2009-01-26 17:54:56Z poncet $";
    }
}
