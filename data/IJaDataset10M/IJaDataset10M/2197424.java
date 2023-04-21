package org.clico.swing.impl.view;

import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.clico.model.Required;
import org.clico.model.StringValidation;
import org.clico.model.TypeValidation;
import org.clico.spi.ViewStage;
import org.clico.swing.impl.SwingMVCImpl;
import org.clico.swing.view.Stage;
import org.clico.swing.view.ValidationFeedback;
import org.clico.swing.view.VisualClue;
import org.clico.swing.view.VisualFeedback;

public class ValidationStage extends ViewStage {

    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(ValidationStage.class);

    private final ValidationFeedback validationFeedback;

    public ValidationStage(ValidationFeedback validationFeedback) {
        this.validationFeedback = validationFeedback;
    }

    @Override
    public Stage getStage() {
        return Stage.VALIDATION;
    }

    @Override
    public void init(JComponent view, SwingMVCImpl mvc) {
        for (Field field : view.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(view);
                if (value instanceof JComponent && field.getAnnotations().length > 0) {
                    JComponent component = (JComponent) value;
                    Required required = field.getAnnotation(Required.class);
                    if (required != null && required.value()) {
                        validationFeedback.required(component);
                    }
                    StringValidation stringValidation = field.getAnnotation(StringValidation.class);
                    if (stringValidation != null) {
                        if (component instanceof JTextField) {
                            JTextField textField = (JTextField) component;
                            if (stringValidation.lengthLessThan() != Integer.MAX_VALUE) {
                                validationFeedback.lengthLessThan(stringValidation.lengthLessThan(), textField);
                            }
                            if (stringValidation.lengthMoreThan() != 0) {
                                validationFeedback.lengthMoreThan(stringValidation.lengthMoreThan(), textField);
                            }
                            if (!"*".equals(stringValidation.format())) {
                                validationFeedback.regexFormat(stringValidation.format(), textField);
                            }
                        } else {
                            log.warn("@StringValidation tag is defined for field " + field.getName() + " but field is not an instance of JTextField");
                        }
                    }
                    TypeValidation typeValidation = field.getAnnotation(TypeValidation.class);
                    if (typeValidation != null) {
                        if (component instanceof JFormattedTextField) {
                            JFormattedTextField textField = (JFormattedTextField) component;
                            validationFeedback.typeFormat(typeValidation.value(), textField);
                        } else {
                            log.warn("@TypeValidation tag is defined for field " + field.getName() + " but field is not an instance of JFormattedTextField");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        VisualFeedback visualFeedback = view.getClass().getAnnotation(VisualFeedback.class);
        if (visualFeedback != null) {
            for (int i = 0; i < visualFeedback.value().length; i++) {
                VisualClue visualClue = visualFeedback.value()[i];
                switch(visualClue) {
                    case MANDATORY_BACKGROUND:
                        validationFeedback.textComponentsHaveMandatoryBackground(view);
                        break;
                    default:
                        log.warn("Unsupported visual clue " + visualClue);
                }
            }
        }
    }
}
