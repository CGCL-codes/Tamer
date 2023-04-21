package org.javalid.core.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.javalid.annotations.validation.value.MaxValueAnnotationValueImpl;
import org.javalid.core.JavalidException;
import org.javalid.core.ValidationMessage;

/**
 * This class is responsible for validating the MaxValue annotation.
 * Checks if given value is less or equal than the one specified at the annotation.
 * On error adds a ValidationMessage one of: MessageCodes.MSG_MAX_VALUE_ERROR or
 * MessageCodes.MSG_NOT_NUMERIC_ERROR.
 * <p>
 * ValidationMessage's value property contains currentValue,annotationValue.
 * <p>
 * <strong>Changes for 1.1:</strong>
 * <ul>
 *   <li>Removed ValidatorSupport instance, using its static methods instead</li>
 * </ul>
 * <strong>Changes for 1.2:</strong>
 * <ul>
 *   <li>Refactored: Extends AbstractJavalidValidator</li>
 * </ul>
 * <strong>Changes for 1.3:</strong>
 * <ul>
 *   <li>Changed to AnnotationValue instead of annotation</li>
 * </ul>
 * </p>
 * @author  M.Reuvers
 * @version 1.2
 * @since   1.0
 */
public class MaxValueValidatorImpl extends AbstractJavalidAnnotationValueValidatorImpl<MaxValueAnnotationValueImpl> {

    public MaxValueValidatorImpl() {
    }

    /**
   * @inheritDoc
   */
    @Override
    public boolean isSupportedDataType(Object value) {
        return ValidatorSupport.isNumericValue(value) || ValidatorSupport.isBigMathNumber(value);
    }

    /**
   * @inheritDoc
   */
    @Override
    public List<ValidationMessage> validate(MaxValueAnnotationValueImpl annotationValue, Object value, String path, JvConfigurationWrapper config) {
        List<ValidationMessage> list = new ArrayList<ValidationMessage>();
        double currentValue;
        try {
            if (ValidatorSupport.isNumericValue(value)) {
                currentValue = ValidatorSupport.getValueAsDouble(value);
                if (!ValidatorSupport.isEmptyString(annotationValue.getBigValue())) {
                    throw new JavalidException("@MaxValue validation failed, as bigValue property was used on a primitive method/field. Use value instead.");
                }
                if (value instanceof Float) {
                    BigDecimal left = new BigDecimal(((Float) value).toString());
                    BigDecimal right = new BigDecimal(String.valueOf(annotationValue.getValue()));
                    if (left.compareTo(right) > 0) {
                        String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCodeMaxValue()) ? MessageCodes.MSG_MAX_VALUE_ERROR : annotationValue.getCustomCodeMaxValue();
                        list.add(new ValidationMessage(path, messageCode, new Object[] { value, annotationValue.getValue() }, annotationValue.isGlobalMessage()));
                    }
                } else if (value instanceof Double) {
                    if (((Double) value).compareTo(new Double(annotationValue.getValue())) > 0) {
                        String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCodeMaxValue()) ? MessageCodes.MSG_MAX_VALUE_ERROR : annotationValue.getCustomCodeMaxValue();
                        list.add(new ValidationMessage(path, messageCode, new Object[] { value, annotationValue.getValue() }, annotationValue.isGlobalMessage()));
                    }
                } else {
                    if (currentValue > annotationValue.getValue()) {
                        String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCodeMaxValue()) ? MessageCodes.MSG_MAX_VALUE_ERROR : annotationValue.getCustomCodeMaxValue();
                        list.add(new ValidationMessage(path, messageCode, new Object[] { value, annotationValue.getValue() }, annotationValue.isGlobalMessage()));
                    }
                }
            } else if (ValidatorSupport.isBigMathNumber(value)) {
                String bigStr = annotationValue.getBigValue();
                if (ValidatorSupport.isEmptyString(bigStr)) {
                    throw new JavalidException("For @MaxValue the bigValue() property was empty while annotated on a BigInteger / BigDecimal, this is not allowed.");
                }
                if (value instanceof BigInteger) {
                    BigInteger bigAnnot = ValidatorSupport.getAsBigInteger(bigStr);
                    BigInteger bigValue = (BigInteger) value;
                    if (bigValue.compareTo(bigAnnot) > 0) {
                        String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCodeMaxValue()) ? MessageCodes.MSG_MAX_VALUE_ERROR : annotationValue.getCustomCodeMaxValue();
                        list.add(new ValidationMessage(path, messageCode, new Object[] { value, annotationValue.getValue() }, annotationValue.isGlobalMessage()));
                    }
                } else {
                    BigDecimal bigAnnot = ValidatorSupport.getAsBigDecimal(bigStr);
                    BigDecimal bigValue = (BigDecimal) value;
                    if (bigValue.compareTo(bigAnnot) > 0) {
                        String messageCode = ValidatorSupport.isEmptyString(annotationValue.getCustomCodeMaxValue()) ? MessageCodes.MSG_MAX_VALUE_ERROR : annotationValue.getCustomCodeMaxValue();
                        list.add(new ValidationMessage(path, messageCode, new Object[] { value, annotationValue.getValue() }, annotationValue.isGlobalMessage()));
                    }
                }
            } else {
                throw new JavalidException("Unsupported class for @MaxValue=" + value != null ? value.getClass().getName() : "null");
            }
        } catch (JavalidException e) {
            throw new JavalidException("@MaxValue validation failed, see detail stack why.", e);
        }
        return list;
    }
}
