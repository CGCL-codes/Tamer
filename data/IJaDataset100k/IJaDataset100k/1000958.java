package uk.ac.ebi.intact.application.editor.struts.view.wrappers;

import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.model.AnnotatedObject;
import java.util.Date;

/**
 * An instance of this class contains information for the display library to display
 * results from a search page.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: ResultRowData.java 4988 2006-06-07 12:58:13Z baranda $
 */
public class ResultRowData {

    private String myAc;

    private String myShortLabel;

    private String myFullName;

    private String myType;

    private String myCreator;

    private String myUpdator;

    private Date myCreated;

    private Date myUpdated;

    public ResultRowData() {
    }

    /**
     * @param data an array of data. [0]. ac, [1], shortlabel and [2] full name
     * @param clazz the edit class type.
     */
    public ResultRowData(Object[] data, Class clazz) {
        this((String) data[0], (String) data[1], (String) data[2], (String) data[3], (String) data[4], (Date) data[5], (Date) data[6]);
        myType = IntactHelper.getDisplayableClassName(clazz);
    }

    /**
     * Constructs with an annotated object
     * @param annobj the Annotated object to represent a row
     */
    public ResultRowData(AnnotatedObject annobj) {
        this(annobj.getAc(), annobj.getShortLabel(), annobj.getFullName(), annobj.getCreator(), annobj.getUpdator(), annobj.getCreated(), annobj.getUpdated());
        myType = IntactHelper.getDisplayableClassName(annobj);
    }

    public ResultRowData(String ac, String shortlabel, String fullname, String creator, String updator, Date created, Date updated) {
        myAc = ac;
        myShortLabel = shortlabel;
        myFullName = fullname;
        myCreator = creator;
        myUpdator = updator;
        myCreated = created;
        myUpdated = updated;
    }

    /**
     * True if ACs match
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj != null) && (getClass() == obj.getClass())) {
            ResultRowData other = (ResultRowData) obj;
            return myAc.equals(other.myAc);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return myAc.hashCode();
    }

    public String getAc() {
        return myAc;
    }

    public String getShortLabel() {
        return myShortLabel;
    }

    public String getFullName() {
        return myFullName;
    }

    public String getType() {
        return myType;
    }

    public String getCreator() {
        return myCreator;
    }

    public String getUpdator() {
        return myUpdator;
    }

    public Date getCreated() {
        return myCreated;
    }

    public Date getUpdated() {
        return myUpdated;
    }
}
