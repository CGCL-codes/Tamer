package org.jsynthlib.synthdrivers.RolandJD800;

import org.jsynthlib.core.Device;
import org.jsynthlib.core.Driver;
import org.jsynthlib.core.Patch;
import org.jsynthlib.core.SysexHandler;

/**
 * System Area Driver for Roland JD800. This area contains information about
 * master tune, Mix out filter, Effect B master switch, Delay, Chorus and Reverb.
 * It uses standard methods defined in <code>core.Driver</code> package.
 * @see core.Driver
 */
public class RolandJD800SystemArea extends Driver {

    private static final SysexHandler SYS_REQ = new SysexHandler("F0 41 @@ 3D 11 02 00 00 00 00 19 00 F7");

    public RolandJD800SystemArea(final Device device) {
        super(device, "System Area", "Robert Wirski");
        sysexID = "F041**3D12020000";
        deviceIDoffset = 2;
        patchNameStart = 0;
        patchNameSize = 0;
        patchSize = 35;
        checksumOffset = 33;
        checksumStart = 5;
        checksumEnd = 32;
        bankNumbers = new String[] { "" };
        patchNumbers = new String[] { "" };
    }

    /**
    * Creates new Patch based on a sysex message obtained
    * from factory presets of JD800.
    */
    public Patch createNewPatch() {
        SysexHandler init = new SysexHandler("F0 41 10 3D 12 02 00 00  32 05 05 05 01 01 01 6E" + "00 6E 28 7D 28 28 23 0F  00 31 64 04 00 08 0D 19" + "28 48 F7");
        return new Patch(init.toByteArray(), this);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYS_REQ.toSysexMessage(getDeviceID()));
    }
}
