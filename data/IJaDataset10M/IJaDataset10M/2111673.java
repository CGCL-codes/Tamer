package com.prowidesoftware.swift.model.mt.mt9xx;

import java.io.Serializable;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * MT 999<br /><br />
 *
 *		 
 * <em>NOTE: this source code has been generated from template</em>
 *
 * @author www.prowidesoftware.com
 */
public class MT999 extends AbstractMT implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient java.util.logging.Logger log = java.util.logging.Logger.getLogger(MT999.class.getName());

    /**
	 * @param m swift message to model as a particular MT
	 */
    public MT999(SwiftMessage m) {
        super(m);
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 20, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 20 at MT999 is expected to be the only one.
	 * 
	 * @return a Field20 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field20 getField20() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("20");
            if (t == null) {
                log.fine("field 20 not found");
                return null;
            } else {
                return new Field20(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 21, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 21 at MT999 is expected to be the only one.
	 * 
	 * @return a Field21 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field21 getField21() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("21");
            if (t == null) {
                log.fine("field 21 not found");
                return null;
            } else {
                return new Field21(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 79, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 79 at MT999 is expected to be the only one.
	 * 
	 * @return a Field79 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field79 getField79() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("79");
            if (t == null) {
                log.fine("field 79 not found");
                return null;
            } else {
                return new Field79(t.getValue());
            }
        }
    }
}
