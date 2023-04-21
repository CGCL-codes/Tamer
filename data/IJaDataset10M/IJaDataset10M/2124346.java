package org.viewaframework.test;

import java.util.Collection;
import junit.framework.TestCase;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.viewaframework.test.application.ApplicationTrapperAware;
import org.viewaframework.test.application.NumberAware;
import org.viewaframework.test.application.ViewTrapperAware;

public class PropertyTrapper implements Trapper<Object>, ApplicationTrapperAware, ViewTrapperAware, NumberAware {

    private static final Log logger = LogFactory.getLog(PropertyTrapper.class);

    private ApplicationTrapper applicationTrapper;

    private Object target;

    /**
	 * @param applicationTrapper
	 * @param componentName
	 */
    public PropertyTrapper(ApplicationTrapper applicationTrapper, String targetName) {
        this.applicationTrapper = applicationTrapper;
        this.target = applicationTrapper.view().getTarget();
        try {
            this.target = new PropertyUtilsBean().getProperty(this.target, targetName);
        } catch (Exception e) {
            TestCase.fail(e.getMessage());
        }
    }

    public ApplicationTrapper applicationTrapper() {
        return this.applicationTrapper;
    }

    public PropertyTrapper asByte() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).byteValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    /**
	 * @return
	 */
    public PropertyTrapper asCollection() {
        this.target = Collection.class.cast(target);
        return this;
    }

    public PropertyTrapper asDouble() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).doubleValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    public PropertyTrapper asFloat() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).floatValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    public PropertyTrapper asInteger() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).intValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    public PropertyTrapper asLong() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).longValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    public PropertyTrapper asNumber() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target);
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    public PropertyTrapper asShort() {
        try {
            if (this.target instanceof Number) {
                this.target = Number.class.cast(this.target).shortValue();
            }
        } catch (Throwable t) {
            TestCase.fail(t.getMessage());
        }
        return this;
    }

    /**
	 * @return
	 */
    public Integer collectionSize() {
        Integer result = -1;
        if (this.target.getClass().isAssignableFrom(Collection.class)) {
            result = Collection.class.cast(this.target).size();
        }
        return result;
    }

    public Object getTarget() {
        return target;
    }

    public Class<Object> getType() {
        return Object.class;
    }

    public PropertyTrapper log(String message) {
        logger.info(message);
        return this;
    }

    /**
	 * @param propertyName
	 * @return
	 */
    public PropertyTrapper property(String propertyName) {
        try {
            this.target = new PropertyUtilsBean().getProperty(this.target, propertyName);
        } catch (Exception e) {
            TestCase.fail(e.getMessage());
        }
        return this;
    }

    public PropertyTrapper requireByte(Byte number) {
        if (this.target instanceof Byte) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Byte expected");
        }
        return this;
    }

    public PropertyTrapper requireDouble(Double number) {
        if (this.target instanceof Double) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Double expected");
        }
        return this;
    }

    public PropertyTrapper requireFloat(Float number) {
        if (this.target instanceof Float) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Float expected");
        }
        return this;
    }

    public PropertyTrapper requireInteger(Integer number) {
        if (this.target instanceof Integer) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Integer expected");
        }
        return this;
    }

    public PropertyTrapper requireLong(Long number) {
        if (this.target instanceof Long) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Long expected");
        }
        return this;
    }

    public PropertyTrapper requireNumber(Number number) {
        if (this.target instanceof Number) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Number expected");
        }
        return this;
    }

    public PropertyTrapper requireShort(Short number) {
        if (this.target instanceof Short) {
            TestCase.assertEquals(number, this.target);
        } else {
            TestCase.fail("Not correct type Short expected");
        }
        return this;
    }

    /**
	 * @param expected
	 * @return
	 */
    public PropertyTrapper requireSize(Integer expected) {
        if (this.target.getClass().isAssignableFrom(Collection.class)) {
            TestCase.assertEquals(expected.intValue(), Collection.class.cast(this.target).size());
        } else {
            TestCase.fail("Target is not a collection");
        }
        return this;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public ViewTrapper view() {
        return this.applicationTrapper().view();
    }

    public ViewTrapper view(String viewId) {
        return this.applicationTrapper().view(viewId);
    }
}
