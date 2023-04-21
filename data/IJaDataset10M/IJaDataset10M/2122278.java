package net.sf.evemsp.model;

/**
 * Get information aboiut changes to the data stored in a AbstractRmsStore
 * 
 * @author Jaabaa
 */
public interface StoreListener {

    /**
	 * AbstractRmsRecord was added to the AbstractRmsStore
	 * @param src The AbstractRmsStore that was changed
	 * @param record The AbstractRmsRecord that was added
	 */
    void recordAdded(AbstractRmsStore src, AbstractRmsRecord record);

    /**
	 * AbstractRmsRecord was updated in the AbstractRmsStore
	 * @param src The AbstractRmsStore that was changed
	 * @param record The AbstractRmsRecord that was updated
	 */
    void recordUpdated(AbstractRmsStore src, AbstractRmsRecord record);

    /**
	 * AbstractRmsRecord was deleted from the AbstractRmsStore
	 * @param src The AbstractRmsStore that was changed
	 * @param record The AbstractRmsRecord that was deleted
	 */
    void recordDeleted(AbstractRmsStore src, AbstractRmsRecord record);
}
