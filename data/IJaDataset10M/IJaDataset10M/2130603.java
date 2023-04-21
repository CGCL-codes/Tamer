package com.byterefinery.rmbench.export.diff.db;

import com.byterefinery.rmbench.export.diff.IComparisonNode;
import com.byterefinery.rmbench.export.diff.StructureNode;
import com.byterefinery.rmbench.model.dbimport.DBSchema;

/**
 * a node that represents the database model as a whole
 * @author cse
 */
public class ModelNode extends StructureNode {

    public ModelNode(String name, DBSchema[] schemas) {
        super(name, null);
        SchemaNode[] schemaNodes = new SchemaNode[schemas.length];
        for (int i = 0; i < schemas.length; i++) {
            schemaNodes[i] = new SchemaNode(schemas[i]);
        }
        setChildNodes(schemaNodes);
    }

    public String getNodeType() {
        return IComparisonNode.MODEL;
    }
}
