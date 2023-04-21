package taskgraph.reflect;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Property editor for java.util.Date values.
 * 
 * @author Armando Blancas
 */
public class DateEditor extends PropertyEditorSupport {

    private DateFormat df;

    public DateEditor() {
        df = new SimpleDateFormat("MM/dd/yyyy");
    }

    @Override
    public String getAsText() {
        return df.format(getValue());
    }

    @Override
    public String getJavaInitializationString() {
        return ("((Date)" + getValue() + ")");
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(df.parse(text));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
