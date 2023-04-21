package org.thymeleaf.standard.processor.value;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.ValueProcessorException;
import org.thymeleaf.processor.value.IValueProcessor;
import org.thymeleaf.standard.processor.value.link.IStandardLinkValueProcessor;
import org.thymeleaf.standard.processor.value.literal.IStandardLiteralValueProcessor;
import org.thymeleaf.standard.processor.value.message.IStandardMessageValueProcessor;
import org.thymeleaf.standard.processor.value.variable.IStandardVariableValueProcessor;
import org.thymeleaf.standard.syntax.StandardSyntax.ConditionalValue;
import org.thymeleaf.standard.syntax.StandardSyntax.DefaultValue;
import org.thymeleaf.standard.syntax.StandardSyntax.LinkValue;
import org.thymeleaf.standard.syntax.StandardSyntax.LiteralValue;
import org.thymeleaf.standard.syntax.StandardSyntax.MsgValue;
import org.thymeleaf.standard.syntax.StandardSyntax.TokenValue;
import org.thymeleaf.standard.syntax.StandardSyntax.Value;
import org.thymeleaf.standard.syntax.StandardSyntax.VarValue;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.ObjectUtils;
import org.thymeleaf.util.Validate;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class StandardValueProcessor implements IStandardValueProcessor {

    @SuppressWarnings("unchecked")
    private static Set<Class<? extends IValueProcessor>> DEPENDENCIES = Collections.unmodifiableSet(new HashSet<Class<? extends IValueProcessor>>((List<Class<? extends IValueProcessor>>) (List<?>) Arrays.asList(new Class<?>[] { IStandardLiteralValueProcessor.class, IStandardVariableValueProcessor.class, IStandardMessageValueProcessor.class, IStandardLinkValueProcessor.class })));

    public StandardValueProcessor() {
        super();
    }

    public String getName() {
        return this.getClass().getName();
    }

    public Set<Class<? extends IValueProcessor>> getValueProcessorDependencies() {
        return DEPENDENCIES;
    }

    public Object getValue(final Arguments arguments, final TemplateResolution templateResolution, final Value value) {
        Validate.notNull(arguments, "Arguments cannot be null");
        Validate.notNull(templateResolution, "Template Resolution cannot be null");
        Validate.notNull(value, "Value cannot be null");
        switch(value.getType()) {
            case TYPE_TOKEN:
                return ((TokenValue) value).getValue();
            case TYPE_LITERAL:
                final IStandardLiteralValueProcessor literalProcessor = arguments.getConfiguration().getValueProcessorByClass(this, IStandardLiteralValueProcessor.class);
                return literalProcessor.getLiteralValue(arguments, templateResolution, (LiteralValue) value);
            case TYPE_VAR:
                final IStandardVariableValueProcessor varProcessor = arguments.getConfiguration().getValueProcessorByClass(this, IStandardVariableValueProcessor.class);
                return varProcessor.getVariableValue(arguments, templateResolution, (VarValue) value);
            case TYPE_MSG:
                final IStandardMessageValueProcessor msgProcessor = arguments.getConfiguration().getValueProcessorByClass(this, IStandardMessageValueProcessor.class);
                return msgProcessor.getMessageValue(arguments, templateResolution, (MsgValue) value);
            case TYPE_LINK:
                final IStandardLinkValueProcessor linkProcessor = arguments.getConfiguration().getValueProcessorByClass(this, IStandardLinkValueProcessor.class);
                return linkProcessor.getLinkValue(arguments, templateResolution, (LinkValue) value);
            case TYPE_CONDITION:
                return evaluateCondition(arguments, templateResolution, (ConditionalValue) value);
            case TYPE_DEFAULT:
                return evaluateDefault(arguments, templateResolution, (DefaultValue) value);
        }
        throw new ValueProcessorException("No value processors registered for " + value.getClass().getName());
    }

    private Object evaluateCondition(final Arguments arguments, final TemplateResolution templateResolution, final ConditionalValue value) {
        final Object condObj = getValue(arguments, templateResolution, value.getConditionValue());
        final boolean cond = ObjectUtils.evaluateCondition(condObj);
        if (cond) {
            return getValue(arguments, templateResolution, value.getThenValue());
        }
        return getValue(arguments, templateResolution, value.getElseValue());
    }

    private Object evaluateDefault(final Arguments arguments, final TemplateResolution templateResolution, final DefaultValue value) {
        final Object valueObj = getValue(arguments, templateResolution, value.getValue());
        if (valueObj == null) {
            return getValue(arguments, templateResolution, value.getDefaultValue());
        }
        return valueObj;
    }
}
