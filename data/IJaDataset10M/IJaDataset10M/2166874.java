package biz.flammable.waft.types;

public abstract class StringType extends AbstractType<String> implements Comparable<StringType> {

    public StringType(String value) throws TypeValueException {
        super(value);
    }

    public static String removeQuotes(final String arg) {
        if (arg == null) return null;
        if (arg.length() == 0) return arg;
        String result = arg;
        switch(result.charAt(0)) {
            case '"':
            case '\'':
                result = result.substring(1);
                break;
        }
        switch(result.charAt(result.length() - 1)) {
            case '"':
            case '\'':
                result = result.substring(0, result.length() - 1);
                break;
        }
        return result;
    }

    public int compareTo(StringType arg0) {
        return getValue().compareTo(arg0.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    protected String doTypeConversion(String value) {
        return value;
    }

    @Override
    public String toString() {
        return (String) value;
    }
}
