package org.apache.jetspeed.om.registry.base;

import org.apache.jetspeed.om.registry.*;
import java.util.*;

/**
 * The BaseSkinEntry is a bean like implementation of the SkinEntry
 * interface suitable for Castor XML serialization
 *
 * @see org.apache.jetspeed.om.registry.SkinEntry
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: BaseSkinEntry.java,v 1.4 2004/02/23 03:08:26 jford Exp $
 */
public class BaseSkinEntry extends BaseRegistryEntry implements SkinEntry {

    private Vector parameter = new Vector();

    private transient Map nameIdx = null;

    /**
     * Implements the equals operation so that 2 elements are equal if
     * all their member values are equal.
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        BaseSkinEntry obj = (BaseSkinEntry) object;
        Iterator i = parameter.iterator();
        Iterator i2 = obj.parameter.iterator();
        while (i.hasNext()) {
            BaseParameter c1 = (BaseParameter) i.next();
            BaseParameter c2 = null;
            if (i2.hasNext()) {
                c2 = (BaseParameter) i2.next();
            } else {
                return false;
            }
            if (!c1.equals(c2)) {
                return false;
            }
        }
        if (i2.hasNext()) {
            return false;
        }
        return super.equals(object);
    }

    /** @return an enumeration of this entry parameter names */
    public Iterator getParameterNames() {
        synchronized (parameter) {
            if (nameIdx == null) {
                buildNameIndex();
            }
        }
        return nameIdx.keySet().iterator();
    }

    /** Search for a named parameter and return the associated
     *  parameter object. The search is case sensitive.
     *
     *  @return the parameter object for a given parameter name
     *  @param name the parameter name to look for
     */
    public Parameter getParameter(String name) {
        synchronized (parameter) {
            if (nameIdx == null) {
                buildNameIndex();
            }
        }
        if (name != null) {
            Integer pos = (Integer) nameIdx.get(name);
            if (pos != null) {
                return (Parameter) parameter.elementAt(pos.intValue());
            }
        }
        return null;
    }

    /** Returns a map of parameter values keyed on the parameter names
     *  @return the parameter values map
     */
    public Map getParameterMap() {
        Hashtable params = new Hashtable();
        Enumeration en = parameter.elements();
        while (en.hasMoreElements()) {
            Parameter param = (Parameter) en.nextElement();
            params.put(param.getName(), param.getValue());
        }
        return params;
    }

    /** Adds a new parameter for this entry
     *  @param name the new parameter name
     *  @param value the new parameter value
     */
    public void addParameter(String name, String value) {
        if (name != null) {
            Parameter p = getParameter(name);
            if (p == null) {
                p = new BaseParameter();
                p.setName(name);
            }
            p.setValue(value);
            addParameter(p);
        }
    }

    /** Adds a new parameter for this entry
     *  @param parameter the new parameter to add
     */
    public void addParameter(Parameter param) {
        synchronized (parameter) {
            parameter.addElement(param);
            nameIdx.put(param.getName(), new Integer(parameter.size() - 1));
        }
    }

    /** Removes all parameter values associated with the
     *  name
     *
     * @param name the parameter name to remove
     */
    public void removeParameter(String name) {
        if (name == null) return;
        synchronized (parameter) {
            Iterator i = parameter.iterator();
            while (i.hasNext()) {
                Parameter param = (Parameter) i.next();
                if (param.getName().equals(name)) {
                    i.remove();
                }
            }
            buildNameIndex();
        }
    }

    /** This method recreates the paramter name index for quick retrieval
     *  of parameters by name. Shoule be called whenever a complete index
     *  of parameter should be rebuilt (eg removing a parameter or setting
     *  a parameters vector)
     */
    private void buildNameIndex() {
        Hashtable idx = new Hashtable();
        Iterator i = parameter.iterator();
        int count = 0;
        while (i.hasNext()) {
            Parameter p = (Parameter) i.next();
            idx.put(p.getName(), new Integer(count));
            count++;
        }
        this.nameIdx = idx;
    }

    /** Needed for Castor 0.8.11 XML serialization for retrieving the
     *  parameters objects associated to this object
     */
    public Vector getParameters() {
        return this.parameter;
    }

    public void setParameters(Vector parameters) {
        this.parameter = parameters;
    }
}
