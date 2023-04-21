package eibstack.transceiver;

import eibstack.Result;

public class Transmission {

    public byte[] frame;

    public boolean waitConfirm;

    public int result;

    public Transmission(byte[] lPDU, boolean waitConfirm) {
        frame = lPDU;
        this.waitConfirm = waitConfirm;
        result = Result.OK;
    }
}
