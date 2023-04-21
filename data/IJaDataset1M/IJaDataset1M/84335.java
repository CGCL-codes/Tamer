package net.larsbehnke.petclinicplus.validation;

import net.larsbehnke.petclinicplus.model.Owner;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Owner</code> forms.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class OwnerValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return Owner.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        Owner owner = (Owner) obj;
        ValidationUtils.rejectIfEmpty(errors, "userData.firstName", "required", "required");
        ValidationUtils.rejectIfEmpty(errors, "userData.lastName", "required", "required");
        ValidationUtils.rejectIfEmpty(errors, "address", "required", "required");
        ValidationUtils.rejectIfEmpty(errors, "city", "required", "required");
        String telephone = owner.getTelephone();
        if (!StringUtils.hasLength(telephone)) {
            errors.rejectValue("telephone", "required", "required");
        } else {
            for (int i = 0; i < telephone.length(); ++i) {
                if ((Character.isDigit(telephone.charAt(i))) == false) {
                    errors.rejectValue("telephone", "nonNumeric", "non-numeric");
                    break;
                }
            }
        }
    }
}
