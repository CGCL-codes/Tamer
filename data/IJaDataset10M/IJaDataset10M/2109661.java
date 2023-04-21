package de.fu_berlin.inf.dpp.whiteboard.sxe.records;

import de.fu_berlin.inf.dpp.whiteboard.sxe.constants.RecordType;
import de.fu_berlin.inf.dpp.whiteboard.sxe.records.serializable.RecordDataObject;

/**
 * <p>
 * The most generalized version of an SXE record.
 * </p>
 * 
 * <p>
 * All SXE edit classes (records) should implement this.
 * </p>
 * 
 * @author jurke
 */
public interface IRecord {

    public boolean isPartOfVisibleDocument();

    /**
	 * Tries to apply this record to our document.
	 * 
	 * @return if applying was successful, including conflict resolution
	 */
    public boolean apply(DocumentRecord document);

    /**
	 * 
	 * @return whether the record can be applied
	 */
    public boolean canApply();

    /**
	 * The record type
	 */
    public RecordType getRecordType();

    /**
	 * <p>
	 * The target of this record.
	 * </p>
	 * 
	 * <p>
	 * Note that it should return a this-reference in case of a new-record, thus
	 * is a convenient method to access the record and the next parent
	 * ElementRecord by getTarget().getParent()
	 * </p>
	 * 
	 */
    public NodeRecord getTarget();

    /**
	 * the last modifier
	 */
    public String getLastModifiedBy();

    /**
	 * 
	 * @return the serializable record data object
	 */
    public RecordDataObject getRecordDataObject();

    /**
	 * 
	 * @return the sender of this record
	 */
    public String getSender();

    /**
	 * Sets the sender of this record
	 * 
	 * @param sender
	 * @throws de.fu_berlin.inf.dpp.whiteboard.sxe.exceptions.CommittedRecordException
	 */
    public void setSender(String sender);
}
