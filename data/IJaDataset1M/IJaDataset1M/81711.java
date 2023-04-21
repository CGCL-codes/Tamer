package ch.arpage.collaboweb.model.validation;

/**
 * MinValueValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MinValueValidatorTest extends AbstractValidatorTest {

    MinValueValidator validator = new MinValueValidator();

    @Override
    protected Validator getValidator() {
        return validator;
    }
}
