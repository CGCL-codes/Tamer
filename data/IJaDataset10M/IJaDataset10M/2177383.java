package com.google.code.jqwicket.ui.validationengine;

import com.google.code.jqwicket.Utils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

/**
 * @author mkalina
 */
public class ValidationEngineFormComponentBehavior extends AttributeModifier {

    private static final long serialVersionUID = 1L;

    public ValidationEngineFormComponentBehavior(CharSequence ruleSequence) {
        super("class", new Model<String>(String.valueOf(ruleSequence)));
    }

    public ValidationEngineFormComponentBehavior(CharSequence... rules) {
        super("class", new Model<String>(String.valueOf(Utils.join(rules, ","))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String newValue(String currentValue, String replacementValue) {
        StringBuffer buf = new StringBuffer();
        if (Utils.isNotBlank(currentValue)) {
            buf.append(currentValue).append(" ");
        }
        buf.append("validate[").append(replacementValue).append("]");
        return buf.toString();
    }
}
