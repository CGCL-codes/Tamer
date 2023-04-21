package org.mbari.instrument.model;

public class Identifier {

    /**
	 * This is the <code>Term</code> that is linked to the
	 * <code>Identifier</code>
	 */
    private org.mbari.instrument.model.Term term;

    /**
	 * This is the name associated with the particular <code>Identifier</code>
	 */
    private String name;

    /**
	 * @return the lnkTerm
	 */
    public org.mbari.instrument.model.Term getTerm() {
        return term;
    }

    /**
	 * @param lnkTerm
	 *            the lnkTerm to set
	 */
    public void setTerm(org.mbari.instrument.model.Term term) {
        this.term = term;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
