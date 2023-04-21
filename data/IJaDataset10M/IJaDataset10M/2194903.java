package arbitration.logic.formatter;

/**
 *
 * @author prophet
 */
public interface TextFieldFormatter {

    public String valueToString(Object ob);

    public Object stringToValue(String str);
}
