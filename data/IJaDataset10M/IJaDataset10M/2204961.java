package com.xavax.jsf.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;

/**
 * BeanMultiMap is used to maintain a mapping of names to backing beans
 * and business objects. BeanMultiMap is implemented as a map of maps.
 * The first level map is indexed by the primary name. A map retreived
 * by the primary name is indexed by the secondary name. One example
 * application of BeanMultiMap is to store lists of counties indexed
 * first by state name and then by county name.
 */
public class BeanMultiMap {

    /**
   * Construct a BeanMap.
   */
    public BeanMultiMap() {
        this(null);
    }

    /**
   * Construct a BeanMultiMap with the specified comparators.
   *
   * @param comparator  the comparator for comparing names.
   */
    public BeanMultiMap(Comparator<? super Object> comparator) {
        mapByID = CollectionFactory.treeMap();
        mapByName = CollectionFactory.treeMap();
        this.comparator = comparator;
    }

    /**
   * Add an entry to the map.
   *
   * @param entry  the entry to be added.
   */
    public void add(BeanMultiMapEntry entry) {
        Object id = entry.getID();
        if (id != null) {
            mapByID.put(id, entry);
        }
        String name = entry.getPrimaryName();
        if (name != null) {
            Map<String, BeanMultiMapEntry> map = mapByName.get(name);
            if (map == null) {
                map = CollectionFactory.treeMap(comparator);
                mapByName.put(name, map);
            }
            String sname = entry.getSecondaryName();
            map.put(sname, entry);
        }
    }

    /**
   * Create an entry and add it to the map.
   *
   * @param id     the ID (primary key) for this entry.
   * @param name1  the primary name (sorting key) for this entry.
   * @param name2  the secondary name (sorting key) for this entry.
   * @param bo     the business object.
   * @param bean   the backing bean.
   */
    public void add(Object id, String name1, String name2, Object bo, Object bean) {
        BeanMultiMapEntry entry = new BeanMultiMapEntry(id, name1, name2, bo, bean);
        add(entry);
    }

    /**
   * Create an entry and add it to the map.
   *
   * @param id     the ID (primary key) for this entry.
   * @param name1  the primary name (sorting key) for this entry.
   * @param name2  the secondary name (sorting key) for this entry.
   * @param bo     the business object.
   * @param bean   the backing bean.
   */
    public void add(long id, String name1, String name2, Object bo, Object bean) {
        Long l = new Long(id);
        BeanMultiMapEntry entry = new BeanMultiMapEntry(l, name1, name2, bo, bean);
        add(entry);
    }

    /**
   * Associate a bean with the map entry with the specified ID.
   *
   * @param id  the ID to be associated.
   * @param bean  the bean to be associated.
   * @return true if the operation is successful.
   */
    public boolean attach(long id, Object bean) {
        Long l = new Long(id);
        return attach(l, bean);
    }

    /**
   * Associate a bean with the map entry with the specified ID.
   *
   * @param id  the ID to be associated.
   * @param bean  the bean to be associated.
   * @return true if the operation is successful.
   */
    public boolean attach(Object id, Object bean) {
        boolean result = false;
        if (id != null) {
            BeanMultiMapEntry entry = mapByID.get(id);
            if (entry != null) {
                entry.setBean(bean);
                result = true;
            }
        }
        return result;
    }

    /**
   * Remove all mappings from this map and reset the list of beans,
   * list of select items, and the list data model.
   */
    public void clear() {
        mapByID.clear();
        mapByName.clear();
    }

    /**
   * Disassociate a bean from the map entry with the specified ID.
   *
   * @param id  the ID to be disassociated.
   * @return true if the operation is successful.
   */
    public boolean detach(long id) {
        Long l = new Long(id);
        return detach(l);
    }

    /**
   * Disassociate a bean from the map entry with the specified ID.
   *
   * @param id  the ID to be disassociated.
   * @return true if the operation is successful.
   */
    public boolean detach(Object id) {
        boolean result = false;
        if (id != null) {
            BeanMultiMapEntry entry = mapByID.get(id);
            if (entry != null) {
                entry.setBean(null);
                result = true;
            }
        }
        return result;
    }

    /**
   * Returns the bean associated with the specified business object,
   * or null if no match was found.
   *
   * @return the bean associated with the specified business object.
   */
    public Object getBean(Object id) {
        Object result = null;
        BeanMultiMapEntry entry = mapByID.get(id);
        if (entry != null) {
            result = entry.getBean();
        }
        return result;
    }

    /**
   * Returns the bean associated with the specified business object,
   * or null if no match was found.
   *
   * @return the bean associated with the specified business object.
   */
    public Object getBean(long id) {
        Object l = new Long(id);
        return getBean(l);
    }

    /**
   * Returns business object with the specified ID, or null if no
   * match was found.
   *
   * @return the business object with the specified ID.
   */
    public Object getObject(Object id) {
        Object result = null;
        BeanMultiMapEntry entry = mapByID.get(id);
        if (entry != null) {
            result = entry.getObject();
        }
        return result;
    }

    /**
   * Returns business object with the specified ID, or null if no
   * match was found.
   *
   * @return the business object with the specified ID.
   */
    public Object getObject(long id) {
        Long l = new Long(id);
        return getObject(l);
    }

    /**
   * Returns a list of select items sorted by name to use in a JSF menu.
   * The keys of the first level map (mapByName) are used as both the
   * item label and value.
   *
   * @return a list of select items.
   */
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = CollectionFactory.arrayList();
        for (String s : mapByName.keySet()) {
            SelectItem item = new SelectItem(s, s);
            selectItems.add(item);
        }
        return selectItems;
    }

    /**
   * Returns a list of select items sorted by name to use in a JSF menu.
   * The primary name is used to retrieve a map. For each entry in the
   * map, the entry ID is used as the select item value and the entry
   * name is used as the select item label.
   *
   * @param  name  the primary name.
   * @return a list of select items.
   */
    public List<SelectItem> getSelectItems(String name) {
        List<SelectItem> selectItems = CollectionFactory.arrayList();
        if (name != null) {
            Map<String, BeanMultiMapEntry> map = mapByName.get(name);
            if (map != null) {
                for (BeanMultiMapEntry entry : map.values()) {
                    String sname = entry.getSecondaryName();
                    sname = sname != null ? sname : "";
                    Object id = entry.getID();
                    String sid = id.toString();
                    SelectItem item = new SelectItem(sid, sname);
                    selectItems.add(item);
                }
            }
        }
        return selectItems;
    }

    /**
   * Returns an iterator for the collection of map entries.
   *
   * @return an iterator for the collection of map entries.
   */
    public Iterator<BeanMultiMapEntry> iterator() {
        Collection<BeanMultiMapEntry> values = mapByID.values();
        return values.iterator();
    }

    /**
   * Remove an entry by ID (primary key).
   *
   * @param id  the ID of the entry to remove.
   * @return the removed entry, or null if no match was found.
   */
    public BeanMultiMapEntry remove(Object id) {
        BeanMultiMapEntry result = (BeanMultiMapEntry) mapByID.remove(id);
        if (result != null) {
            String key = result.getPrimaryName();
            Map<String, BeanMultiMapEntry> map = mapByName.get(key);
            if (map != null) {
                String sname = result.getSecondaryName();
                map.remove(sname);
            }
        }
        return result;
    }

    /**
   * Remove an entry by ID (primary key).
   *
   * @param id  the ID of the entry to remove.
   * @return the removed entry, or null if no match was found.
   */
    public BeanMapEntry remove(long id) {
        Long l = new Long(id);
        return remove(l);
    }

    /**
   * Remove an entry by name.
   *
   * @param name1  the primary name of the entry to remove.
   * @param name2  the secondary name of the entry to remove.
   * @return the removed entry, or null if no match was found.
   */
    public BeanMultiMapEntry removeByName(String name1, String name2) {
        BeanMultiMapEntry result = null;
        Map<String, BeanMultiMapEntry> map = mapByName.get(name1);
        if (map != null) {
            result = map.remove(name2);
            if (result != null) {
                Object id = result.getID();
                mapByID.remove(id);
            }
        }
        return result;
    }

    /**
   * Sets the bean associated with the specified ID.
   *
   * @param id    the id to be associated with the bean.
   * @param bean  the bean to be associated with the ID.
   * @return true if the operation is successful.
   */
    public boolean setBean(Object id, Object bean) {
        boolean result = false;
        BeanMultiMapEntry entry = (BeanMultiMapEntry) mapByID.get(id);
        if (entry != null) {
            entry.setBean(bean);
            result = true;
        }
        return result;
    }

    /**
   * Sets the bean associated with the specified ID.
   *
   * @param id    the id to be associated with the bean.
   * @param bean  the bean to be associated with the ID.
   * @return true if the operation is successful.
   */
    public boolean setBean(long id, Object bean) {
        Long l = new Long(id);
        return setBean(l, bean);
    }

    /**
   * Sets the business object associated with the specified ID.
   *
   * @param id  the id to be associated with the bean.
   * @param bo  the business object to be associated with the ID.
   * @return true if the operation is successful.
   */
    public boolean setObject(Object id, Object bo) {
        boolean result = false;
        BeanMultiMapEntry entry = mapByID.get(id);
        if (entry != null) {
            entry.setObject(bo);
        }
        return result;
    }

    /**
   * Sets the business object associated with the specified ID.
   *
   * @param id  the id to be associated with the bean.
   * @param bo  the business object to be associated with the ID.
   * @return true if the operation is successful.
   */
    public boolean setObject(long id, Object bo) {
        Long l = new Long(id);
        return setObject(l, bo);
    }

    /**
   * Returns the number of entries in this map.
   *
   * @return the number of entries in this map.
   */
    public int size() {
        return mapByID.size();
    }

    /**
   * Returns a string representation of this BeanMap.
   *
   * @return a string representation of this BeanMap.
   */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        boolean firstOuter = true;
        for (String key : mapByName.keySet()) {
            if (firstOuter) {
                firstOuter = false;
            } else {
                sb.append(", ");
            }
            Map<String, BeanMultiMapEntry> map = mapByName.get(key);
            sb.append(key).append(":{");
            boolean firstInner = true;
            for (BeanMultiMapEntry entry : map.values()) {
                if (firstInner) {
                    firstInner = false;
                } else {
                    sb.append(", ");
                }
                String s = entry.toString();
                sb.append(s);
            }
            sb.append("}");
        }
        String result = sb.toString();
        return result;
    }

    protected Comparator<? super Object> comparator;

    protected Map<Object, BeanMultiMapEntry> mapByID;

    protected Map<String, Map<String, BeanMultiMapEntry>> mapByName;
}
