package it.hotel.controller.photo.validator;

import it.hotel.controller.abstrakt.validation.AbstractValidator;
import it.hotel.model.photo.Photo;
import org.springframework.validation.Errors;

public class PhotoValidator extends AbstractValidator {

    private Photo type;

    private final int MAX_SIZE = 50000;

    public boolean supports(Class clazz) {
        return clazz.equals(type.getClass());
    }

    /**
	 * @param
	 */
    public void validate(Object command, Errors errors) {
        Photo istance = (Photo) command;
        checkFormartImageFile(istance.getImage(), "image", errors);
    }

    public Photo getType() {
        return type;
    }

    public void setType(Photo type) {
        this.type = type;
    }
}
