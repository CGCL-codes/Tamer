package org.sourceforge.vlibrary.user.valuebeans;

/**
 * Value object representing a book edit transaction
 * @version $Revision$ $Date$
 *
 */
public class BookEditTransaction {

    private String title = null;

    private long id = 0;

    private String action = null;

    /** Creates new BookEditTransaction */
    public BookEditTransaction() {
    }

    /** Getter for property action.
     * @return Value of property action.
     */
    public java.lang.String getAction() {
        return action;
    }

    /** Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }

    /** Getter for property id.
     * @return Value of property id.
     */
    public long getId() {
        return id;
    }

    /** Setter for property id.
     * @param id New value of property id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }

    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
}
