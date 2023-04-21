package net.sf.sail.webapp.domain.impl;

/**
 * @author Laurel Williams
 * 
 * @version $Id: OfferingParameters.java 1597 2008-01-08 05:17:49Z hiroki $
 * 
 * Parameters that a user would probably need to supply in order to create an
 * offering from the UI.
 * 
 */
public class OfferingParameters {

    private String name;

    private Long curnitId;

    private Long jnlpId;

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

    /**
	 * @return the curnitId
	 */
    public Long getCurnitId() {
        return curnitId;
    }

    /**
	 * @param curnitId
	 *            the curnitId to set
	 */
    public void setCurnitId(Long curnitId) {
        this.curnitId = curnitId;
    }

    /**
	 * @return the jnlpId
	 */
    public Long getJnlpId() {
        return jnlpId;
    }

    /**
	 * @param jnlpId the jnlpId to set
	 */
    public void setJnlpId(Long jnlpId) {
        this.jnlpId = jnlpId;
    }
}
