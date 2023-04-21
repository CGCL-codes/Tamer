package org.objectstyle.cayenne.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.objectstyle.cayenne.access.DataDomain;
import org.objectstyle.cayenne.access.DataNode;
import org.objectstyle.cayenne.dba.AutoAdapter;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.util.Util;

/**
 * Save delegate used for saving Cayenne access stack.
 * 
 * @author Andrei Adamchik
 */
public class RuntimeSaveDelegate implements ConfigSaverDelegate {

    protected Configuration config;

    public RuntimeSaveDelegate(Configuration config) {
        this.config = config;
    }

    /**
     * Constructor for RuntimeSaveDelegate.
     */
    public RuntimeSaveDelegate() {
        super();
    }

    protected DataDomain findDomain(String domainName) {
        DataDomain domain = config.getDomain(domainName);
        if (domain == null) {
            throw new IllegalArgumentException("Can't find DataDomain: " + domainName);
        }
        return domain;
    }

    protected DataNode findNode(String domainName, String nodeName) {
        DataDomain domain = findDomain(domainName);
        DataNode node = domain.getNode(nodeName);
        if (node == null) {
            throw new IllegalArgumentException("Can't find DataNode: " + domainName + "." + nodeName);
        }
        return node;
    }

    /**
     * @since 1.1
     */
    public String projectVersion() {
        return config.getProjectVersion();
    }

    public Iterator domainNames() {
        Transformer tr = new Transformer() {

            public Object transform(Object input) {
                return ((DataDomain) input).getName();
            }
        };
        return new TransformIterator(config.getDomains().iterator(), tr);
    }

    /**
     * @since 1.1
     */
    public Iterator viewNames() {
        return config.getDataViewLocations().keySet().iterator();
    }

    /**
     * @since 1.1
     */
    public String viewLocation(String dataViewName) {
        return (String) config.getDataViewLocations().get(dataViewName);
    }

    public Iterator propertyNames(String domainName) {
        return findDomain(domainName).getProperties().keySet().iterator();
    }

    public String propertyValue(String domainName, String propertyName) {
        return (String) findDomain(domainName).getProperties().get(propertyName);
    }

    public String mapLocation(String domainName, String mapName) {
        return findDomain(domainName).getMap(mapName).getLocation();
    }

    public Iterator mapNames(String domainName) {
        List maps = new ArrayList(findDomain(domainName).getDataMaps());
        Collections.sort(maps, new Comparator() {

            public int compare(Object o1, Object o2) {
                String name1 = (o1 != null) ? ((DataMap) o1).getName() : null;
                String name2 = (o1 != null) ? ((DataMap) o2).getName() : null;
                return Util.nullSafeCompare(true, name1, name2);
            }
        });
        Transformer tr = new Transformer() {

            public Object transform(Object input) {
                return ((DataMap) input).getName();
            }
        };
        return new TransformIterator(maps.iterator(), tr);
    }

    public String nodeAdapterName(String domainName, String nodeName) {
        DbAdapter adapter = findNode(domainName, nodeName).getAdapter();
        return (adapter != null && adapter.getClass() != AutoAdapter.class) ? adapter.getClass().getName() : null;
    }

    public String nodeDataSourceName(String domainName, String nodeName) {
        return findNode(domainName, nodeName).getDataSourceLocation();
    }

    public String nodeFactoryName(String domainName, String nodeName) {
        return findNode(domainName, nodeName).getDataSourceFactory();
    }

    public Iterator nodeNames(String domainName) {
        Transformer tr = new Transformer() {

            public Object transform(Object input) {
                return ((DataNode) input).getName();
            }
        };
        List nodes = new ArrayList(findDomain(domainName).getDataNodes());
        Collections.sort(nodes, new Comparator() {

            public int compare(Object o1, Object o2) {
                String name1 = (o1 != null) ? ((DataNode) o1).getName() : null;
                String name2 = (o1 != null) ? ((DataNode) o2).getName() : null;
                return Util.nullSafeCompare(true, name1, name2);
            }
        });
        return new TransformIterator(nodes.iterator(), tr);
    }

    public Iterator linkedMapNames(String domainName, String nodeName) {
        Transformer tr = new Transformer() {

            public Object transform(Object input) {
                return ((DataMap) input).getName();
            }
        };
        Collection maps = findNode(domainName, nodeName).getDataMaps();
        return new TransformIterator(maps.iterator(), tr);
    }
}
