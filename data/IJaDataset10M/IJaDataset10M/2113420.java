package it.hotel.controller.structure.validation;

import it.hotel.controller.abstrakt.validation.AbstractValidator;
import it.hotel.model.structure.Structure;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class StructureValidator extends AbstractValidator {

    private Structure type;

    private final int MAX_SIZE = 50000;

    public void setType(Structure type) {
        this.type = type;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(type.getClass());
    }

    /**
	 * @param
	 */
    public void validate(Object command, Errors errors) {
        Structure istanza = (Structure) command;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "errors.name.required", "errors.name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "errors.address.required", "errors.address.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companytype", "errors.companytype.required", "errors.companytype.required");
        checkRequireMail(istanza.getMail(), "mail", errors);
        this.isImageTooLarge(command, errors);
    }

    private void isImageTooLarge(Object command, Errors errors) {
        Structure structure = (Structure) command;
        if (structure.getImage() != null && structure.getImage().length > MAX_SIZE) {
            errors.reject("errors.image.tooBig", "errors.image.tooBig");
        }
    }
}
