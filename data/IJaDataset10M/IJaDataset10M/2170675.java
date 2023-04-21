package library.utils.validation;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEnumListValidator implements ConstraintValidator<IsEnumList, Object> {

    @SuppressWarnings("unchecked")
    private Class enumType;

    private String separator;

    @Override
    public void initialize(IsEnumList constraintAnnotation) {
        enumType = constraintAnnotation.value();
        separator = Pattern.quote(constraintAnnotation.separator());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result;
        try {
            Object processedValue = value;
            if (value.getClass().equals(String.class)) {
                String[] tmp = ((String) value).split(separator);
                List<String> tmpFiltered = new ArrayList<String>();
                for (String str : tmp) {
                    if (!str.equals("")) {
                        tmpFiltered.add(str);
                    }
                }
                processedValue = tmpFiltered.toArray();
            }
            result = isEnumConstant(processedValue);
        } catch (IllegalArgumentException e) {
            result = false;
        }
        return result;
    }

    private boolean isEnumConstant(Object value) {
        boolean result;
        if (value.getClass().equals(String.class)) {
            isEnum((String) value);
            result = true;
        } else if (value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++) {
                Object element = Array.get(value, i);
                if (!element.getClass().equals(String.class)) {
                    throw new IllegalArgumentException(element.getClass() + "");
                }
                isEnum((String) element);
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void isEnum(String value) {
        List valuesAllowed = null;
        try {
            Method vals = enumType.getMethod("vals");
            valuesAllowed = (List) vals.invoke(enumType);
            Method method = enumType.getMethod("getValue", String.class);
            Object enumValue = method.invoke(enumType, value);
            if (valuesAllowed == null || !valuesAllowed.contains(enumValue)) {
                throw new IllegalArgumentException(enumType + ": " + enumValue);
            }
        } catch (Exception e) {
            Enum enumValue = Enum.valueOf(enumType, value);
            if (valuesAllowed != null) {
                if (!valuesAllowed.contains(enumValue)) {
                    throw new IllegalArgumentException(enumType + ": " + enumValue);
                }
            }
        }
    }
}
