package javango.forms.widgets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javango.forms.fields.DateField;

public class TimeWidget extends Input {

    public final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm");

    public TimeWidget() {
        super();
    }

    public TimeWidget(Map<String, Object> attrs) {
        super(attrs);
    }

    @Override
    public String getInputType() {
        return "text";
    }

    @Override
    public String render(String name, Object value, Map<String, Object> attrs) {
        if (value instanceof Date) {
            value = value == null ? null : DATE_FORMATTER.format((Date) value);
        }
        return super.render(name, value, attrs);
    }
}
