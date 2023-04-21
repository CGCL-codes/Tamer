package org.springextension.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.DefaultValidatorFactory;

/**
 * Extended version of {@link DefaultValidatorFactory} to provide 
 * field-wise validator instances.
 * 
 * @author Aditya Jha
 * 
 */
public class ExtendedValidatorFactory extends DefaultValidatorFactory {

    /**
	 * Returns a list of {@link Validator} instances - one for each field of the given bean.
	 * 
	 * @param beanName Field name
	 * @param bean Bean to validate
	 * @param errors {@link Errors} instance
	 * 
	 * @return List of {@link Validator} instances
	 */
    public List<Validator> getValidators(String beanName, Object bean, Errors errors) {
        return this.getValidators(beanName, bean, errors, null);
    }

    /**
	 * Returns a list of {@link Validator} instances - one for each field of the given bean.
	 * 
	 * @param beanName Field name
	 * @param bean Bean to validate
	 * @param errors {@link Errors} instance
	 * @param ignorableFields Set of field-names to be ignored 
	 *                        ({@link Validator} instances will not be created for these fields)
	 * 
	 * @return List of {@link Validator} instances
	 */
    public List<Validator> getValidators(String beanName, Object bean, Errors errors, Set<String> ignorableFields) {
        List<Field> fields = this.getFields(beanName);
        List<Validator> validators = new ArrayList<Validator>();
        if (ignorableFields == null) {
            ignorableFields = Collections.emptySet();
        }
        for (Field f : fields) {
            String fieldName = f.getKey();
            if (!ignorableFields.contains(fieldName)) {
                Validator validator = new Validator(this.getValidatorResources(), beanName, fieldName);
                validator.setParameter(DefaultValidatorFactory.ERRORS_KEY, errors);
                validator.setParameter(Validator.BEAN_PARAM, bean);
                validators.add(validator);
            }
        }
        return validators;
    }

    /**
	 * Returns an ordered list of fields for the given bean as per the configuration.
	 * 
	 * @param beanName Bean for which field-list is to be returned
	 * 
	 * @return List of fields
	 */
    @SuppressWarnings(value = "unchecked")
    public List<Field> getFields(String beanName) {
        return this.getValidatorResources().getForm(Locale.getDefault(), beanName).getFields();
    }
}
