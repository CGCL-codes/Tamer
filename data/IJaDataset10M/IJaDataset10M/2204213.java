package uk.ac.ebi.intact.sanity.check.model;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id: AnnotatedBean.java,v 1.1 2005/07/28 16:13:29 catherineleroy Exp $
 */
public class AnnotatedBean extends IntactBean {

    private String shortlabel;

    private String fullname;

    public AnnotatedBean() {
    }

    public String getShortlabel() {
        return shortlabel;
    }

    public void setShortlabel(String shortlabel) {
        this.shortlabel = shortlabel;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
