package br.com.linkcom.neo.validation.validators;

import java.lang.annotation.Annotation;
import java.util.List;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import br.com.linkcom.neo.validation.JavascriptValidationItem;
import br.com.linkcom.neo.validation.ObjectAnnotationValidator;
import br.com.linkcom.neo.validation.PropertyValidator;
import br.com.linkcom.neo.validation.annotation.MinLength;

public class MinLengthValidator implements PropertyValidator {

    public void validate(Object bean, Object property, String fieldName, String fieldDisplayName, Annotation annotation, Errors errors, ObjectAnnotationValidator annotationValidator) {
        if (property != null && !property.toString().trim().equals("")) {
            MinLength minLength = (MinLength) annotation;
            int min = minLength.value();
            if (!GenericValidator.minLength(property.toString(), min)) {
                errors.rejectValue(fieldName, "minLenght", "O campo " + fieldDisplayName + " deve ter um tamanho menor ou igual � " + min);
            }
        }
    }

    public String getValidationName() {
        return "minlength";
    }

    public String getValidationFunctionName() {
        return "MinLength";
    }

    public String getJavascriptFunctionPath() {
        return "br/com/linkcom/neo/validation/validators/javascript/validateMinLength.js";
    }

    public String getJavascriptFunction(JavascriptValidationItem validationItem) {
        List<Annotation> validations = validationItem.getValidations();
        int min = 0;
        for (Annotation annotation : validations) {
            if (MinLength.class.isAssignableFrom(annotation.getClass())) {
                min = ((MinLength) annotation).value();
                break;
            }
        }
        return "new Function (\"varName\", \"this.minlength='" + min + "';  return this[varName];\")";
    }

    public String getMessage(JavascriptValidationItem validationItem) {
        List<Annotation> validations = validationItem.getValidations();
        int min = 0;
        for (Annotation annotation : validations) {
            if (MinLength.class.isAssignableFrom(annotation.getClass())) {
                min = ((MinLength) annotation).value();
                break;
            }
        }
        return "O campo " + validationItem.getFieldDisplayName() + " deve ter um tamanho maior que " + min;
    }
}
