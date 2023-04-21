package net.sf.webwarp.modules.history;

import net.sf.webwarp.util.hibernate.dao.IDType;
import net.sf.webwarp.util.history.HistorizedEntity;
import net.sf.webwarp.util.history.HistorizedProperty;
import org.joda.time.DateTime;

/**
 * Interface that represents a single historical entry. When the user creates or changes a historizable object several
 * items of this type are created, dependening on the (annotated) property and propagation settings.
 * 
 * @see HistorizedEntity
 * @see HistorizedProperty
 * @author atr
 */
public interface HistoryEntry extends IDType<Long> {

    /**
     * @return the type
     */
    public String getTypeName();

    /**
     * The date when the item should be valid from. This must not be the same as the {@link #getCreationDate()}.
     * 
     * @return the validFrom
     */
    public DateTime getValidFrom();

    /**
     * Returns the valid to date.
     * 
     * @return validTo
     */
    public DateTime getValidTo();

    /**
     * The ID that identifies the instance of the given entity type that owns this historical entry.
     * 
     * @return the entityID
     */
    public String getEntityID();

    /**
     * The property that was affected by the operation.
     * 
     * @return the propertyName
     */
    public String getPropertyName();

    /**
     * The new value (the old value is not saved directly, but could referenced by calling the dao old value lookup
     * method.
     * 
     * @return the value
     */
    public Object getValue();

    /**
     * The remarks string of the item.
     * 
     * @return the remarks
     */
    public String getRemarks();

    /**
     * Returns the createdFrom.
     * 
     * @return createdFrom
     */
    public DateTime getCreatedAt();

    /**
     * Returns the creation date.
     * 
     * @return createdTo
     */
    public DateTime getOverwrittenAt();

    /**
     * Returns the creartors name
     * 
     * @return createdBy
     */
    public String getCreatedBy();
}
