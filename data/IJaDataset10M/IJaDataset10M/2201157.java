package org.javalid.core.validator;

import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import org.javalid.annotations.validation.value.NotEmptyAllAnnotationValueImpl;
import org.javalid.core.JavalidException;
import org.javalid.core.ValidationMessage;
import org.javalid.core.el.ELSupport;
import org.javalid.core.support.PairTuple;

/**
 * This class is responsible for validating several values at once and they
 * must be all non-empty. It validates the NotEmptyAll annotation.
 * <p>
 * <strong>Changes for 1.3:</strong>
 * <ul>
 *   <li>Changed to AnnotationValue instead of annotation</li>
 * </ul>
 * </p>
 * @author  M.Reuvers
 * @version 1.0
 * @since   1.2
 */
public class NotEmptyAllValidatorImpl extends AbstractJavalidAnnotationValueValidatorImpl<NotEmptyAllAnnotationValueImpl> {

    public NotEmptyAllValidatorImpl() {
    }

    /**
   * @inheritDoc
   */
    @Override
    public List<ValidationMessage> validate(NotEmptyAllAnnotationValueImpl annotationValue, Object value, String path, JvConfigurationWrapper config) {
        List<ValidationMessage> list = new ArrayList<ValidationMessage>();
        String[] values = annotationValue.getValues();
        if (values.length == 0) {
            throw new JavalidException("Property values must at least contain 1 entry for @NotNullAll (currently none is set), currentPath=" + path);
        }
        PairTuple<ExpressionFactory, ELContext> expressionTuple = config.getExpressionDataWithClassLevelDefaultRegistered(value);
        Object[] resultValues = new Object[values.length];
        int index = 0;
        boolean valid = true;
        for (String val : values) {
            this.checkIsSingleExpression(val);
            resultValues[index] = ELSupport.resolveSingleExpression(expressionTuple.getOne(), expressionTuple.getTwo(), val, true);
            if (!ValidatorSupport.validateForNotEmptyAnnotation(resultValues[index], annotationValue.isTrim())) {
                valid = false;
            }
            index++;
        }
        if (!valid) {
            String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCode()) ? MessageCodes.MSG_NOT_EMPTY_ALL_ERROR : annotationValue.getCustomCode();
            list.add(new ValidationMessage(path, messageCode, new Object[] { value, resultValues }, annotationValue.isGlobalMessage()));
        }
        return list;
    }
}
