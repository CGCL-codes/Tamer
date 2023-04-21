package org.codavaj.type;

import java.util.List;

/**
 * DOCUMENT ME!
 * 
 * Contributed by Brian Koehmstedt.
 */
public class EnumConst {

    private List comment = null;

    private String name;

    EnumConst() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getComment() {
        return comment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comment DOCUMENT ME!
     */
    public void setComment(List comment) {
        this.comment = comment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }
}
