package net.wotonomy.control;

/**
* A simple extension of EODataSource that 
* allows for indexed insertion.  The wotonomy
* implementation of EODisplayGroup supports 
* this and will use it if possible.  This is
* useful for classes like the PropertyDataSource,
* where the ordering of items in an indexed 
* property may be important.
*
* @author michael@mpowers.net
* @author $Author: cgruber $
* @version $Revision: 893 $
*/
public abstract class OrderedDataSource extends EODataSource {

    /**
    * Inserts the specified object into this data source,
    * at the specified index.
    */
    public abstract void insertObjectAtIndex(Object anObject, int anIndex);
}
