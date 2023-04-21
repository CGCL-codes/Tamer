package org.springframework.binding.convert.converters;

import org.springframework.core.enums.LabeledEnum;
import org.springframework.core.enums.LabeledEnumResolver;
import org.springframework.core.enums.StaticLabeledEnumResolver;

/**
 * Converts from a textual representation to a {@link LabeledEnum}. The text should be the enum's label.
 * 
 * @author Keith Donald
 */
public class StringToLabeledEnum extends StringToObject {

    private LabeledEnumResolver labeledEnumResolver = StaticLabeledEnumResolver.instance();

    public StringToLabeledEnum() {
        super(LabeledEnum.class);
    }

    protected Object toObject(String string, Class targetClass) throws Exception {
        return labeledEnumResolver.getLabeledEnumByLabel(targetClass, string);
    }

    protected String toString(Object object) throws Exception {
        LabeledEnum labeledEnum = (LabeledEnum) object;
        return labeledEnum.getLabel();
    }
}
