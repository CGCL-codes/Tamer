package com.jvito.parameter;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.jvito.JViTo;
import com.rapidminer.parameter.ParameterType;

/**
 * This class is constructed to build Objects with Parameters. These Parameters are hold as a Collection. This class
 * also wraps this collection with get and set-Methods.
 * 
 * @author Daniel Hakenjos
 * @version $Id: ParameterObject.java,v 1.3 2008/04/12 14:28:12 djhacker Exp $
 */
public abstract class ParameterObject {

    protected JViToParameters parameters;

    private String[] keys;

    private String description = new String("");

    /**
	 * Constructs new ParameterObject.
	 */
    public ParameterObject() {
        super();
        initParameters();
    }

    /**
	 * Inits the Parameters.
	 * 
	 */
    public void initParameters() {
        List<ParameterType> list = getParameterTypes();
        parameters = new JViToParameters(list);
        keys = new String[list.size()];
        int index = 0;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            keys[index] = new String(((ParameterType) iter.next()).getKey());
            index++;
        }
    }

    /** Returns a collection of all parameters of this operator. */
    public JViToParameters getParameters() {
        return parameters;
    }

    /**
	 * Returns a single parameter retrieved from the {@link JViToParameters}of this Operator.
	 */
    public Object getParameter(String key) {
        return parameters.getParameter(key);
    }

    /**
	 * Returns a single parameter retrieved from the {@link JViToParameters}of this Operator.
	 */
    public Object getParameter(int index) {
        if (index >= countParameterTypes()) return null;
        return getParameter(keys[index]);
    }

    /** Returns true iff the parameter with the given name is set. */
    public boolean isParameterSet(String key) {
        return getParameter(key) != null;
    }

    /** Returns a single named parameter and casts it to String. */
    public String getParameterAsString(String key) {
        return (String) getParameter(key);
    }

    /** Returns a single named parameter and casts it to int. */
    public int getParameterAsInt(String key) {
        return ((Integer) getParameter(key)).intValue();
    }

    /** Returns a single named parameter and casts it to double. */
    public double getParameterAsDouble(String key) {
        return ((Double) getParameter(key)).doubleValue();
    }

    /** Returns a single named parameter and casts it to boolean. */
    public boolean getParameterAsBoolean(String key) {
        return ((Boolean) getParameter(key)).booleanValue();
    }

    /** Returns a single named parameter and casts it to List. */
    public List getParameterList(String key) {
        return (List) getParameter(key);
    }

    /**
	 * Returns a single named parameter and casts it to File. This file is already resolved against the experiment.
	 */
    public File getParameterAsFile(String key) {
        String fileName = getParameterAsString(key);
        return new File(fileName);
    }

    public Color getParameterAsColor(String key) {
        return (Color) getParameter(key);
    }

    /**
	 * Sets a Parameter with this key and object.
	 * 
	 * @param key
	 * @param object
	 */
    public void setParameter(String key, Object object) {
        this.parameters.setParameter(key, object);
        JViTo.getApplication().setDirty();
    }

    /**
	 * Sets a Parameter at the index with the object.
	 * 
	 * @param index
	 * @param object
	 */
    public void setParameter(int index, Object object) {
        if (index >= countParameterTypes()) return;
        setParameter(keys[index], object);
        JViTo.getApplication().setDirty();
    }

    /**
	 * Returns a list of <tt>ParameterTypes</tt> describing the parameters of this operator. The default
	 * implementation returns an empty list.
	 */
    public List<ParameterType> getParameterTypes() {
        return new ArrayList<ParameterType>();
    }

    /**
	 * Numbers of <code>ParameterType</code>s.
	 */
    public int countParameterTypes() {
        return parameters.getKeys().size();
    }

    /**
	 * Gets a <code>ParameterType</code> by the index.
	 * 
	 * @param index
	 */
    public ParameterType getParameterType(int index) {
        if (index >= countParameterTypes()) return null;
        return parameters.getParameterType(keys[index]);
    }

    /**
	 * Gets a <code>ParameterType</code> by the key.
	 * 
	 * @param key
	 */
    public ParameterType getParameterType(String key) {
        return parameters.getParameterType(key);
    }

    /**
	 * Gets the key at the index.
	 * 
	 * @param index
	 */
    public String getKey(int index) {
        return keys[index];
    }

    /**
	 * Gets the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description.
	 * 
	 * @param description
	 */
    public void setDescription(String description) {
        this.description = description;
        JViTo.getApplication().setDirty();
    }
}
