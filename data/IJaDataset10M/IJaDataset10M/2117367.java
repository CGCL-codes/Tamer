package net.sf.webwarp.modules.history;

import java.util.List;
import org.joda.time.DateTime;
import net.sf.webwarp.util.history.HistorizedEntity;

/**
 * The DAO interface to {@link HistoryEntry}.
 * 
 * @see HistoryDAOImpl
 * @version $Id: $
 * @author mos
 */
public interface HistoryDAO {

    /**
     * Simple DAO Method.
     * 
     * @param entityType
     */
    public abstract void deleteEntityType(EntityType entityType);

    /**
     * Simple DAO Method.
     * 
     * @param id
     * @return
     */
    public abstract EntityType getEntityType(Integer id);

    /**
     * Simple DAO Method.
     * 
     * @param entityType
     * @return
     */
    public abstract Integer saveEntityType(EntityType entityType);

    /**
     * The entity types are sorted by 'name' ascending.
     * 
     * @return
     */
    public abstract List<EntityType> loadAllEntityTypes();

    /**
     * Simple DAO Method.
     * 
     * @param name
     * @return
     */
    public abstract EntityType getEntityType(String name);

    /**
     * Simple DAO Method.
     * 
     * @param id
     * @return
     */
    public abstract HistoryEntry getHistoryEntry(Long id);

    /**
     * Inserts a new historical entry into the database. So setting one of the properties
     * {@link HistoryEntry#getValidTo()} or {@link HistoryEntry#getOverwrittenAt()} is strictly illegal. If the data
     * inserted is <b>not time based</b>, setting the property {@link HistoryEntry#getValidFrom()} is also illegal.
     * 
     * @param historyEntry
     * @param timeBased
     * @return the primary key of the inserted object.
     */
    public abstract Long saveHistoryEntry(HistoryEntry historyEntry, boolean timeBased);

    /**
     * Gets the current entry which is valid a certain date. This function only makes sense for time based data.
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @param validAt
     * @return
     */
    public abstract HistoryEntry getCurrentValue(String typeName, String entityID, String propertyName, DateTime validAt);

    /**
     * Gets the current entry of a simple historised property. Only use if the object is not time based!
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @return
     */
    public abstract HistoryEntry getCurrentValue(String typeName, String entityID, String propertyName);

    /**
     * Gets the current entry of a time based historised property with a validAt equals to now (current timestamp). This
     * function only makes sense for time based data.
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @return
     */
    public abstract HistoryEntry getCurrentValueNow(String typeName, String entityID, String propertyName);

    /**
     * Gets the value until of a simple historised property. Only use if the object is not time based!
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @param createdUntil
     * @return
     */
    public abstract HistoryEntry getValueUntil(String typeName, String entityID, String propertyName, DateTime createdUntil);

    /**
     * Gets the entry which is valid at a certain date and was created until a certain date (historical value of time
     * based data).
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @param validAt
     * @param createdUntil
     * @return
     */
    public abstract HistoryEntry getValueUntil(String typeName, String entityID, String propertyName, DateTime validAt, DateTime createdUntil);

    /**
     * Gets the value of a historised property which was created until which is valid now (current timestamp).This
     * function only makes sense for time based data.
     * 
     * @param typeName
     * @param entityID
     * @param propertyName
     * @param createdUntil
     * @return
     */
    public abstract HistoryEntry getValueUntilNow(String typeName, String entityID, String propertyName, DateTime createdUntil);

    /**
     * Deletes a historical data entry.
     * <ul>
     * <li>For simple historised there will set the overwrittenAt property with the current timestamp, meaning that
     * this entry is no longer visible.
     * <li>For time based data there it will set the overwritten property with the current timestamp, meaning that this
     * entry is no longer visible and makes the entry, which is valid until the found one (validFrom), valid until the
     * validTo of the found one by inserting an additional entry.
     * 
     * @param id
     *            the primary key of a {@link HistorizedEntity}
     */
    public abstract void delete(Long id);

    /**
     * Definitely deletes the found history entries.
     * 
     * @param typeName
     *            may not be null
     * @param entityID
     * @param propertyName
     * @return the number of rows deleted
     */
    public int purgeHistory(String typeName, String entityID, String propertyName);

    /**
     * Make a search in the history.
     * <p>
     * Passing a {@link HistorySearchBean#getCreatedUntil()} == null means: only show actual values (values with
     * {@link HistoryEntry#getOverwrittenAt()} == null).
     * <p>
     * Passing a {@link HistorySearchBean#getValidAt()} == null means: show the whole timeline of the entity. If the
     * data is not time based, this has no influence since simple historised data has no validAt.
     * 
     * @param searchBean
     * @return
     */
    public abstract List<HistoryEntry> searchHistory(HistorySearchBean searchBean);
}
