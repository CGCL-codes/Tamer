package org.rjam.report.xml.xlsfo;

import org.rjam.xml.Token;

public class FoPageSequence extends Token implements FoConstants {

    private static final long serialVersionUID = 1L;

    public FoPageSequence() {
        super(FO_PAGE_SEQUENCE);
    }

    public FoPageSequence(Token child) {
        super(FO_PAGE_SEQUENCE, child);
    }

    /**
	* @param val examle : first
	*/
    public void setMasterReference(String val) {
        setAttribute(MASTER_REFERENCE, val);
    }

    /**
	* @param val examle : 1
	*/
    public void setInitialPageNumber(String val) {
        setAttribute(INITIAL_PAGE_NUMBER, val);
    }

    /**
	* @param val examle : N2561
	*/
    public void setId(String val) {
        setAttribute(ID, val);
    }
}
