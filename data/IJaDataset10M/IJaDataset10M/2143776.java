package org.zoolib;

import java.util.Map;

public class ZTBTxn {

    public ZTBTxn(ZTxn iTxn, ZTB iTB) {
        fTxn = iTxn;
        fTB = iTB;
    }

    public ZTBTransRep getTransRep() {
        return fTB.getTransRep(fTxn);
    }

    public ZID allocateZID() {
        return fTB.allocateZID();
    }

    public ZID add(Map iMap) {
        return fTB.add(fTxn, iMap);
    }

    public void set(ZID iID, Map iMap) {
        fTB.set(fTxn, iID, iMap);
    }

    public ZTuple get(ZID iID) {
        return fTB.get(fTxn, iID);
    }

    public final long count(ZTBQuery iQuery) {
        return fTB.count(fTxn, iQuery);
    }

    public final ZID[] search(ZTBQuery iQuery) {
        return fTB.search(fTxn, iQuery);
    }

    private final ZTxn fTxn;

    private final ZTB fTB;
}
