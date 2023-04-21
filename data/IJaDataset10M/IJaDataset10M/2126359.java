package com.tensegrity.palobrowser.table;

import org.palo.api.ext.ui.table.TableFormat;

/**
 * <code>AxisModel</code> for use in a
 * {@link com.tensegrity.palobrowser.table.TableModel}.
 * 
 * @author Stepan Rutz
 * @version $ID$
 */
public interface AxisModel {

    /**
     * Returns the key
     * @returns the key
     */
    AxisModelKey getKey();

    /**
     * Returns the domain object for the model.
     * @return the domain object for the model.
     */
    DomainObject getDomainObject();

    /**
     * Returns the display name for the model.
     * @return the display name for the model.
     */
    String getDisplayName();

    /**
     * Returns the format for the model.
     * @return the format for the model.
     */
    TableFormat getFormat();

    /**
     * Returns the parent <code>AxisModel</code> or <code>null</code> if no parent exists.
     * @return the parent <code>AxisModel</code> or <code>null</code> if no parent exists.
     */
    AxisModel getParent();

    /**
     * Returns the children or null if none exist.
     * @return the children or null if none exist.
     */
    AxisModel[] getChildren();

    /**
     * Returns the level of this model.
     * @return the level of this model.
     */
    int getLevel();

    /**
     * Returns whether the model is expanded or not.
     * This setting is only significant if the model
     * has any children.
     * @return whether the model is expanded or not.
     */
    boolean isExpanded();

    /**
     * Sets whether the model is expanded or not.
     * @param expanded 
     */
    void setExpanded(boolean expanded);
}
