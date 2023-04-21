package x.sql2.interfaces;

public interface ISQLConverterHandler {

    /**
     * Serialising data from SQLTableRow Field to HashMap Value
     * @param columnName
     * @param value
     * @return
     */
    public Object serialise(String columnName, Object value);

    public Object deserialise(String mapKey, Object mapValue);
}
