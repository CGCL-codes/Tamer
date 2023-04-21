package synthdrivers.BehringerFCB1010;

import core.*;

/** Behringer FCB1010 Driver.
*
* @author Jeff Weber
*/
class FCB1010Driver extends Driver {

    /** FCB1010 Dump Request
    */
    private static final SysexHandler SYS_REQ = new SysexHandler(Constants.FCB1010_DUMP_REQ_ID);

    /** Constructs a FCB1010Driver.
        */
    public FCB1010Driver() {
        super(Constants.FCB1010_PATCH_TYP_STR, Constants.AUTHOR);
        sysexID = Constants.FCB1010_SYSEX_MATCH_ID;
        patchSize = Constants.HDR_SIZE + Constants.FCB1010_NATIVE_SIZE + 1;
        deviceIDoffset = Constants.DEVICE_ID_OFFSET;
        bankNumbers = Constants.FCB1010_BANK_LIST;
        patchNumbers = Constants.FCB1010_PATCH_LIST;
    }

    /** Send Program Change MIDI message. The FCB1010 driver does
        * not utilize program change messages. This method is overriden with a
        * null method.
        */
    protected void setPatchNum(int patchNum) {
    }

    /** Send Control Change (Bank Select) MIDI message. The FCB1010 
        * driver does not utilize bank select. This method is overriden with a
        * null method.
        */
    protected void setBankNum(int bankNum) {
    }

    /** FCB1010Driver patch does not utilize checksum. Method overridded with
        * null method.
        */
    protected void calculateChecksum(Patch p) {
    }

    /** FCB1010Driver patch does not utilize checksum. Method overridded with
        * null method.
        */
    protected void calculateChecksum(Patch patch, int start, int end, int offset) {
    }

    /** Requests a dump of the FCB1010 patch.
        * This patch does not utilize bank select or program changes. 
        */
    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("channel", getChannel())));
    }

    /** Creates a new patch with default values.
        */
    protected Patch createNewPatch() {
        Patch p = new Patch(Constants.NEW_SYSEX, this);
        return p;
    }

    /** Opens an edit window on the specified patch.
        */
    protected JSLFrame editPatch(Patch p) {
        return new FCB1010Editor((Patch) p);
    }
}
