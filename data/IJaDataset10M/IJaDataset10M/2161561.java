package pl.org.minions.stigma.databases.xml;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import pl.org.minions.stigma.databases.Resourcer;
import pl.org.minions.stigma.databases.parsers.GenericParser;
import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.parsers.ParserFactory;

/**
 * Abstract class which represents XML database.
 * @param <ObjectType>
 *            object which is database element (has id)
 * @param <StorageType>
 *            object which is directly stored in XML
 *            database (is parsable and convertible to
 *            ObjectType)
 */
public abstract class XmlDB<ObjectType extends XmlDbElem, StorageType extends Parsable> {

    private Map<Short, ObjectType> map = new HashMap<Short, ObjectType>();

    private URI uri;

    private Converter<ObjectType, StorageType> converter;

    private Class<StorageType> clazz;

    private Class<? extends StorageType>[] childrenClasses;

    private GenericParser<StorageType> cachedParser;

    private boolean validate;

    /**
     * Constructor for derived classes.
     * @param clazz
     *            class used by XML parser
     * @param converter
     *            converter between stored in XML and in
     *            database types
     * @param childrenClasses
     *            classes derived from base class which also
     *            could be included in DB
     * @param validate
     *            whether or not loaded files should be
     *            validate against proper schemas
     * @param uri
     *            URI to where base is located
     */
    protected XmlDB(URI uri, boolean validate, Class<StorageType> clazz, Converter<ObjectType, StorageType> converter, Class<? extends StorageType>... childrenClasses) {
        this.uri = uri;
        this.clazz = clazz;
        this.childrenClasses = childrenClasses;
        this.converter = converter;
    }

    /**
     * Adds object to database.
     * @param obj
     *            object to add
     */
    protected final void add(ObjectType obj) {
        map.put(obj.getId(), obj);
    }

    /**
     * Creates URL from id of database element.
     * @param id
     *            id of database element
     * @return URL to file representing database element
     */
    protected final URL createUrlFromId(short id) {
        String path = String.format(getDbDir() + "/" + getFilePrefix() + "%04X.xml", id);
        return Resourcer.getXMLResourceUrl(uri, path);
    }

    /**
     * Gets object by id form database.
     * @param id
     *            id of object
     * @return object or null if it is not in database
     */
    protected final ObjectType get(short id) {
        return map.get(id);
    }

    /**
     * Return object form database by it's id.
     * @param id
     *            id of object
     * @return object if it exists in database, otherwise
     *         null
     */
    public abstract ObjectType getById(short id);

    /**
     * Returns "object" converted to "data".
     * @param obj
     *            object to be converted
     * @return data representing object
     * @see Converter#buildData(Object)
     */
    protected final StorageType getData(ObjectType obj) {
        if (obj == null) return null;
        return converter.buildData(obj);
    }

    /**
     * Returns "object" for given id converted to "data".
     * May return {@code null} when no such object in
     * database.
     * @param id
     *            id of object to be converted
     * @return data representing object
     * @see Converter#buildData(Object)
     */
    protected final StorageType getData(short id) {
        return getData(get(id));
    }

    /**
     * Gets directory to database.
     * @return directory to database
     */
    public abstract String getDbDir();

    /**
     * Gets database file default prefix.
     * @return database file prefix
     */
    public abstract String getFilePrefix();

    /**
     * Returns keys stored in database.
     * @return keys stored in database.
     */
    protected final Collection<Short> getKeys() {
        return map.keySet();
    }

    /**
     * Creates parser for database element.
     * @return database element's parser
     */
    protected GenericParser<StorageType> getParser() {
        if (cachedParser == null) cachedParser = ParserFactory.getInstance().getParser(validate, clazz, childrenClasses);
        return cachedParser;
    }

    /**
     * Checks whether database contains object which
     * specified id. For derived classes.
     * @param id
     *            id of object
     * @return true if database contains object, otherwise
     *         false
     */
    protected final boolean hasId(short id) {
        return map.containsKey(id);
    }

    /**
     * Returns set of currently available identifiers in
     * database (there might be more, yet not read).
     * @return available identifiers
     */
    public Set<Short> keys() {
        return map.keySet();
    }

    /**
     * Puts object into database. For derived classes.
     * @param object
     *            object to put into database
     * @return converted object
     */
    protected final ObjectType put(StorageType object) {
        ObjectType obj = converter.buildObject(object);
        short id = obj.getId();
        map.put(id, obj);
        return obj;
    }

    /**
     * Returns all currently available values stored in
     * database.
     * @return available objects
     */
    public Collection<ObjectType> values() {
        return map.values();
    }
}
