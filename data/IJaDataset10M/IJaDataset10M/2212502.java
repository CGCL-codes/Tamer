package org.hl7.types.impl;

import org.hl7.types.CS;
import org.hl7.types.DSET;
import org.hl7.types.PNXP;
import org.hl7.types.enums.EntityNamePartType;

public class PNXPimpl extends ENXPimpl implements PNXP {

    private PNXPimpl(String data, EntityNamePartType type, DSET<CS> qualifier) {
        super(data, type, qualifier);
    }

    public static PNXP valueOf(String data) {
        return valueOf(data, null);
    }

    public static PNXP valueOf(String data, EntityNamePartType type) {
        return valueOf(data, type, null);
    }

    public static PNXP valueOf(String data, EntityNamePartType type, DSET<CS> qualifier) {
        if (data == null) return PNXPnull.NI; else return new PNXPimpl(data.trim(), type, qualifier);
    }
}
