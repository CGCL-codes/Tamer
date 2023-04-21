package gov.noaa.eds.xapi.generic;

import java.util.Vector;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

/**
 * A basic Set of resources.
 * @version $Id: GenericResourceSet.java,v 1.2 2004/12/23 22:26:01 mrxtravis Exp $
 * @author tns
 */
public class GenericResourceSet extends Vector implements ResourceSet {

    /** Creates a new instance of GenericResourceSet */
    public GenericResourceSet() {
    }

    /**Add a resource to this set
     *@param resource The resource to add
     */
    public void addResource(Resource resource) throws XMLDBException {
        this.add(resource);
    }

    /**Removes the resource at the specified index
     *@param index The index of the resource to remove.
     */
    public void removeResource(long index) throws XMLDBException {
        int intIndex = convertLong(index);
        this.remove(intIndex);
    }

    /**Returns the size of this set.
     *@return The size
     */
    public long getSize() throws XMLDBException {
        return (long) this.size();
    }

    /**Vectors use ints and xmldb api requires long so this converts
     *the long to an int, throwing an exception if the long is out of the
     *integer range.
     */
    private int convertLong(long l) {
        if (l > Integer.MAX_VALUE) {
            throw new IllegalStateException("specified parameter can not be " + "larger than the largest int value" + Integer.MAX_VALUE + " but it was " + l);
        }
        return (int) l;
    }

    /**Returns the resource at the specified index.  Throws an XMLDBException 
     *if there is the index is inappropriate.
     *@param index The index of the resource to return
     *@return The resource at the specified index.
     */
    public Resource getResource(long index) throws XMLDBException {
        int intIndex = convertLong(index);
        try {
            return (Resource) this.get(intIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new XMLDBException(ErrorCodes.NO_SUCH_RESOURCE);
        }
    }

    /**Not implemented
     *@todo Implement this method
     */
    public Resource getMembersAsResource() throws XMLDBException {
        throw new XMLDBException(ErrorCodes.NOT_IMPLEMENTED);
    }

    /**Returns an iterator for this set of resources
     *@return The iterator.
     */
    public ResourceIterator getIterator() throws XMLDBException {
        return new GenericResourceIterator(this.iterator());
    }
}
