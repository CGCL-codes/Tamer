package lg.commands.excursion;

import java.sql.Date;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FrecuenciaFormValidator implements Validator {

    public boolean supports(Class arg0) {
        return arg0 == FrecuenciaForm.class;
    }

    public void validate(Object command, Errors errors) {
        FrecuenciaForm form = (FrecuenciaForm) command;
    }
}
