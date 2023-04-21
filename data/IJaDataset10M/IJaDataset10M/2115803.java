package org.jia.ptrack.domain;

public class ProjectColumnType extends EnumeratedType {

    public static final ProjectColumnType NAME = new ProjectColumnType(0, "Name");

    public static final ProjectColumnType TYPE = new ProjectColumnType(10, "Type");

    public static final ProjectColumnType STATUS = new ProjectColumnType(20, "Status");

    public static final ProjectColumnType ROLE = new ProjectColumnType(30, "Role");

    private static EnumManager enumManager;

    static {
        enumManager = new EnumManager();
        enumManager.addInstance(NAME);
        enumManager.addInstance(TYPE);
        enumManager.addInstance(STATUS);
        enumManager.addInstance(ROLE);
    }

    public static EnumManager getEnumManager() {
        return enumManager;
    }

    private ProjectColumnType(int value, String description) {
        super(value, description);
    }
}
