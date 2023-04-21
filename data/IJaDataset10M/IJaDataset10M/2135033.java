package org.xaware.ide.xadev.gui.mapper;

/**
 * Class to hold the mapping status.
 * 
 * @author satishk
 * 
 */
class MappingStatus {

    /** Indicates some items are mapped */
    private boolean someItemsMapped = false;

    /** Indicates invalid mappings exist. */
    private boolean invalidMappingsExist = false;

    /** Indicates all items are mapped. */
    private boolean allItemsMapped = false;

    /** Indicates if this mapping status is to be ignored irrespective of the present status */
    private boolean ignoreMappingStatus = false;

    /**
     * Returns the value of someItemsMapped instance variable.
     * 
     * @return the someItemsMapped
     */
    protected boolean isSomeItemsMapped() {
        return someItemsMapped;
    }

    /**
     * sets the value of someItemsMapped instance variable.
     * 
     * @param someItemsMapped
     *            the someItemsMapped to set
     */
    protected void setSomeItemsMapped(boolean someItemsMapped) {
        this.someItemsMapped = someItemsMapped;
    }

    /**
     * Returns the value of invalidMappingsExist instance variable.
     * 
     * @return the invalidMappingsExist
     */
    protected boolean isInvalidMappingsExist() {
        return invalidMappingsExist;
    }

    /**
     * sets the value of invalidMappingsExist instance variable.
     * 
     * @param invalidMappingsExist
     *            the invalidMappingsExist to set
     */
    protected void setInvalidMappingsExist(boolean invalidMappingsExist) {
        this.invalidMappingsExist = invalidMappingsExist;
    }

    /**
     * Returns the value of allItemsMapped instance variable.
     * 
     * @return the allItemsMapped
     */
    protected boolean isAllItemsMapped() {
        return allItemsMapped;
    }

    /**
     * sets the value of allItemsMapped instance variable.
     * 
     * @param allItemsMapped
     *            the allItemsMapped to set
     */
    protected void setAllItemsMapped(boolean allItemsMapped) {
        this.allItemsMapped = allItemsMapped;
    }

    /**
     * Sets the ignore mapping status option .
     * 
     * @param ignoreMappingStatus
     *            if the status is to be ignored.
     */
    public void setIgnoreMappingStatus(boolean ignoreMappingStatus) {
        this.ignoreMappingStatus = ignoreMappingStatus;
    }

    /**
     * Returns true if the mapping status is to be ignored.
     * 
     * @return If the mapping status is to be ignored or not.
     */
    public boolean isIgnoreMappingStatus() {
        return ignoreMappingStatus;
    }
}
