package net.sourceforge.gendo.attributes;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This obect contains information about AttributeMap. AttributeMap are defined
 * in Groups (usually three), and this is maintained in the datastructure.
 * 
 * @author Jo-Herman Haugholt
 * @version 1.0
 */
public class AttributeMap implements Map {

    private LinkedHashMap attributes = new LinkedHashMap();

    /**
	 * Add an Atandard Attribute Group to the list of attributes.
	 * 
	 * @param name
	 *            The name of the attribute group. E.g. 'Body'
	 * @param power
	 *            The power attribute. E.g. 'Strength'
	 * @param aptitude
	 *            The aptitude attribute. E.g. 'Reflexes'
	 * @param resistance
	 *            The Resistance attribute. E.g. 'Health'
	 */
    public void addAttributeGroup(String name, Attribute power, Attribute aptitude, Attribute resistance) {
        StandardAttributeGroup ag = new StandardAttributeGroup(name, power, aptitude, resistance);
        attributes.put(ag.getName(), ag);
    }

    /**
	 * Adds an custom Attribute Group, defined by a Collection
	 * 
	 * @param name
	 *            Name of the attribute group. E.g. 'Body'
	 * @param c
	 *            Collection with the attributes
	 */
    public void addCustomAttributeGroup(String name, Collection c) {
        CustomSizeAttributeGroup cag = new CustomSizeAttributeGroup(name, c);
        attributes.put(cag.getName(), cag);
    }

    /**
	 * Adds an custom Attribute Group, defined by an array
	 * 
	 * @param name
	 *            Name of the attribute group. E.g. 'Body'
	 * @param attributes
	 *            an array containing the attributes
	 */
    public void addCustomAttributeGroup(String name, Attribute[] attributes) {
        CustomSizeAttributeGroup cag = new CustomSizeAttributeGroup(name, attributes);
        this.attributes.put(cag.getName(), cag);
    }

    /**
	 * Gets an attribute by a given short name. If it don't exist, this method
	 * returns null.
	 * 
	 * @param shortName
	 *            short name of attribute to get
	 * @return attribute if it exists, null if not
	 */
    public Attribute get(String shortName) {
        Iterator i = attributes.values().iterator();
        while (i.hasNext()) {
            AttributeGroup ag = (AttributeGroup) i.next();
            Attribute a = ag.get(shortName);
            if (a != null) {
                return a;
            }
        }
        return null;
    }

    /**
	 * Gets an Collection of the attribute groups
	 * 
	 * @return collection of attribute groups
	 */
    public Collection getAttributeGroups() {
        return this.attributes.values();
    }

    /**
	 * Gets an Collection of all the attributes defined
	 * 
	 * @return Collection with all attributes
	 */
    public Collection getAttributes() {
        Vector v = new Vector();
        Iterator i = getAttributeGroups().iterator();
        while (i.hasNext()) {
            AttributeGroup ag = (AttributeGroup) i.next();
            v.addAll(ag.getAttributes().values());
        }
        return v;
    }

    /**
	 * Returns an compiled Map containing all the attributes
	 * @return map containing all attributes
	 */
    public Map getCompiledAttributeMap() {
        LinkedHashMap map = new LinkedHashMap();
        Iterator i = getAttributeGroups().iterator();
        while (i.hasNext()) {
            AttributeGroup ag = (AttributeGroup) i.next();
            map.putAll(ag.getAttributes());
        }
        return map;
    }

    /**
	 * This method returns the number of Attribute Groups
	 * 
	 * @return number of attribute groups
	 */
    public int size() {
        return this.attributes.size();
    }

    /**
	 * This method returns the total number of Attributes
	 * 
	 * @return total number of attributes
	 */
    public int numberOfAttributes() {
        return getAttributes().size();
    }

    public void clear() {
        this.attributes.clear();
    }

    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    public boolean containsKey(Object arg0) {
        return this.attributes.containsKey(arg0);
    }

    public boolean containsValue(Object arg0) {
        return this.attributes.containsValue(arg0);
    }

    public Collection values() {
        return getAttributeGroups();
    }

    public void putAll(Map arg0) {
        this.attributes.putAll(arg0);
    }

    public Set entrySet() {
        return this.attributes.entrySet();
    }

    public Set keySet() {
        return this.attributes.keySet();
    }

    public Object get(Object arg0) {
        return this.attributes.get(arg0);
    }

    public Object remove(Object arg0) {
        return this.attributes.remove(arg0);
    }

    public Object put(Object arg0, Object arg1) {
        return this.put(arg0, arg1);
    }
}
