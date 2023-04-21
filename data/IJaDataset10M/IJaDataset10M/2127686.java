package org.javalid.core.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.javalid.annotations.validation.AnnotationConstants;
import org.javalid.annotations.validation.DateCheck;
import org.javalid.annotations.validation.value.DateCheckAnnotationValueImpl;
import org.javalid.core.JavalidException;
import org.javalid.core.ValidationMessage;

/**
 * Validator implementation for the @DateCheck annotation
 * <p>
 * <strong>Changes for 1.1:</strong>
 * <ul>
 *   <li>Refactored: Extends AbstractJavalidValidator</li>
 * </ul>
 * <strong>Changes for 1.3:</strong>
 * <ul>
 *   <li>Changed to AnnotationValue instead of annotation</li>
 * </ul>
 * </p>
 * @author  M.Reuvers
 * @version 1.1
 * @since   1.1
 */
public class DateCheckValidatorImpl extends AbstractJavalidAnnotationValueValidatorImpl<DateCheckAnnotationValueImpl> {

    public DateCheckValidatorImpl() {
    }

    /**
   * @inheritDoc
   */
    @Override
    public boolean isSupportedDataType(Object value) {
        return value instanceof Date;
    }

    /**
   * @inheritDoc
   */
    @Override
    public List<ValidationMessage> validate(DateCheckAnnotationValueImpl annotationValue, Object value, String path, JvConfigurationWrapper config) {
        if (!(value instanceof Date)) {
            throw new JavalidException("@DateCheck must be annotated on java.util.Date (or subclasses), current validationPath=" + path);
        }
        List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
        Date currentValue = (Date) value;
        int mode = annotationValue.getMode();
        int convertType = annotationValue.getConvertType();
        String strValue = annotationValue.getValue();
        String pattern = annotationValue.getPattern();
        boolean today = annotationValue.isToday();
        Date annotConvertedValue = null;
        Calendar calAnnotation = null;
        Calendar calCurrentValue = null;
        if (ValidatorSupport.isEmptyString(pattern)) {
            throw new JavalidException("Pattern cannot be empty, using a pattern as used in java.util.SimpleDateFormatter. Current validationPath=" + path);
        }
        if (convertType != DateCheck.TYPE_CONVERT_DATE && convertType != DateCheck.TYPE_CONVERT_FULL && convertType != DateCheck.TYPE_CONVERT_MILLIS) {
            throw new JavalidException("convertType() has an invalid value, use one of the constants in DateCheck, starting with TYPE_. convertType=" + convertType + ", current validationPath=" + path);
        }
        if (today) {
            annotConvertedValue = new Date();
        } else {
            if (ValidatorSupport.isEmptyString(strValue)) {
                throw new JavalidException("When today() = false, you must specify value() of the annotation. Current validationPath=" + path);
            }
            if (mode != DateCheck.MODE_EQUALS && mode != DateCheck.MODE_EQUALS_LESS_THAN && mode != DateCheck.MODE_EQUALS_MORE_THAN && mode != DateCheck.MODE_LESS_THAN && mode != DateCheck.MODE_MORE_THAN && mode != DateCheck.MODE_NOT_EQUALS) {
                throw new JavalidException("mode() has an invalid value, use one of the constants in DateCheck, starting with MODE_. mode=" + mode + ", current validationPath=" + path);
            }
            annotConvertedValue = ValidatorSupport.convertStringToDate(strValue, pattern);
        }
        calAnnotation = ValidatorSupport.convertDateToCalendar(annotConvertedValue, convertType);
        calCurrentValue = ValidatorSupport.convertDateToCalendar(currentValue, convertType);
        ValidatorSupport.compareCalendars(AnnotationConstants.ANNOTATION_DATE_DATE_CHECK, pattern, annotationValue.getCustomCode(), annotationValue.isGlobalMessage(), calAnnotation, calCurrentValue, mode, messages, path);
        return messages;
    }
}
