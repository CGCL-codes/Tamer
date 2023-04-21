package issrg.gt4.util;

/**
 *
 * @author ls97
 */
public class Attribute {

    public static int SUBJECT = 10;

    public static int ACTION = 11;

    public static int RESOURCE = 12;

    public static int ENVIRONMENT = 13;

    private String name;

    private String dataType;

    private int type;

    /** Creates a new instance of Attribute */
    public Attribute(String nameIn, String dataTypeIn, int typeIn) {
        this.dataType = dataTypeIn;
        this.name = nameIn;
        this.type = typeIn;
    }

    public String getName() {
        return this.name;
    }

    public String getDataType() {
        return this.dataType;
    }

    public int getType() {
        return this.type;
    }
}
