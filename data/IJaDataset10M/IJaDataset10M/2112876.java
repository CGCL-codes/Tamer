package org.az.hhp.params;

import org.az.hhp.domain.Claim;

public class ProcedureAtDSFSParam implements ClaimParameter {

    private final ProcedureGroupParam pg = new ProcedureGroupParam();

    private final DSFSParam dsfs = new DSFSParam();

    @Override
    public String valueOf(final Claim c) {
        return pg.valueOf(c) + "@" + dsfs.valueOf(c);
    }
}
