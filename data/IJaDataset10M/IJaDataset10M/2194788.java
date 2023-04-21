package org.aarboard.ckeditor.connector.providers;

/**
 *
 * @author a.schild
 */
public interface IObject {

    /**
     * Get name to be displayed in list
     *
     * @return Name to display in list
     */
    public String getName();

    /**
     * Get the URL to be returned to the editor
     * 
     * @return URL to be returned to the editor
     */
    public String getURL();

    /**
     * Is the user allowed to rename this folder
     *
     * @return True if user is allowed to rename the folder
     */
    public boolean isRenameAllowed();

    /**
     * Is the user allowed to delete this folder
     *
     * @return True if user is allowed to delete this folder
     */
    public boolean isDeleteAllowed();

    /**
     * Is the user allowed to resize this file
     *
     * @return True if the user is allowed to resize this file (Must be a image)
     */
    public boolean isResizeAllowed();

    /**
     * Delete the current object
     *
     * @return true when deleted OK
     */
    public boolean delete();

    /**
     * Rename this file
     * 
     * @param newName
     * @return True when rename succeeded
     */
    public boolean handleRename(String newName);

    /**
     * Can we select this item in the File/Link browser ?
     * 
     * 
     * @return  True when this item can be selected in the browser
     */
    public boolean isSelectAllowed();
}
