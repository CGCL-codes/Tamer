package br.com.linkcom.neo.validation.validators;

import java.lang.annotation.Annotation;
import org.springframework.validation.Errors;
import br.com.linkcom.neo.validation.JavascriptValidationItem;
import br.com.linkcom.neo.validation.ObjectAnnotationValidator;
import br.com.linkcom.neo.validation.PropertyValidator;

public class IntegerValidator implements PropertyValidator {

    public void validate(Object bean, Object property, String fieldName, String fieldDisplayName, Annotation annotation, Errors errors, ObjectAnnotationValidator annotationValidator) {
    }

    public String getValidationName() {
        return "IntegerValidations";
    }

    public String getJavascriptFunctionPath() {
        return "br/com/linkcom/neo/validation/validators/javascript/validateInteger.js";
    }

    public String getMessage(JavascriptValidationItem validationItem) {
        return "O campo \\\"" + validationItem.getFieldDisplayName() + "\\\" n�o � um n�mero inteiro v�lido";
    }

    public String getJavascriptFunction(JavascriptValidationItem validationItem) {
        return "new Function (\"varName\", \" return this[varName];\")";
    }

    public String getValidationFunctionName() {
        return "Integer";
    }
}
