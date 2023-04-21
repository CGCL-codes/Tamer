package org.pojosoft.core.persistence;

/**
 * Typesafe enums used to specify the type of merge to use when persisting an object
 *
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class MergeType {

    private final String type;

    private MergeType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }

    /**
   * Apply on the changes to the object and its privately owned parts to the database
   */
    public static final MergeType MERGE = new MergeType("merge");

    /**
   * Apply on the changes to the object to the database
   */
    public static final MergeType SHALLOW_MERGE = new MergeType("shallowMerge");

    /**
   * Apply on the changes to the object and all of its references to the database
   */
    public static final MergeType MERGE_WITH_REFERENCES = new MergeType("mergeWithReferences");

    /**
   * Apply on the changes to the object and everything connected to it to the database
   */
    public static final MergeType DEEP_MERGE = new MergeType("deepMerge");
}
