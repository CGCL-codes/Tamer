package icreate.comps;

public class Group implements icreate.mans.DataObject, Orphan, java.io.Serializable {

    long id;

    String title = "";

    long creator;

    public Group() {
    }

    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public long getId() {
        return id;
    }

    /** Setter for property id.
     * @param id New value of property id.
     *
     */
    public void setId(long id) {
        this.id = id;
    }

    public icreate.mans.DataObject getParent() {
        return null;
    }

    public void setParent(icreate.mans.DataObject dobj) {
        ;
    }

    /** Getter for property title.
     * @return Value of property title.
     *
     */
    public java.lang.String getTitle() {
        return title;
    }

    /** Setter for property title.
     * @param title New value of property title.
     *
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    /** Getter for property creator.
     * @return Value of property creator.
     *
     */
    public long getCreator() {
        return creator;
    }

    /** Setter for property creator.
     * @param creator New value of property creator.
     *
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }
}
