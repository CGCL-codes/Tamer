package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * This class encapsulates all the code generation rules for a table using the
 * hierarchical model.
 * 
 * @author Jeff Butler
 * 
 */
public class HierarchicalModelRules extends BaseRules {

    /**
     * 
     */
    public HierarchicalModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }

    /**
     * Implements the rule for determining whether to generate a primary key
     * class. If the physical table has a primary key, then we generate the
     * class.
     * 
     * @return true if the primary key should be generated
     */
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.hasPrimaryKeyColumns();
    }

    /**
     * Implements the rule for generating a base record. If the table has fields
     * that are not in the primary key, and non-BLOB fields, then generate the
     * class.
     * 
     * @return true if the class should be generated
     */
    public boolean generateBaseRecordClass() {
        return introspectedTable.hasBaseColumns();
    }

    /**
     * Implements the rule for generating a record with BLOBs. A record with
     * BLOBs is generated if the table contains any BLOB fields.
     * 
     * @return true if the record with BLOBs class should be generated
     */
    public boolean generateRecordWithBLOBsClass() {
        return introspectedTable.hasBLOBColumns();
    }
}
