package lichen.validator;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.Validator;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.services.FormSupport;

/**
 * 对email的校验
 * @author jcai
 * @version $Revision: 225 $
 * @since 0.0.2
 */
public class Email implements Validator<Void, String> {

    public Class<Void> getConstraintType() {
        return null;
    }

    public String getMessageKey() {
        return "email";
    }

    public Class<String> getValueType() {
        return String.class;
    }

    public boolean invokeIfBlank() {
        return false;
    }

    public void render(Field field, Void constraintValue, MessageFormatter formatter, MarkupWriter writer, RenderSupport pageRenderSupport) {
    }

    private String buildMessage(MessageFormatter formatter, Field field) {
        return formatter.format(field.getLabel());
    }

    public void validate(Field field, Void constraintValue, MessageFormatter formatter, String value) throws ValidationException {
        if (!value.matches("\\w{1,}[@][\\w\\-]{1,}([.]([\\w\\-]{1,})){1,3}$")) {
            throw new ValidationException(buildMessage(formatter, field));
        }
    }

    public boolean isRequired() {
        return false;
    }

    public void render(Field field, Void constraintValue, MessageFormatter formatter, MarkupWriter writer, FormSupport formSupport) {
    }
}
