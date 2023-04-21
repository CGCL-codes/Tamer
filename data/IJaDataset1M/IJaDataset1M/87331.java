package org.leviatan.definator.gui.swingmodels;

import javax.swing.tree.DefaultMutableTreeNode;
import org.leviatan.definator.core.model.Record;

public class RecordTreeNodeModel extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1450796989045032153L;

    private String id;

    private String name;

    private String tableRef;

    public RecordTreeNodeModel(Record recModel) {
        super();
        this.id = recModel.getId();
        this.name = recModel.getName();
        this.tableRef = recModel.getTableRef();
        this.setUserObject(this);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getTableRef() {
        return this.tableRef;
    }

    public void setId(String aId) {
        this.id = aId;
    }

    public void setName(String aName) {
        this.name = aName;
    }

    public void setTableRef(String aId) {
        this.tableRef = aId;
    }

    public String toString() {
        return "[rec] " + this.name;
    }
}
