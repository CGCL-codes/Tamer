package org.javalid.external.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.javalid.core.AnnotationValidator;
import org.javalid.core.JavalidException;
import org.javalid.core.ValidationMessage;
import org.javalid.core.ValidatorParams;
import org.javalid.core.support.ReflectionSupport;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * SpringValidator which implements the Validator interface.
 * You can inject this bean directly into your MVC controllers.
 * Configure the validator through your spring configuration file.
 * <p>
 * Use the validatorMap to define what class is supported to be validated
 * and how. For complex validation and/or different type of validations for
 * a single class, you should just use the AnnotationValidator directly
 * and use SpringMessageConverter to convert the messages to Spring Errors.
 * @author  M.Reuvers
 * @version 1.0
 * @since   1.0
 */
public class SpringValidator implements Validator {

    private static final Logger logger = Logger.getLogger(SpringValidator.class);

    private AnnotationValidator validator;

    private SpringMessageConverter converter;

    private Map<String, ValidatorParams> validatorMap;

    public SpringValidator() {
        converter = new SpringMessageConverter();
        validatorMap = new HashMap<String, ValidatorParams>();
    }

    public boolean supports(Class c) {
        return validatorMap.containsKey(c.getName());
    }

    public void validate(Object object, Errors errors) {
        if (object != null) {
            ValidatorParams params = null;
            List<ValidationMessage> messages = null;
            String group = null;
            String[] groups = null;
            params = validatorMap.get(object.getClass().getName());
            group = params.getGroup();
            if (logger.isDebugEnabled()) {
                logger.debug("validate: Will attempt to validate group=" + group);
            }
            if (isExpression(group)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("validate: This is an expression group=" + group);
                }
                group = solveExpression(object, removeExpression(group));
                if (logger.isDebugEnabled()) {
                    logger.debug("validate: Solved group to=" + group);
                }
            }
            groups = group.split(",");
            for (int i = 0; i < groups.length; i++) {
                if (logger.isDebugEnabled()) {
                    logger.debug("validate: Calling validation for single group=" + groups[i]);
                }
                messages = validator.validateObject(object, groups[i], params.getPrefixPath(), params.isRecurse(), params.getLevelDeep());
                if (!messages.isEmpty()) {
                    converter.convertMessages(messages, errors);
                }
            }
        }
    }

    public void setValidator(AnnotationValidator validator) {
        this.validator = validator;
    }

    public AnnotationValidator getValidator() {
        return validator;
    }

    public void setValidatorMap(Map<String, ValidatorParams> validatorMap) {
        this.validatorMap = validatorMap;
    }

    public Map<String, ValidatorParams> getValidatorMap() {
        return validatorMap;
    }

    private boolean isExpression(String value) {
        if (value == null || value.length() < 3) {
            return false;
        }
        boolean retValue = false;
        if (value.charAt(0) == '#' && value.charAt(1) == '{' && value.charAt(value.length() - 1) == '}') {
            retValue = true;
        }
        return retValue;
    }

    private String removeExpression(String value) {
        return value.substring(2, value.length() - 1);
    }

    private String solveExpression(Object cmd, String value) {
        String retValue = null;
        try {
            Object mValue = null;
            retValue = ReflectionSupport.propertyToGetMethod(value);
            mValue = ReflectionSupport.invokeMethod(cmd, ReflectionSupport.findMethod(cmd, retValue, null), null);
            if (mValue == null || !(mValue instanceof String)) {
                throw new JavalidException("Value returned from expression value=" + value + ", is not of type java.lang.String");
            }
            retValue = (String) mValue;
        } catch (Exception e) {
            throw new JavalidException("Failed solving expression=" + value, e);
        }
        return retValue;
    }
}
