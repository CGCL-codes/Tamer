package mipt.data.store.table;

import mipt.data.store.DataID;
import mipt.data.store.PersistentData;
import mipt.data.store.PersistentDataSet;
import mipt.io.table.RowSet;

/**
 * The extension of PersistentDataCreator having two extra optional arguments of methods:
 * 	id and name. 
 * Name (String) parameter is often used to create object with name:
 *  1) such object can be loaded with only id and name to load other fields later 
 *   (that is efficient for applications with UI);
 *  2) such object usually implements {@link mipt.common.Named} interface.
 * Id (DataID) parameter is sometimes used to create object with fixed identity
 *  (it is useful during mass data transfers like migration from a legacy system).
 *  Id can be of any type, but sometimes DataID stores data type and allows creating
 *  the data of other entity/type than the parent DataStorage relates to.
 * Note: id should (but name should NOT!) be removed from arrays before sending them
 *  to constructors of PersistentData or similar methods.
 * @author Evdokimov
 */
public interface XPersistentDataCreator<T extends PersistentData> extends PersistentDataCreator<T> {

    /**
	 * Creates new data (with new (id==null) or given identity)
	 *  with the given name (optional) and with null fields (or with default fields).
	 * @see PersistentDataCreator#newData()
	 */
    T newData(DataID id, String name);

    /**
	 * Creates new data (with new (id==null) or given identity) 
	 *  with the given name (optional) and with the given fields.
	 * @see PersistentDataCreator#newData(Object[])
	 */
    T newData(Object[] fields, DataID id, String name);

    /**
	 * Creates new data (with new (id==null) or given identity)
	 *  with the given name (optional) and with the given some fields (others are nulls).
	 * @see PersistentDataCreator#newData(String[], Object[])
	 */
    T newData(String[] fieldNames, Object[] fields, DataID id, String name);

    /**
	 * Creates new data (with new (if idAttrName!=null) or given identity)
	 *  with names (if nameAttrName!=null) and with fields (other columns of RowSet).
	 * Caller must loop through all results to complete creation.
	 * @see PersistentDataCreator#newSet(RowSet)
	 */
    PersistentDataSet<T> newSet(RowSet newValues, String idAttrName, String nameAttrName);
}
