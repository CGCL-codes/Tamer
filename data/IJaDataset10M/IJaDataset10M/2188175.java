package cn.vlabs.duckling.dct.services.plugin.impl.filter;

public class FilterColumn {

    private String path;

    private String name;

    private String type;

    private String headName;

    private boolean flag;

    public FilterColumn() {
    }

    public FilterColumn(int index) {
    }

    public FilterColumn(String key, String value) {
        buildFilterColumn(key, value);
    }

    public FilterColumn(String p, String n, String t) {
        path = p;
        name = n;
        type = t;
    }

    public void buildFilterColumnByIndex(int index) {
    }

    public void buildFilterColumn(String p, String src) {
        path = p;
        if (src.indexOf('(') == -1) {
            name = src;
            type = "VARCHAR(255)";
        } else {
            name = src.substring(0, src.indexOf('('));
            String subStr = src.substring(src.indexOf('(') + 1, src.length() - 1);
            String _array[] = subStr.split("#");
            headName = _array[0];
            type = _array[1];
            if (type.equals("Text")) {
                type = "VARCHAR(255)";
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String _path) {
        this.path = _path;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public String getType() {
        return type;
    }

    public void setType(String _type) {
        this.type = _type;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String _head) {
        this.headName = _head;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean _f) {
        this.flag = _f;
    }
}
