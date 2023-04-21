package net.sf.xc4j.gtl.meta;

import java.util.*;

/**
 *  an language declaration
 *
 * @author    cap
 * @since     0.0.1
 */
public class MLanguage extends MBase {

    /**
   *  map from name to definitions
   *
   * @since    0.0.1
   */
    private final HashMap name2def = new HashMap();

    /**
   *  Name of the language
   *
   * @since    0.0.1
   */
    private String name;

    /**
   *  Sets the Name attribute of the MLanguage object
   *
   * @param  nm  The new Name value
   * @since      0.0.1
   */
    public void setName(String nm) {
        name = nm;
    }

    /**
   * get contexts
   * @return collection of contexts
   */
    public Collection getContexts() {
        return name2def.values();
    }

    /**
   *  Gets the Name attribute of the MLanguage object
   *
   * @return    The Name value
   * @since     0.0.1
   */
    public String getName() {
        return name;
    }

    /**
   *  Gets the definition by name
   *
   * @param  nm  name of definition
   * @return     a definition or null if not found
   * @since      0.0.1
   */
    public MContextDef getContext(String nm) {
        return (MContextDef) name2def.get(nm);
    }

    /**
   *  Adds defintion to the language
   *
   * @param  d  definition to add
   * @since     0.0.1
   */
    public void addContext(MContextDef d) {
        if (d == null) {
            throw new IllegalArgumentException("argument could not be null");
        }
        if (d.getName() == null) {
            throw new IllegalArgumentException("name of definition must be specified");
        }
        if (name2def.get(d.getName()) != null) {
            throw new IllegalArgumentException("there already is an definition with name: " + d.getName());
        }
        name2def.put(d.getName(), d);
        d.setLanguage(this);
    }
}
