package sf.model.fields;

import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;

@Deprecated
public class DateField extends JFormattedTextField implements Field {

    private static final long serialVersionUID = 1L;

    public DateField(String name, int columns) {
        super(new SimpleDateFormat("dd.MM.yyyy"));
        setName(name);
        setColumns(columns);
    }

    public void clear() {
        setValue(null);
    }
}
