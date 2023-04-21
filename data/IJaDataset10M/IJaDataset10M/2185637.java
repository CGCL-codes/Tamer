package org.rjam.report.xml.xlsfo;

import org.rjam.xml.Token;

public class FoRegionStart extends Token implements FoConstants {

    private static final long serialVersionUID = 1L;

    public FoRegionStart() {
        super(FO_REGION_START);
    }

    public FoRegionStart(Token child) {
        super(FO_REGION_START, child);
    }

    /**
	* @param val examle : 1cm
	*/
    public void setExtent(String val) {
        setAttribute(EXTENT, val);
    }
}
