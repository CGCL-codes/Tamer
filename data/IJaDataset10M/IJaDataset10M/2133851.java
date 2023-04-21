package jpersist.interfaces;

/**
 * This interface is extended if a class instance is to be used for a global delete 
 * (i.e. no where clause).  If an object is used for a delete and no where clause 
 * is provided and/or generatable (persistent object), then the object must have this 
 * interface or an exception will be thrown.
 */
public interface GlobalDelete {
}
