package net.sf.xc4j.gtl.meta;

import java.util.*;

/**
 *  Operator set declaration
 *
 * @author    cap
 * @since     0.0.1
 */
public class MContextDef extends MBase {

    /**
   *  name of field
   *
   * @since    0.0.1
   */
    private String name;

    /**
   *  Language of the definition
   *
   * @since    0.0.1
   */
    private MLanguage language;

    /**
   *  list of extented definitions
   *
   * @since    0.0.1
   */
    private final ArrayList extendedDefinitions = new ArrayList();

    /**
   *  unmodifiable list of extended definitions
   *
   * @since    0.0.1
   */
    private final List uextendedDefinitions = Collections.unmodifiableList(extendedDefinitions);

    /**
   *  collection of operators that are defined here
   *
   * @since    0.0.1
   */
    private final ArrayList operators = new ArrayList();

    /**
   *  unmodifiable collection of operators
   *
   * @since    0.0.1
   */
    private final List uoperators = Collections.unmodifiableList(operators);

    /**
   *  Gets operators from operator set
   *
   * @return    The Operators value
   * @since     0.0.1
   */
    public List getOperators() {
        return operators;
    }

    /**
   *  Add an operator
   *
   * @param  o  operator to add
   * @since     0.0.1
   */
    void addOperator(MOperator o) {
        operators.add(o);
    }

    /**
   *  Sets the Name attribute of the MContextDef object
   *
   * @param  nm  The new Name value
   * @since      0.0.1
   */
    public void setName(String nm) {
        name = nm;
    }

    /**
   *  Gets the ExtendedDefinitions attribute of the MContextDef object
   *
   * @return    The ExtendedDefinitions value
   * @since     0.0.1
   */
    public List getExtendedDefinitions() {
        return uextendedDefinitions;
    }

    /**
   *  Gets the Language attribute of the MContextDef object
   *
   * @return    The Language value
   * @since     0.0.1
   */
    public MLanguage getLanguage() {
        return language;
    }

    /**
   *  Gets the Name attribute of the MContextDef object
   *
   * @return    The Name value
   * @since     0.0.1
   */
    public String getName() {
        return name;
    }

    /**
   *  Adds a feature to the ExtendedDefinition attribute of the MContextDef
   *  object
   *
   * @param  d  a difinition to be added to the list of extneded definitions
   * @since     0.0.1
   */
    public void addExtendedDefinition(MContextDef d) {
        if (d == null) {
            throw new IllegalArgumentException("argument could not be null");
        }
        if (getClass() != d.getClass()) {
            throw new IllegalArgumentException("definition should have class " + getClass().getName() + "and is of class " + d.getClass().getName());
        }
        if (extendedDefinitions.contains(d)) {
            throw new IllegalStateException("definition " + getName() + " already extends definition " + d.getName());
        }
        extendedDefinitions.add(d);
    }

    /**
   *  Sets the Language attribute of the MContextDef object. This method should
   *  be only call from addDefinition() method on the MLanguage.
   *
   * @param  l  The new Language value
   * @since     0.0.1
   */
    void setLanguage(MLanguage l) {
        if (language != null) {
            throw new IllegalStateException("definition already belong to language: " + language.getName());
        }
        language = l;
    }
}
