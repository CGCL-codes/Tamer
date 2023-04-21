package cn.jsprun.vo.system;

public class TableStatusVO {

    private String name;

    private String engine;

    private String type;

    private String collation;

    private Long rows;

    private Long data_length;

    private Long index_length;

    private Long data_free;

    private String auto_increment;

    public String getAuto_increment() {
        return auto_increment;
    }

    public void setAuto_increment(String auto_increment) {
        this.auto_increment = auto_increment;
    }

    public Long getData_free() {
        return data_free;
    }

    public void setData_free(Long data_free) {
        this.data_free = data_free;
    }

    public Long getData_length() {
        return data_length;
    }

    public void setData_length(Long data_length) {
        this.data_length = data_length;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Long getIndex_length() {
        return index_length;
    }

    public void setIndex_length(Long index_length) {
        this.index_length = index_length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCollation() {
        return collation;
    }

    public void setCollation(String collation) {
        this.collation = collation;
    }
}
