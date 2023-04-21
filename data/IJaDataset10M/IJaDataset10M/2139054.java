package jout.structure;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author shumi
 */
public class FTable {

    protected java.util.ArrayList<FieldData> __table;

    private CPTable __cpt;

    public FTable(CPTable cpt) {
        __cpt = cpt;
        __table = new java.util.ArrayList<FieldData>();
    }

    public DefaultMutableTreeNode getTreeNode() {
        DefaultMutableTreeNode tn = new DefaultMutableTreeNode("Fields Table");
        for (int i = 0; i < __table.size(); ++i) tn.add(__table.get(i).getTreeNode(i, __cpt));
        return tn;
    }

    public boolean add(FieldData field) {
        return __table.add(field);
    }

    public String getName(int index) throws Exception {
        ConstantData constant_data = __cpt.getElement(__table.get(index).name_ref);
        if (constant_data.getType() != CT.Utf8Info) throw new Exception("Field name reference is not of type Utf8Info!");
        return ((ConstData_Utf8Info) constant_data).getData();
    }

    public int getNameRef(int index) {
        return __table.get(index).name_ref;
    }

    public String getType(int index) throws Exception {
        ConstantData constant_data = __cpt.getElement(__table.get(index).type_ref);
        if (constant_data.getType() != CT.Utf8Info) throw new Exception("Field type reference is not of type Utf8Info!");
        return ((ConstData_Utf8Info) constant_data).getData();
    }

    public int getTypeRef(int index) {
        return __table.get(index).type_ref;
    }

    public int getAccessFlags(int index) {
        return __table.get(index).access_flags;
    }

    public FieldData getData(int index) {
        return __table.get(index);
    }

    public int getDataCount() {
        return __table.size();
    }

    FieldData set(int index, FieldData data) {
        return __table.set(index, data);
    }
}
