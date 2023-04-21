package br.com.caelum.stella.hibernate.validator.xml;

import junit.framework.Assert;
import net.vidageek.mirror.Mirror;
import org.junit.Test;
import br.com.caelum.stella.hibernate.validator.xml.logic.StellaLengthValidator;

public class StellaLengthValidatorTest {

    @Test
    public void testThatNullIsValid() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("foo"));
        Assert.assertTrue(validator.isValid(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testThatThrowsExceptionIfTypeIsNotString() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("bar"));
        Assert.assertTrue(validator.isValid(1));
    }

    @Test
    public void testThatIsInvalidIfStringIsSmallerThanParameter() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("s1"));
        Assert.assertFalse(validator.isValid("as"));
    }

    @Test
    public void testThatIsInvalidIfStringIsBiggerThanParameter() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("s2"));
        Assert.assertFalse(validator.isValid("12345678912"));
    }

    @Test(expected = IllegalStateException.class)
    public void testThatThrowsExceptionIfMinIsBiggerThanMax() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("s4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testThatThrowsExceptionIfMinIsNegative() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("s5"));
    }

    @Test
    public void testThatIsValidIfBetweenMaxAndMin() {
        StellaLengthValidator validator = new StellaLengthValidator();
        validator.initialize(Mirror.on(AnnotatedModel.class).reflect().annotation(Length.class).atField("s3"));
        Assert.assertTrue(validator.isValid("1234567890"));
    }

    public static class AnnotatedModel {

        @Length
        public String foo = null;

        @Length
        public Integer bar = null;

        @Length(min = 10)
        public String s1;

        @Length(max = 10)
        public String s2;

        @Length(min = 10, max = 10)
        public String s3;

        @Length(min = 11, max = 10)
        public String s4;

        @Length(min = -1, max = 10)
        public String s5;
    }
}
