package javango.forms.fields;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.google.inject.Inject;
import javango.forms.widgets.WidgetFactory;

public class MultipleLongChoiceField extends AbstractChoiceField<Long[], Long[]> {

    @Inject
    public MultipleLongChoiceField(WidgetFactory widgetFactory) {
        super(widgetFactory);
    }

    @Override
    public Long[] clean(String value, Map<String, String> errors) {
        return new Long[] { cleanOne(value, errors) };
    }

    protected Long cleanOne(String value, Map<String, String> errors) {
        if (StringUtils.isEmpty(value)) {
            if (isRequired()) {
                errors.put(getName(), REQUIRED_ERROR);
            } else if (isAllowNull()) {
                return null;
            }
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            errors.put(getName(), UNKNOWN_CHOICE_ERROR);
        }
        return null;
    }

    @Override
    public Long[] clean(String[] values, Map<String, String> errors) {
        Long[] clean = new Long[values.length];
        int i = 0;
        for (String val : values) {
            Long longValue = cleanOne(val, errors);
            if (!getChoices().containsKey(longValue)) {
                errors.put(this.name, "Select a valid choice.  That choice is not one of the available choices.");
            }
            clean[i++] = longValue;
        }
        return clean;
    }
}
