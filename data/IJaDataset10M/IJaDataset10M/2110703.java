package org.databene.benerator.primitive.datetime;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;
import org.databene.benerator.test.GeneratorClassTest;
import org.databene.commons.validator.bean.AbstractConstraintValidator;
import org.junit.Test;

/**
 * Tests the CurrentMilliTimeGenerator.<br/>
 * <br/>
 * Created: 19.11.2007 20:43:45
 * @author Volker Bergmann
 */
public class CurrentMilliTimeGeneratorTest extends GeneratorClassTest {

    public CurrentMilliTimeGeneratorTest() {
        super(CurrentMilliTimeGenerator.class);
    }

    @Test
    public void testProducts() {
        CurrentMilliTimeGenerator generator = new CurrentMilliTimeGenerator();
        generator.init(context);
        expectGenerations(generator, 10, new AbstractConstraintValidator<Annotation, Long>() {

            public boolean isValid(Long millis, ConstraintValidatorContext context) {
                return Math.abs(System.currentTimeMillis() - millis) < 1000L;
            }
        });
    }
}
