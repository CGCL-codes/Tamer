package jhomenet.ui.table.model;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public abstract class AbstractStatusTableRow extends AbstractTableRow {

    /**
	 * Data status options.
	 */
    public static enum TablerowStatusOptions {

        /**
		 * Yes
		 */
        YES, /**
		 * No 
		 */
        NO, /**
		 * Warning: error occurred
		 */
        WARNING
    }

    /**
	 * 
	 */
    public static final String STATUS_PROPERTY = "status";

    /**
	 * Current status.
	 */
    private TablerowStatusOptions status = TablerowStatusOptions.NO;

    /**
	 * Constructor.
	 */
    public AbstractStatusTableRow() {
        super();
    }

    /**
	 * 
	 * @param newStatus
	 */
    public final void setStatus(TablerowStatusOptions newStatus) {
        TablerowStatusOptions oldStatus = getStatus();
        this.status = newStatus;
        this.propertyChangeSupport.firePropertyChange(STATUS_PROPERTY, oldStatus, status);
    }

    /**
	 * 
	 * @return
	 */
    public final TablerowStatusOptions getStatus() {
        return this.status;
    }
}
