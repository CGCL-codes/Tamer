package com.oroad.stxx.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 *  Defines a transform
 *
 *@author    Jeff Pennal, Don Brown
 */
public class ActionTransform implements Serializable, Cloneable {

    Map params = new HashMap();

    String type = "html";

    String selector = null;

    /**  Construct a new instance with default values. */
    public ActionTransform() {
    }

    /**
     *  Construct a new instance with the specified path.
     *
     *@deprecated As of 1.2
     *@param  path  Path for this instance
     */
    public ActionTransform(String path) {
        addParameter("path", path);
    }

    /**
     *  Construct a new instance with the specified path and debug flag.
     *
     *@deprecated As of 1.2
     *@param  path   Path for this instance
     *@param  debug  debug flag for this instance
     */
    public ActionTransform(String path, boolean debug) {
        addParameter("path", path);
        addParameter("debug", String.valueOf(debug));
    }

    /**
     *  Construct a new instance initializing every field
     *
     *@deprecated As of 1.2
     *@param  name      The name of the transform
     *@param  selector  The name of the selector
     *@param  paths     An array of XSL paths
     *@param  debug     Whether it should be in debugging mode
     *@param  render    Where to render the output
     *@param  type      The transform type to use
     */
    public ActionTransform(String name, ArrayList paths, String debug, String render, String type) {
        setName(name);
        setType(type);
        params.put("path", paths);
        addParameter("debug", String.valueOf(debug));
        addParameter("render", render);
    }

    /**
     *  Return the name.
     *
     *@deprecated As of v1.2v use {@link #getSelector} instead
     *@return    The name value
     */
    public String getName() {
        return (this.selector);
    }

    /**
     *  Set the name.
     *
     *@deprecated As of v1.2v use {@link #setSelector} instead
     *@param  name  The new name
     */
    public void setName(String name) {
        this.selector = name;
    }

    /**
     *  Return the selector value.
     *
     *@return    The selector value
     */
    public String getSelector() {
        return (this.selector);
    }

    /**
     *  Set the selector.
     *
     *@param  selector  The new selector
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    /**
     *  Return the path.
     *
     *@deprecated As of 1.2v use {@link #getParameter} instead
     *@return    The path value
     */
    public String getPath() {
        return getParameter("path");
    }

    /**
     *  Set the path.
     *
     *@deprecated As of 1.2v use {@link #addParameter} instead
     *@param  path  The new path
     */
    public void setPath(String path) {
        addParameter("path", path);
    }

    /**
     *  Return array of paths the path.
     *
     *@deprecated As of 1.2v use {@link #getParameterValues} instead
     *@return    The paths value
     */
    public List getPaths() {
        return getParameterValues("path");
    }

    /**
     *  Set the paths.
     *
     *@deprecated As of 1.2v use {@link #addParameter} instead
     *@param  paths  The new paths
     */
    public void setPaths(ArrayList paths) {
        params.put("path", paths);
    }

    /**
     *  Return the debug flag.
     *
     *@deprecated As of 1.2v use {@link #getParameter} instead
     *@return    The debug value
     */
    public boolean getDebug() {
        return "true".equals(getParameter("debug"));
    }

    /** 
     *  Sets the debug mode
     * 
     *@deprecated As of 1.2v use {@link #addParameter} instead
     */
    public void setDebug(boolean debug) {
        addParameter("debug", String.valueOf(debug));
    }

    /**
     *  Return the render flag.
     *   
     *@deprecated As of 1.2v use {@link #getParameter} instead
     *@return    The render value
     */
    public String getRender() {
        return getParameter("render");
    }

    /** 
     *  Sets where the transform should take place
     * 
     *@deprecated As of 1.2v use {@link #addParameter} instead
     */
    public void setRender(String render) {
        addParameter("render", render);
    }

    /**
     *  Gets a list of all available parameter names
     */
    public Collection getParameterNames() {
        return params.keySet();
    }

    /**
     * Gets a single parameter value if one exists
     *
     *@param name The name of the parameter
     *@return The value if one exists, otherwise null
     *@since 1.2
     */
    public String getParameter(String name) {
        List values = (List) params.get(name);
        if (values != null) {
            if (values.size() > 0) {
                return (String) values.get(0);
            }
        }
        return null;
    }

    /**
     *  Gets a list of parameter values
     *
     *@param name The name of the parameter
     *@return A list of values if the parameter exists, otherwise null
     *@since 1.2
     */
    public List getParameterValues(String name) {
        return (List) params.get(name);
    }

    /**
     *  Adds a parameter value
     *
     *@param name The name of the parameter
     *@param value The parameter value
     *@since 1.2
     */
    public void addParameter(String name, String value) {
        List list = (List) params.get(name);
        if (list == null) {
            list = new ArrayList();
            params.put(name, list);
        }
        list.add(value);
    }

    /**
     *  Return the type flag.
     *
     *@return    The type value
     */
    public String getType() {
        return (this.type);
    }

    /**
     *  Set the type flag.
     *
     *@param  type  The new type flag
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *  Sets the type
     * 
     *@deprecated As of v1.2, use {@link #setType} instead
     *@param type The new type
     */
    public void setMimeType(String type) {
        setType(type);
    }

    /**
     *  Return a String version of this mapping.
     *
     *@return    The debugging String
     */
    public String toString() {
        return ("ActionTransform[" + selector + "]");
    }

    /**
     *  Clones itself
     *
     */
    public Object clone() {
        try {
            ActionTransform trans = (ActionTransform) super.clone();
            trans.params = new HashMap();
            Map.Entry entry;
            ArrayList lst;
            for (Iterator i = this.params.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                lst = new ArrayList();
                lst.addAll((List) entry.getValue());
                trans.params.put(entry.getKey(), lst);
            }
            return trans;
        } catch (Exception ex) {
            throw new Error("Unable to clone ActionTransform: " + ex.toString());
        }
    }
}
