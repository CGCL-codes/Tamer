package ru.spb.leti.g6351.kinpo.adressbook.validators;

import javax.swing.text.JTextComponent;
import ru.spb.leti.g6351.kinpo.common.AbstractFocusLostValidator;

/**
 * Реализация валидатора по событию потери фокуса для URL. Автодополнение адреса префиксом http:// производится автоматически при отсутствии оного.
 * @author nikita
 */
public class UrlValidator extends AbstractFocusLostValidator {

    public UrlValidator(JTextComponent field) {
        super(field);
        field.setText("");
    }

    @Override
    protected boolean isValidInternal() {
        org.apache.commons.validator.UrlValidator validator = new org.apache.commons.validator.UrlValidator();
        return validator.isValid(getText());
    }

    @Override
    protected void validateInternal() {
        String text = getText();
        if (text.isEmpty()) return;
        if (text.startsWith("http://") || text.startsWith("https://")) return; else {
            getComponent().setText("http://".concat(text));
            getComponent().validate();
        }
    }

    @Override
    protected String getErrorTextInternal() {
        return "Адрес " + getText() + " имеет неправильный формат!";
    }

    @Override
    protected void fireErrorReactionInternal(String errorText) {
        getComponent().setText("");
    }
}
