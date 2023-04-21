package it.simplerecords.migration;

public class Index {

    public static int ACTION_CREATE = 1;

    public static int ACTION_DROP = 2;

    private String name;

    private String tableName;

    private int action;

    public Index(String name) {
        this.name = name;
    }

    public Index(String name, String tableName) {
        this.name = name;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
