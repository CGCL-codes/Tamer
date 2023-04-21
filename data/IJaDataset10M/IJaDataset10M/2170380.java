package org.eclipse.emf.importer.rose.builder;

public class TableObject {

    private String name;

    private String quid;

    private UnitTreeNode container;

    private Object object;

    public TableObject(String name, String quid, UnitTreeNode unitTree) {
        this.name = name;
        this.quid = quid;
        container = unitTree;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQUID(String quid) {
        this.quid = quid;
    }

    public void setContainer(UnitTreeNode container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public String getQUID() {
        return quid;
    }

    public UnitTreeNode getContainer() {
        return container;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
