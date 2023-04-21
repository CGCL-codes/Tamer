package net.sf.karatasi.databaseoperations.edit;

import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;

/** This class combines the data necessary for a selection list.
 * It is the super class of all specialized lists containers.
 * The close() method has to be called before the container gets discarded.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class SelectionListContainer {

    /** The headline of the list. */
    protected String headline = null;

    /** The data model for the list. */
    protected DefaultListModel dataModel;

    /** The descriptor of the selection used for the selection stack. */
    protected String description = null;

    /** Constructor. */
    public SelectionListContainer() {
        dataModel = new DefaultListModel();
    }

    /** Close the container.
     * This method frees resources, and de-registers any notifications.
     */
    public void close() {
    }

    /** Mark a container that stays on the top of the stack.
     * @return true if it stays on the top of the stack.
     */
    public boolean isSticky() {
        return false;
    }

    /** Getter for the headline.
     * @return the headline.
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * @param headline the headline to set
     */
    protected void setHeadline(final String headline) {
        this.headline = headline;
    }

    /** Getter for the data model.
     * @return the data model
     */
    public DefaultListModel getDataModel() {
        return dataModel;
    }

    /** Setter for the data model.
     * @param dataModel the data model to set
     */
    protected void setDataModel(final DefaultListModel dataModel) {
        this.dataModel = dataModel;
    }

    /** Getter for the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /** Setter for the description.
     * @param description the description to set
     */
    protected void setDescription(final String description) {
        this.description = description;
    }

    /** A list element has been selected.
     *
     * @param index the position in the list.
     * @return the controller of the detail panel to be displayed. Or null if we ignore it.
     */
    protected DetailController getControllerForListElement(final int index) {
        return null;
    }

    /** Get a cell renderer instance for the side board list.
     * @return the cell renderer, or null.
     */
    public ListCellRenderer getSideboardListCellRenderer() {
        return null;
    }
}
