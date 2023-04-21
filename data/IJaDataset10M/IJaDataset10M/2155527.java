package gov.noaa.eds.xapi.generic;

import java.util.Hashtable;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

/**
 * An implementation of an XAPI Database, when the database is just
 * a collection of XAPI Collection objects.
 * @author tns
 * @version $Id: GenericDatabase.java,v 1.2 2004/12/23 22:26:01 mrxtravis Exp $
 */
public class GenericDatabase extends GenericConfigurable implements Database {

    private Hashtable collections = new Hashtable();

    private String conformanceLevel = "Core Level 1";

    /** Creates a new instance of ListDatabase */
    public GenericDatabase() {
    }

    /**Add a Collection to the list of collections.  If a collection is added
     * that has the same name as another collection, the old one is replaced.
     *@param collection A collection to add.
     */
    public void addCollection(Collection collection) throws XMLDBException {
        String name = collection.getName();
        if (name == null) {
            throw new NullPointerException("Collection's name property must be set.");
        }
        this.collections.put(collection.getName(), collection);
    }

    /**Checks the list of collections and determines if one of
     *them is named with the specified uri.
     *@param uri The uri to check if a collection exists
     */
    public boolean acceptsURI(String uri) {
        return collections.containsKey(uri);
    }

    /**Returns null, use getNames
     *@depricate
     */
    public String getName() {
        return null;
    }

    /**Returns the known collections
     *@return The known collection names
     */
    public String[] getNames() {
        return (String[]) collections.keySet().toArray(new String[0]);
    }

    /** Returns the conformance level.  The default is "Core Level 1.0".
     *@return The conformance level
     */
    public String getConformanceLevel() {
        return this.conformanceLevel;
    }

    /**Sets the conformance level as specified at {@link http://xmldb-org.sourceforge.net/xapi/}.
     *The default is set to Core Level 0.
     *
     *@param conformanceLevel
     */
    public void setConformanceLevel(String conformanceLevel) {
        this.conformanceLevel = conformanceLevel;
    }

    /**Returns a collection with the specified uri
     *@param uri The uri of the collection
     *@param userName The user name to use for permissions
     *@param password The user name to use for permissions
     */
    public Collection getCollection(String uri, String userName, String password) throws XMLDBException {
        Collection col = (Collection) collections.get(uri);
        if (col == null) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }
        return col;
    }
}
