package architecture.ee.user.profile;

public class StringConverter extends SingleFieldMapper implements TypeConverter {

    public StringConverter() {
    }

    public String convertFromString(String s) throws TypeConverter.ConversionException {
        return s;
    }

    public String convertToString(String object) throws TypeConverter.ConversionException {
        return object;
    }

    public boolean objectIsConvertable(Object object) {
        return object instanceof String;
    }

    public boolean objectIsInitialized(String object) {
        return object != null && object.length() > 0;
    }

    public boolean stringIsConvertable(String string) {
        return true;
    }

    public String getValidationKey() {
        return "";
    }

    public String convertToString(Object obj) throws ConversionException {
        return convertToString((String) obj);
    }

    public boolean objectIsInitialized(Object obj) {
        return objectIsInitialized((String) obj);
    }
}
