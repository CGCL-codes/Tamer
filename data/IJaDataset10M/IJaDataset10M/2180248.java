package oracle.toplink.essentials.changesets;

/**
 * <p>
 * <b>Purpose</b>: This interface provides public API to the class responsible for holding the change made to a directToFieldMapping.
 * <p>
 * <b>Description</b>: This changeRecord stores the value that the direct to field was changed to.
 * <p>
 */
public interface DirectToFieldChangeRecord extends ChangeRecord {

    /**
     * ADVANCED:
     * Returns the new value assigned during the change
     * @return java.lang.Object
     */
    public Object getNewValue();
}
