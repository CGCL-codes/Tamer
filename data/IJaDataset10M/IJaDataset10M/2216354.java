package uk.ac.ebi.intact.application.search3.advancedSearch.powerSearch.struts.view.bean;

/**
 * Used to store information about cv objects. It is used to store the shortlabels of the CVs, that are shown in the
 * drop down lists.
 *
 * @author Anja Friedrichsen
 * @version $Id: CvBean.java 4573 2006-02-02 16:13:50Z skerrien $
 */
public class CvBean {

    /**
     * AC of the CV term.
     */
    private String ac = null;

    /**
     * Shortlabel of the CV term.
     */
    private String shortlabel = null;

    /**
     * FullName of the CV term.
     */
    private String fullname = null;

    /**
     * Constructs a CvBean object.
     *
     * @param ac         ac of the CV term.
     * @param shortlabel shortlabel of the CV term.
     * @param fullname   fullname of the CV term.
     */
    public CvBean(String ac, String shortlabel, String fullname) {
        this.ac = ac;
        this.shortlabel = shortlabel;
        this.fullname = fullname;
    }

    private CvBean() {
    }

    /**
     * Returns the ac value.
     *
     * @return a String representing the ac value
     *
     * @see #setAc
     */
    public String getAc() {
        return ac;
    }

    /**
     * Specifies the ac value.
     *
     * @param ac a String specifying the ac value
     *
     * @see #getAc
     */
    public void setAc(String ac) {
        this.ac = ac;
    }

    /**
     * Returns the shortlabel value.
     *
     * @return a String representing the shortlabel value
     *
     * @see #setShortlabel
     */
    public String getShortlabel() {
        return shortlabel;
    }

    /**
     * Specifies the shortlabel value.
     *
     * @param shortlabel a String specifying the shortlabel value
     *
     * @see #getShortlabel
     */
    public void setShortlabel(String shortlabel) {
        this.shortlabel = shortlabel;
    }

    /**
     * Gets this object's fullname.
     *
     * @return a String representing the fullname value
     *
     * @see #setFullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Sets this object's fullname.
     *
     * @param fullname a String specifying the fullname value
     *
     * @see #getFullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("CvBean");
        sb.append("{ac='").append(ac).append('\'');
        sb.append(", shortlabel='").append(shortlabel).append('\'');
        sb.append(", fullname='").append(fullname).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
