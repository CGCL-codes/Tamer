package ejb.objectmodel.administration;

/**
 * Created Sep 13, 2003 1:05:16 AM
 * Code generated by the Sun ONE Studio EJB Builder
 * @author Administrator
 */
public final class Check_in_Library_ListKey implements java.io.Serializable {

    public java.lang.Integer checkinLibrary;

    public java.lang.Integer library_Id;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(java.lang.Object otherOb) {
        if (this == otherOb) {
            return true;
        }
        if (!(otherOb instanceof ejb.objectmodel.administration.Check_in_Library_ListKey)) {
            return false;
        }
        ejb.objectmodel.administration.Check_in_Library_ListKey other = (ejb.objectmodel.administration.Check_in_Library_ListKey) otherOb;
        return ((checkinLibrary == null ? other.checkinLibrary == null : checkinLibrary.equals(other.checkinLibrary)) && (library_Id == null ? other.library_Id == null : library_Id.equals(other.library_Id)));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return ((checkinLibrary == null ? 0 : checkinLibrary.hashCode()) ^ (library_Id == null ? 0 : library_Id.hashCode()));
    }
}