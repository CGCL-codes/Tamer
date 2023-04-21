package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.expr.GCLOps;

public final class NullTable extends HeldTable {

    private final CommonColumnHdr[] hdr = { new NullHdr() };

    private static final NullTable singleton = new NullTable();

    private NullTable() {
    }

    public static final NullTable theNull() {
        return singleton;
    }

    protected CommonColumnHdr[] getColumnHdrs() {
        return hdr;
    }

    protected String doPath(PathStrategy strategy) throws AccessException {
        return GCLOps.NULL_OP;
    }

    protected int doRowCount() {
        return 1;
    }

    private class NullHdr extends CommonColumnHdr {

        public String getName() {
            return "null";
        }

        protected void doSetName(String name) {
        }

        public CommonType getEnclosingCommonType() {
            return NullTable.this;
        }

        public CommonType commonType() {
            return CommonType.XANY;
        }

        protected String readTypedString(CommonScope scope) throws AccessException {
            return null;
        }

        protected Object readTypedAny(CommonScope scope) throws AccessException {
            return null;
        }
    }
}
