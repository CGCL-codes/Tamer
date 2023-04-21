package org.rjam.report.xml.xlsfo;

import org.rjam.xml.Token;

public class FoListItemLabel extends Token implements FoConstants {

    private static final long serialVersionUID = 1L;

    public FoListItemLabel() {
        super(FO_LIST_ITEM_LABEL);
    }

    public FoListItemLabel(Token child) {
        super(FO_LIST_ITEM_LABEL, child);
    }

    /**
	* @param val examle : label-end()
	*/
    public void setEndIndent(String val) {
        setAttribute(END_INDENT, val);
    }
}
