package openschool.services.validator;

import openschool.domain.model.Cours;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validation du formulaire de création de cours
 * 
 * @author remi
 *
 */
public class CoursUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> arg0) {
        return Cours.class.isAssignableFrom(arg0);
    }

    @Override
    public void validate(Object arg0, Errors arg1) {
        ValidationUtils.rejectIfEmpty(arg1, "titre", "titre.empty", "Titre du cours obligatoire");
    }
}
