package org.nextframework.validation.validators;

import java.lang.annotation.Annotation;
import org.nextframework.validation.JavascriptValidationItem;
import org.nextframework.validation.ObjectAnnotationValidator;
import org.nextframework.validation.PropertyValidator;
import org.springframework.validation.Errors;

public class CpfValidator implements PropertyValidator {

    public void validate(Object bean, Object property, String fieldName, String fieldDisplayName, Annotation annotation, Errors errors, ObjectAnnotationValidator annotationValidator) {
    }

    public String getValidationName() {
        return "cpf";
    }

    public String getJavascriptFunctionPath() {
        return "org/nextframework/validation/validators/javascript/validateCpf.js";
    }

    public String getMessage(JavascriptValidationItem validationItem) {
        return "O campo \\\"" + validationItem.getFieldDisplayName() + "\\\" n�o � um CPF v�lido";
    }

    public String getJavascriptFunction(JavascriptValidationItem validationItem) {
        return "new Function (\"varName\", \" return this[varName];\")";
    }

    public String getValidationFunctionName() {
        return "Cpf";
    }
}
