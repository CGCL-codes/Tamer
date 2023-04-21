package uncertain.datatype;

public abstract class AbstractDataType implements DataType {

    public Object convert(Object value, String format) throws ConvertionException {
        return convert(value);
    }
}
