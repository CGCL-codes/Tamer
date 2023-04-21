package ch.epfl.lbd.database.olap;

import ch.epfl.lbd.database.objects.DatabaseObject;

public abstract class Dimension extends DatabaseObject {

    public static final long serialVersionUID = 0x98329;

    protected Hierarchy hierarchy;

    public Dimension(String name, Hierarchy hierarchy) {
        this.name = name;
        if (hierarchy == null) return;
        this.hierarchy = hierarchy;
    }

    public String getTableName() {
        return hierarchy.getTableName();
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }
}
