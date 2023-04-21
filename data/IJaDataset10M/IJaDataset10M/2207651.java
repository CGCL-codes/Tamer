package net.sourceforge.xconf.toolbox.spring;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import junit.framework.TestCase;
import net.sourceforge.xconf.toolbox.TestObject;

/**
 * @author Tom Czarniecki
 */
public class BeanValidatorAdaptorTest extends TestCase {

    private BeanValidatorAdaptor adaptor;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        adaptor = new BeanValidatorAdaptor();
    }

    public void testSupports() {
        assertTrue(adaptor.supports(String.class));
        adaptor.setSupportedClass(Long.class);
        assertTrue(adaptor.supports(Long.class));
        assertFalse(adaptor.supports(String.class));
    }

    public void testValidate() {
        TestObject obj = new TestObject("test");
        BindException errors = new BindException(obj, "test");
        TestBeanValidator validator = new TestBeanValidator(obj, errors);
        adaptor.setBeanValidator(validator);
        adaptor.validate(obj, errors);
        assertTrue(validator.invoked);
    }

    private final class TestBeanValidator implements BeanValidator {

        private final TestObject obj;

        private final BindException errors;

        private boolean invoked = false;

        private TestBeanValidator(TestObject obj, BindException errors) {
            this.obj = obj;
            this.errors = errors;
        }

        public void validate(BeanWrapper bean, Errors errors) {
            assertSame(this.obj, bean.getWrappedInstance());
            assertSame(this.errors, errors);
            invoked = true;
        }
    }
}
