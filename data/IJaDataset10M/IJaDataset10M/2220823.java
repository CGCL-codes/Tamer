package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.impl.orbutil.ORBConstants;

public class CDRInputStream_1_2 extends CDRInputStream_1_1 {

    protected boolean headerPadding;

    protected boolean restoreHeaderPadding;

    void setHeaderPadding(boolean headerPadding) {
        this.headerPadding = headerPadding;
    }

    public void mark(int readlimit) {
        super.mark(readlimit);
        restoreHeaderPadding = headerPadding;
    }

    public void reset() {
        super.reset();
        headerPadding = restoreHeaderPadding;
        restoreHeaderPadding = false;
    }

    public CDRInputStreamBase dup() {
        CDRInputStreamBase result = super.dup();
        ((CDRInputStream_1_2) result).headerPadding = this.headerPadding;
        return result;
    }

    protected void alignAndCheck(int align, int n) {
        if (headerPadding == true) {
            headerPadding = false;
            alignOnBoundary(ORBConstants.GIOP_12_MSG_BODY_ALIGNMENT);
        }
        checkBlockLength(align, n);
        int alignIncr = computeAlignment(bbwi.position(), align);
        bbwi.position(bbwi.position() + alignIncr);
        if (bbwi.position() + n > bbwi.buflen) {
            grow(1, n);
        }
    }

    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_2;
    }

    public char read_wchar() {
        int numBytes = read_octet();
        char[] result = getConvertedChars(numBytes, getWCharConverter());
        if (getWCharConverter().getNumChars() > 1) throw wrapper.btcResultMoreThanOneChar();
        return result[0];
    }

    public String read_wstring() {
        int len = read_long();
        if (len == 0) return new String("");
        checkForNegativeLength(len);
        return new String(getConvertedChars(len, getWCharConverter()), 0, getWCharConverter().getNumChars());
    }
}
