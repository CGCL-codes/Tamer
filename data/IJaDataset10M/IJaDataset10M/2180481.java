package org.broadleafcommerce.profile.web.controller.validator;

import org.broadleafcommerce.profile.core.domain.Phone;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blPhoneValidator")
public class PhoneValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return clazz.equals(Phone.class);
    }

    public void validate(Object obj, Errors errors) {
        Phone phone = (Phone) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber", "phone.required", new Object[] { phone });
        if (!errors.hasErrors()) {
            String phoneNumber = phone.getPhoneNumber();
            String newString = phoneNumber.replaceAll("\\D", "");
            if (newString.length() != 10) {
                errors.rejectValue("phoneNumber", "phone.ten_digits_required", null);
            }
            if (newString.equals("1234567890") || newString.equals("0123456789") || newString.matches("0{10}") || newString.matches("1{10}") || newString.matches("2{10}") || newString.matches("3{10}") || newString.matches("4{10}") || newString.matches("5{10}") || newString.matches("6{10}") || newString.matches("7{10}") || newString.matches("8{10}") || newString.matches("9{10}")) {
                errors.rejectValue("phoneNumber", "phone.invalid", null);
            }
        }
    }
}
