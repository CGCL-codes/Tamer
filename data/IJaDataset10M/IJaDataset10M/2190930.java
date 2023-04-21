package datatypes;

/**
 *
 * @author EddyM
 */
public class UnknownData extends DataType<Object> {

    public static final String DATA_NAME = "Unknown Data Type";

    public static final double VERSION = 1;

    /** Creates a new instance of UnknownDataType */
    public UnknownData(Object data, String name) {
        super(DATA_NAME, name, VERSION, data);
    }
}
