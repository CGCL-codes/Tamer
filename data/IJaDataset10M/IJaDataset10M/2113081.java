package com.cosylab.gui.components.numberfield;

/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 
 * @author <a href="mailto:ales.pucelj@cosylab.com">Ales Pucelj</a>
 * @version $id$
 */
public class LongDocument extends AbstractNumberDocument {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor for LongDocument.
	 */
    public LongDocument() {
        super();
        super.max = Long.MAX_VALUE;
        super.min = Long.MIN_VALUE;
    }

    /**
	 * @see AbstractNumberDocument#parseNumber(String)
	 */
    protected Number parseNumber(String s) {
        Long value = null;
        try {
            if ("-".equals(s.trim())) {
                value = null;
            } else {
                value = Long.decode(s);
            }
        } catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }
}
