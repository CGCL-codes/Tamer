package org.rjam.report.xml.xlsfo;

import org.rjam.xml.Token;

public class FoBasicLink extends Token implements FoConstants {

    private static final long serialVersionUID = 1L;

    public FoBasicLink() {
        super(FO_BASIC_LINK);
    }

    public FoBasicLink(Token child) {
        super(FO_BASIC_LINK, child);
    }

    /**
	* @param val examle : blue
	*/
    public void setColor(String val) {
        setAttribute(COLOR, val);
    }

    /**
	* @param val examle : http://xmlgraphics.apache.org/batik
	*/
    public void setExternalDestination(String val) {
        setAttribute(EXTERNAL_DESTINATION, val);
    }

    /**
	* @param val examle : sec11
	*/
    public void setInternalDestination(String val) {
        setAttribute(INTERNAL_DESTINATION, val);
    }

    /**
	* @param val examle : italic
	*/
    public void setFontStyle(String val) {
        setAttribute(FONT_STYLE, val);
    }

    /**
	* @param val examle : underline
	*/
    public void setTextDecoration(String val) {
        setAttribute(TEXT_DECORATION, val);
    }
}
