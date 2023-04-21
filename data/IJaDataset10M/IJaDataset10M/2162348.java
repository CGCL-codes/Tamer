package org.xmlcml.cml.legacy2cml.molecule.chemdraw;

import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Node;

/**
 * 
 * @author pm286
 *
 */
public class CDXML extends CDXObject {

    static Logger logger = Logger.getLogger(CDXML.class.getName());

    static Level MYFINE = Level.FINE;

    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    ;

    static int CODE = 0x8000;

    static String NAME = "CDXML";

    static String CDXNAME = "CDXML";

    protected CodeName setCodeName() {
        codeName = new CodeName(CODE, NAME, CDXNAME);
        return codeName;
    }

    ;

    protected CDXML() {
        super(CODE, NAME, CDXNAME);
        setCodeName();
    }

    /**
     * copy node .
     * @return Node
     */
    public Node copy() {
        return new CDXML(this);
    }

    /**
     * copy constructor
     * @param old
     */
    public CDXML(CDXML old) {
        super(old);
    }
}

;
