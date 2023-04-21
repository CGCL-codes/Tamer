package sccommons.util;

import java.util.Date;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author duo
 */
public class CustomFormattedDateValidator extends Validator {

    @Override
    public Result validate(Object value) {
        if (value instanceof Date || value == null) {
            return null;
        }
        return new Result(null, "Data invalida!");
    }
}
