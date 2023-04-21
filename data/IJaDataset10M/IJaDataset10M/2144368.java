package com.prowidesoftware.swift.model.mt.mt2xx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * MT 202COV<br /><br />
 *
 *		 
 * <em>NOTE: this source code has been generated from template</em>
 *
 * @author www.prowidesoftware.com
 */
public class MT202COV extends AbstractMT implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient java.util.logging.Logger log = java.util.logging.Logger.getLogger(MT202COV.class.getName());

    /**
	 * @param m swift message to model as a particular MT
	 */
    public MT202COV(SwiftMessage m) {
        super(m);
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 20, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 20 at MT202COV is expected to be the only one.
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
	 * The first occurrence of field 21 at MT202COV is expected to be the only one.
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
	 * Iterates through block4 fields and return the first one whose name matches 32A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 32A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field32A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field32A getField32A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("32A");
            if (t == null) {
                log.fine("field 32A not found");
                return null;
            } else {
                return new Field32A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 53A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 53A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field53A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field53A getField53A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("53A");
            if (t == null) {
                log.fine("field 53A not found");
                return null;
            } else {
                return new Field53A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 53B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 53B at MT202COV is expected to be the only one.
	 * 
	 * @return a Field53B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field53B getField53B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("53B");
            if (t == null) {
                log.fine("field 53B not found");
                return null;
            } else {
                return new Field53B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 53D, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 53D at MT202COV is expected to be the only one.
	 * 
	 * @return a Field53D object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field53D getField53D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("53D");
            if (t == null) {
                log.fine("field 53D not found");
                return null;
            } else {
                return new Field53D(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 54A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 54A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field54A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field54A getField54A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("54A");
            if (t == null) {
                log.fine("field 54A not found");
                return null;
            } else {
                return new Field54A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 54B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 54B at MT202COV is expected to be the only one.
	 * 
	 * @return a Field54B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field54B getField54B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("54B");
            if (t == null) {
                log.fine("field 54B not found");
                return null;
            } else {
                return new Field54B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 54D, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 54D at MT202COV is expected to be the only one.
	 * 
	 * @return a Field54D object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field54D getField54D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("54D");
            if (t == null) {
                log.fine("field 54D not found");
                return null;
            } else {
                return new Field54D(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 58A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 58A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field58A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field58A getField58A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("58A");
            if (t == null) {
                log.fine("field 58A not found");
                return null;
            } else {
                return new Field58A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 58D, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 58D at MT202COV is expected to be the only one.
	 * 
	 * @return a Field58D object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field58D getField58D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("58D");
            if (t == null) {
                log.fine("field 58D not found");
                return null;
            } else {
                return new Field58D(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 50A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 50A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field50A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field50A getField50A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("50A");
            if (t == null) {
                log.fine("field 50A not found");
                return null;
            } else {
                return new Field50A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 50F, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 50F at MT202COV is expected to be the only one.
	 * 
	 * @return a Field50F object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field50F getField50F() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("50F");
            if (t == null) {
                log.fine("field 50F not found");
                return null;
            } else {
                return new Field50F(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 50K, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 50K at MT202COV is expected to be the only one.
	 * 
	 * @return a Field50K object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field50K getField50K() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("50K");
            if (t == null) {
                log.fine("field 50K not found");
                return null;
            } else {
                return new Field50K(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 56C, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 56C at MT202COV is expected to be the only one.
	 * 
	 * @return a Field56C object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field56C getField56C() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("56C");
            if (t == null) {
                log.fine("field 56C not found");
                return null;
            } else {
                return new Field56C(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 57C, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 57C at MT202COV is expected to be the only one.
	 * 
	 * @return a Field57C object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field57C getField57C() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("57C");
            if (t == null) {
                log.fine("field 57C not found");
                return null;
            } else {
                return new Field57C(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 59A, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 59A at MT202COV is expected to be the only one.
	 * 
	 * @return a Field59A object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field59A getField59A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("59A");
            if (t == null) {
                log.fine("field 59A not found");
                return null;
            } else {
                return new Field59A(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 59, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 59 at MT202COV is expected to be the only one.
	 * 
	 * @return a Field59 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field59 getField59() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("59");
            if (t == null) {
                log.fine("field 59 not found");
                return null;
            } else {
                return new Field59(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 70, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 70 at MT202COV is expected to be the only one.
	 * 
	 * @return a Field70 object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field70 getField70() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("70");
            if (t == null) {
                log.fine("field 70 not found");
                return null;
            } else {
                return new Field70(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return the first one whose name matches 33B, 
	 * or <code>null</code> if none is found.<br />
	 * The first occurrence of field 33B at MT202COV is expected to be the only one.
	 * 
	 * @return a Field33B object or <code>null</code> if the field is not found
	 * @see SwiftTagListBlock#getTagByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public Field33B getField33B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return null;
        } else {
            final Tag t = getSwiftMessage().getBlock4().getTagByName("33B");
            if (t == null) {
                log.fine("field 33B not found");
                return null;
            } else {
                return new Field33B(t.getValue());
            }
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 13C, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 13C at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field13C objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field13C> getField13C() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("13C");
            final List<Field13C> result = new ArrayList<Field13C>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field13C(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 52A, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 52A at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field52A objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field52A> getField52A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("52A");
            final List<Field52A> result = new ArrayList<Field52A>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field52A(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 52D, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 52D at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field52D objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field52D> getField52D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("52D");
            final List<Field52D> result = new ArrayList<Field52D>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field52D(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 56A, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 56A at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field56A objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field56A> getField56A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("56A");
            final List<Field56A> result = new ArrayList<Field56A>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field56A(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 56D, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 56D at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field56D objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field56D> getField56D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("56D");
            final List<Field56D> result = new ArrayList<Field56D>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field56D(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 57A, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 57A at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field57A objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field57A> getField57A() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("57A");
            final List<Field57A> result = new ArrayList<Field57A>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field57A(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 57B, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 57B at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field57B objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field57B> getField57B() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("57B");
            final List<Field57B> result = new ArrayList<Field57B>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field57B(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 57D, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 57D at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field57D objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field57D> getField57D() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("57D");
            final List<Field57D> result = new ArrayList<Field57D>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field57D(tags[i].getValue()));
            }
            return result;
        }
    }

    /**
	 * Iterates through block4 fields and return all occurrences of fields whose names matches 72, 
	 * or <code>Collections.emptyList()</code> if none is found.<br />
	 * Multiple occurrences of field 72 at MT202COV are expected at one sequence or across several sequences.
	 * 
	 * @return a List of Field72 objects or <code>Collections.emptyList()</code> if none is not found
	 * @see SwiftTagListBlock#getTagsByName(String)
	 * @throws IllegalStateException if SwiftMessage object is not initialized
	 */
    public List<Field72> getField72() {
        if (getSwiftMessage() == null) {
            throw new IllegalStateException("SwiftMessage was not initialized");
        }
        if (getSwiftMessage().getBlock4() == null) {
            log.info("block4 is null");
            return Collections.emptyList();
        } else {
            final Tag[] tags = getSwiftMessage().getBlock4().getTagsByName("72");
            final List<Field72> result = new ArrayList<Field72>();
            for (int i = 0; i < tags.length; i++) {
                result.add(new Field72(tags[i].getValue()));
            }
            return result;
        }
    }
}
