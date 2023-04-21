package net.sf.logdistiller;

import java.util.*;
import net.sf.logdistiller.publishers.BasePublishers;
import net.sf.logdistiller.util.ExtensionHelper;
import net.sf.logdistiller.util.FormatUtil;

/**
 * Definition of available report publishers.
 */
public abstract class Publishers {

    /**
     * get the <code>Publishers</code> instances defined by this extension.
     *
     * @return the list of publishers
     * @see LogType
     */
    public abstract List<Publisher> definePublishers();

    public static final List<Publisher> ALL;

    public static final Map<String, Publisher> MAP;

    static {
        List<Publisher> all = new ArrayList<Publisher>(new BasePublishers().definePublishers());
        all.addAll(loadExtensionPublishers());
        ALL = Collections.unmodifiableList(all);
        Map<String, Publisher> map = new HashMap<String, Publisher>();
        for (Publisher type : ALL) {
            map.put(type.getId(), type);
        }
        MAP = Collections.unmodifiableMap(map);
    }

    /**
     * get all the report publishers defined (predefined and custom).
     */
    public static List<Publisher> getAllPublishers() {
        return ALL;
    }

    public static String listAllPublishersIds() {
        return FormatUtil.join(", ", MAP.keySet().iterator());
    }

    public static Publisher getPublisher(String id) {
        return MAP.get(id);
    }

    /**
     * Loads report publishers defined by extension mechanism: if defined, <code>publishers</code> property in
     * <code>logdistiller.properties</code> is the full class name of a concrete implementation of
     * <code>Publishers</code> class.
     *
     * @return List the list of every <code>Publisher</code> defined by loaded <code>Publishers</code>
     */
    private static List<Publisher> loadExtensionPublishers() {
        List<Publisher> publishers = new ArrayList<Publisher>();
        for (String publishersClass : ExtensionHelper.findExtensions("publishers")) {
            if (publishersClass != null) {
                publishers.addAll(loadPublishers(publishersClass));
            }
        }
        return publishers;
    }

    private static List<Publisher> loadPublishers(String publishersClass) {
        try {
            Publishers publishers = (Publishers) Class.forName(publishersClass).newInstance();
            return publishers.definePublishers();
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("unable to load plugins class " + publishersClass, cnfe);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("unable to access plugins constructor for class " + publishersClass, iae);
        } catch (InstantiationException ie) {
            throw new RuntimeException("unable to instanciate plugins class " + publishersClass, ie);
        }
    }
}
