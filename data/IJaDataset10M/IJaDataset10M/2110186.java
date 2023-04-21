package com.prowidesoftware.swift.model.mt.mt4xx;

import java.io.Serializable;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * MT 491<br /><br />
 *
 *		 
 * <em>NOTE: this source code has been generated from template</em>
 *
 * @author www.prowidesoftware.com
 */
public class MT491 extends AbstractMT implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient java.util.logging.Logger log = java.util.logging.Logger.getLogger(MT491.class.getName());

    /**
	 * @param m swift message to model as a particular MT
	 */
    public MT491(SwiftMessage m) {
        super(m);
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 20, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 20 at MT491 is expected to be the only one.
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
	 * The first occurrence of field 21 at MT491 is expected to be the only one.
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
	 * Iterates through block4 fields and return the first one whose name matches 32B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 32B at MT491 is expected to be the only one.
	 * 
	 * @return a Field32B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field32B getField32B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("32B");
            if (t == null) {
                log.fine("field 32B not found");
                return null;
            } else {
                return new Field32B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 52A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 52A at MT491 is expected to be the only one.
	 * 
	 * @return a Field52A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field52A getField52A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("52A");
            if (t == null) {
                log.fine("field 52A not found");
                return null;
            } else {
                return new Field52A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 52D, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 52D at MT491 is expected to be the only one.
	 * 
	 * @return a Field52D object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field52D getField52D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("52D");
            if (t == null) {
                log.fine("field 52D not found");
                return null;
            } else {
                return new Field52D(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 57A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 57A at MT491 is expected to be the only one.
	 * 
	 * @return a Field57A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field57A getField57A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("57A");
            if (t == null) {
                log.fine("field 57A not found");
                return null;
            } else {
                return new Field57A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 57B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 57B at MT491 is expected to be the only one.
	 * 
	 * @return a Field57B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field57B getField57B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("57B");
            if (t == null) {
                log.fine("field 57B not found");
                return null;
            } else {
                return new Field57B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 57D, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 57D at MT491 is expected to be the only one.
	 * 
	 * @return a Field57D object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field57D getField57D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("57D");
            if (t == null) {
                log.fine("field 57D not found");
                return null;
            } else {
                return new Field57D(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 71B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 71B at MT491 is expected to be the only one.
	 * 
	 * @return a Field71B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field71B getField71B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("71B");
            if (t == null) {
                log.fine("field 71B not found");
                return null;
            } else {
                return new Field71B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 72, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 72 at MT491 is expected to be the only one.
	 * 
	 * @return a Field72 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field72 getField72() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("72");
            if (t == null) {
                log.fine("field 72 not found");
                return null;
            } else {
                return new Field72(t.getValue());
            }
        }
    }
}
