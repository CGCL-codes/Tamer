package org.rjam.report.xml.xlsfo;

import org.rjam.xml.Token;

public class FoPageNumberCitation extends Token implements FoConstants {

    private static final long serialVersionUID = 1L;

    public FoPageNumberCitation() {
        super(FO_PAGE_NUMBER_CITATION);
    }

    public FoPageNumberCitation(Token child) {
        super(FO_PAGE_NUMBER_CITATION, child);
    }

    /**
	* @param val examle : end-of-document
	*/
    public void setRefId(String val) {
        setAttribute(REF_ID, val);
    }
}
