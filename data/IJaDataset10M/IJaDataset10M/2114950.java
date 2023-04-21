package mipt.gui.data.choice.table;

public class TableField {

    public String name;

    public Object header;

    /**
 * 
 */
    public TableField(String name, Object header) {
        this.name = name;
        this.header = header;
    }

    /**
 * 
 * @return java.lang.String
 */
    public String toString() {
        return name;
    }
}
