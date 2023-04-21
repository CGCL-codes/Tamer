package uk.ac.reload.straker.datamodel.learningdesign;

import uk.ac.reload.straker.IIcons;

/**
 * A validate/check item for the LD Check routine
 * 
 * @author Phillip Beauvoir
 * @version $Id: LD_CheckListItem.java,v 1.2 2006/07/10 11:50:47 phillipus Exp $
 */
public class LD_CheckListItem implements IIcons {

    /**
     * The Item description
     */
    private String _description;

    /**
     * Whether this list item is an error or OK
     */
    private boolean _isError;

    /**
     * Constructor
     * @param description
     * @param isError
     */
    public LD_CheckListItem(String description, boolean isError) {
        _description = description;
        _isError = isError;
    }

    /**
     * @return The description string
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Set the description string
     * @param description
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * @return True if this Item is an error type, or false if not
     */
    public boolean isError() {
        return _isError;
    }

    /**
     * Set if this Item is an error type, or false if not
     * @param error
     */
    public void setIsError(boolean error) {
        _isError = error;
    }

    /**
     * @return The image nasme for this item
     */
    public String getImageName() {
        return isError() ? ICON_CROSS : ICON_TICK;
    }

    public String toString() {
        return _description == null ? "" : _description;
    }
}
