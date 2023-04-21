package druid.util.velocity.nodes;

import druid.core.DataLib;
import druid.core.DataTypeLib;
import druid.core.DruidException;
import druid.data.AbstractNode;
import druid.data.FieldNode;
import druid.data.TableNode;
import druid.util.decoder.OnClauseDecoder;
import druid.util.velocity.sets.FieldAttribsV;

public class FieldNodeV extends AbstractNodeV {

    private OnClauseDecoder onClause = new OnClauseDecoder();

    public FieldNodeV(AbstractNode node) {
        super(node);
    }

    public String getType() {
        return DataTypeLib.getTypeDef(getFieldNode());
    }

    public String getSqlType() {
        return DataTypeLib.getSqlType(getFieldNode());
    }

    public TableNodeV getForeignTable() {
        int refTable = as.getInt("refTable");
        TableNode refNode = node.getDatabase().getTableByID(refTable);
        return (TableNodeV) convertNode(refNode);
    }

    public FieldNodeV getForeignField() {
        int refTable = as.getInt("refTable");
        int refField = as.getInt("refField");
        TableNode table = node.getDatabase().getTableByID(refTable);
        FieldNode field = table.getFieldByID(refField);
        return (FieldNodeV) convertNode(field);
    }

    public FieldAttribV getFieldAttribVal(FieldAttribsV attr) {
        Object obj = getFieldNode().fieldAttribs.getData("" + attr.getId());
        if (obj == null) throw new DruidException(DruidException.INC_STR, "Obj is null !!!");
        return new FieldAttribV(obj);
    }

    public boolean getIsPrimaryKey() {
        return DataLib.isPrimaryKey(getFieldNode());
    }

    public boolean getIsNotNull() {
        return DataLib.isNotNull(getFieldNode());
    }

    public boolean getIsUnique() {
        return DataLib.isUnique(getFieldNode());
    }

    public boolean getIsForeignKey() {
        return getFieldNode().isFkey();
    }

    public String getOnUpdateAction() {
        return onClause.decode(as.getString("onUpdate"));
    }

    public String getOnDeleteAction() {
        return onClause.decode(as.getString("onDelete"));
    }

    private FieldNode getFieldNode() {
        return (FieldNode) node;
    }
}
