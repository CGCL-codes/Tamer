package test.hudson.zipscript.model;

public class ColumnInfo {

    private String title;

    private int size;

    private String type;

    private String propertyName;

    public ColumnInfo(String title, String propertyName, int size, String type) {
        this.title = title;
        this.size = size;
        this.propertyName = propertyName;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
