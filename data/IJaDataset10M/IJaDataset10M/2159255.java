package net.sf.oval.test.validator;

import java.util.List;
import junit.framework.TestCase;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.Assert;

/**
 * @author Sebastian Thomschke
 */
public class AssertJavascriptTest extends TestCase {

    @Assert(expr = "_this.firstName!=null && _this.lastName!=null && (_this.firstName.length() + _this.lastName.length() > 9)", lang = "javascript", errorCode = "C0")
    public static class Person {

        @Assert(expr = "_value!=null", lang = "javascript", errorCode = "C1")
        public String firstName;

        @Assert(expr = "_value!=null", lang = "js", errorCode = "C2")
        public String lastName;

        @Assert(expr = "_value!=null && _value.length>0 && _value.length<7", lang = "javascript", errorCode = "C3")
        public String zipCode;
    }

    private static class TestRunner implements Runnable {

        private final boolean[] failed;

        private final Validator validator;

        private final Person person;

        public TestRunner(final Validator validator, final Person person, final boolean[] failed) {
            this.validator = validator;
            this.person = person;
            this.failed = failed;
        }

        /**
		 * {@inheritDoc}
		 */
        public void run() {
            for (int i = 0; i < 500; i++) {
                if (validator.validate(person).size() != 4) {
                    failed[0] = true;
                }
                try {
                    Thread.sleep(2);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void testConcurrency() throws InterruptedException {
        final Validator validator = new Validator();
        final Person person = new Person();
        final boolean[] failed = { false };
        final Thread thread1 = new Thread(new TestRunner(validator, person, failed));
        final Thread thread2 = new Thread(new TestRunner(validator, person, failed));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertFalse(failed[0]);
    }

    public void testJavaScriptExpression() {
        final Validator validator = new Validator();
        final Person p = new Person();
        List<ConstraintViolation> violations = validator.validate(p);
        assertTrue(violations.size() == 4);
        p.firstName = "Mike";
        p.lastName = "Mahoney";
        p.zipCode = "1234567";
        violations = validator.validate(p);
        assertTrue(violations.size() == 1);
        assertTrue(violations.get(0).getErrorCode().equals("C3"));
        p.zipCode = "";
        violations = validator.validate(p);
        assertTrue(violations.size() == 1);
        assertTrue(violations.get(0).getErrorCode().equals("C3"));
        p.zipCode = "wqeew";
        violations = validator.validate(p);
        assertTrue(violations.size() == 0);
        p.firstName = "12345";
        p.lastName = "1234";
        violations = validator.validate(p);
        assertTrue(violations.size() == 1);
        assertTrue(violations.get(0).getErrorCode().equals("C0"));
    }
}
