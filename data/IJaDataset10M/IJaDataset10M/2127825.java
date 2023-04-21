package synthdrivers.YamahaCS2x;

import core.*;

/**
 * Driver to send/request all 4 layers of a performance
 * @author Peter Geirnaert
 */
public class YamahaCS2xLayersDriver extends Driver {

    static final SysexHandler SYS_REQ = new SysexHandler("F0 43 20 63 *banknum* *patchnum* 00 F7");

    private static YamahaCS2xLayersDriver mInstance;

    public YamahaCS2xLayersDriver() {
        super("Layers", "Peter Geirnaert");
        sysexID = "F0430*63002D****00";
        deviceIDoffset = 2;
        bankNumbers = new String[] { "Current Performance", "Bank1", "Bank2" };
        patchNumbers = DriverUtil.generateNumbers(1, 128, "Layers ##");
        patchSize = 56 * 4;
        patchNameSize = 0;
    }

    static YamahaCS2xLayersDriver getInstance() {
        if (mInstance == null) {
            mInstance = new YamahaCS2xLayersDriver();
        }
        return mInstance;
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        requestPatchDump(bankNum, patchNum, getDeviceID());
    }

    public void requestPatchDump(int bankNum, int patchNum, int deviceID) {
        int i;
        int Bn;
        int Pn;
        switch(bankNum) {
            case 0:
                Bn = 0x60;
                Pn = 0x00;
                for (i = 1; i < 5; i++) {
                    final SysexHandler.NameValue[] nameValues = { new SysexHandler.NameValue("patchnum", Pn + i), new SysexHandler.NameValue("banknum", Bn) };
                    send(SYS_REQ.toSysexMessage(deviceID, nameValues));
                    CS2x.sleep();
                }
                break;
            case 1:
                Bn = 0x71;
                for (i = 0; i < 4; i++) {
                    final SysexHandler.NameValue[] nameValues = { new SysexHandler.NameValue("patchnum", patchNum), new SysexHandler.NameValue("banknum", Bn + i) };
                    send(SYS_REQ.toSysexMessage(deviceID, nameValues));
                    CS2x.sleep();
                }
                break;
            case 2:
                Bn = 0x79;
                for (i = 0; i < 4; i++) {
                    final SysexHandler.NameValue[] nameValues = { new SysexHandler.NameValue("patchnum", patchNum), new SysexHandler.NameValue("banknum", Bn + i) };
                    send(SYS_REQ.toSysexMessage(deviceID, nameValues));
                    CS2x.sleep();
                }
                break;
        }
    }

    protected void calculateChecksum(Patch p) {
        calculateChecksum(p, 4, 53, 54);
        calculateChecksum(p, 60, 109, 110);
        calculateChecksum(p, 116, 165, 166);
        calculateChecksum(p, 172, 221, 222);
    }

    byte[] cpLayer = new byte[56];

    public void sendPatch(Patch p) {
        int i;
        for (i = 0; i < 4; i++) {
            System.arraycopy(p.sysex, (i * 56), cpLayer, 0, 56);
            cpLayer[6] = (byte) 0x60;
            cpLayer[7] = (byte) (i + 1);
            DriverUtil.calculateChecksum(cpLayer, 4, 53, 54);
            send(cpLayer);
            CS2x.sleep();
        }
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        int i;
        switch(bankNum) {
            case 0:
                sendPatch(p);
                break;
            case 1:
                for (i = 0; i < 4; i++) {
                    System.arraycopy(p.sysex, (i * 56), cpLayer, 0, 56);
                    cpLayer[6] = (byte) (0x71 + i);
                    cpLayer[7] = (byte) patchNum;
                    DriverUtil.calculateChecksum(cpLayer, 4, 53, 54);
                    send(cpLayer);
                    CS2x.sleep();
                }
                break;
            case 2:
                for (i = 0; i < 4; i++) {
                    System.arraycopy(p.sysex, (i * 56), cpLayer, 0, 56);
                    cpLayer[6] = (byte) (0x78 + i);
                    cpLayer[7] = (byte) patchNum;
                    DriverUtil.calculateChecksum(cpLayer, 4, 53, 54);
                    send(cpLayer);
                    CS2x.sleep();
                }
                break;
        }
    }

    /**
     * Gets the name of the patch from the sysex. If the patch uses
     * some weird format or encoding, this needs to be overidden in
     * the particular driver.
     * @see Patch#getName()
     */
    protected String getPatchName(Patch p) {
        int bnk = 0;
        int perf = 500;
        boolean bank;
        switch(p.sysex[6]) {
            case (byte) 0x60:
                bnk = 0;
                break;
            case (byte) 0x71:
                bnk = 1;
                perf = p.sysex[7];
                break;
            case (byte) 0x79:
                bnk = 2;
                perf = p.sysex[7];
                break;
        }
        if (perf < 499) {
            return patchNumbers[perf] + " of " + bankNumbers[bnk];
        } else {
            return bankNumbers[bnk];
        }
    }
}
