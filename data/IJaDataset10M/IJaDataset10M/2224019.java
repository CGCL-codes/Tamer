package record.webcore.db.data;

import java.util.UUID;
import record.Utility;
import record.data.AbstractListenableData;
import record.data.DataEventType;

/**
 * The Class FinancialSubClass.
 * 
 * @author Lin Yi-Li
 */
public class FinancialSubClass extends AbstractListenableData implements Comparable<FinancialSubClass> {

    /** The id. */
    private UUID id;

    /** The class id. */
    private UUID classId;

    /** The name. */
    private String name;

    /** The sort order. */
    private int sortOrder;

    /**
	 * Instantiates a new financial sub class.
	 */
    public FinancialSubClass() {
        this.id = null;
        this.classId = null;
        this.name = null;
        this.sortOrder = 0;
    }

    /**
	 * Gets the id.
	 * 
	 * @return The id
	 */
    public UUID getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id
	 *        The new id
	 */
    public void setId(UUID id) {
        if (Utility.equals(this.id, id) == false) {
            this.fireEvent(DataEventType.EVENT_CHANGED);
            this.id = id;
        }
    }

    /**
	 * Gets the class id.
	 * 
	 * @return The class id
	 */
    public UUID getClassId() {
        return classId;
    }

    /**
	 * Sets the class id.
	 * 
	 * @param classId
	 *        The new class id
	 */
    public void setClassId(UUID classId) {
        if (Utility.equals(this.classId, classId) == false) {
            this.fireEvent(DataEventType.EVENT_CHANGED);
            this.classId = classId;
        }
    }

    /**
	 * Gets the name.
	 * 
	 * @return The name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 * 
	 * @param name
	 *        The new name
	 */
    public void setName(String name) {
        if (Utility.equals(this.name, name) == false) {
            this.fireEvent(DataEventType.EVENT_CHANGED);
            this.name = name;
        }
    }

    /**
	 * Gets the sort order.
	 * 
	 * @return The sort order
	 */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
	 * Sets the sort order.
	 * 
	 * @param sortOrder
	 *        The new sort order
	 */
    public void setSortOrder(int sortOrder) {
        if (this.sortOrder != sortOrder) {
            this.fireEvent(DataEventType.EVENT_CHANGED);
            this.sortOrder = sortOrder;
        }
    }

    @Override
    public int compareTo(FinancialSubClass o) {
        return Integer.valueOf(this.getSortOrder()).compareTo(o.getSortOrder());
    }
}
