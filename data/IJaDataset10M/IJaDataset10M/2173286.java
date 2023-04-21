package com.esri.gpt.catalog.lucene;

import com.esri.gpt.framework.collection.StringSet;

/**
 * Represents the a virtual property associated with the execution of
 * a multi-field query parser.
 */
public class AnyTextProperty extends Storeable {

    /** instance variables ====================================================== */
    private StringSet namesToConsider = new StringSet();

    /**
   * Constructs with a supplied name and a set of field names to consider.
   * @param name the property name
   * @param namesToConsider the set of field names to consider
   */
    public AnyTextProperty(String name, StringSet namesToConsider) {
        super(name);
        this.namesToConsider = namesToConsider;
    }

    /**
   * Gets the array of field names to in include within the multi-field query.
   * the field names (can be null or empty if not properly configured)
   */
    public String[] getFieldNames() {
        if (namesToConsider != null) {
            return namesToConsider.toArray(new String[0]);
        } else {
            return null;
        }
    }
}
