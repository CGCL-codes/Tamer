package unit.util.validation;

import java.util.ArrayList;
import java.util.Collection;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.co.q3c.deplan.util.validation.ValidationCheck;

/**
 * @see ValidationCheck
 * @author DSowerby 7 Dec 2008
 * 
 */
@Test
public class ValidationObject_UT {

    ValidationCheck vob;

    @BeforeMethod
    public void beforeMethod() {
        vob = ValidationCheck.newValidInstance();
    }

    @AfterMethod
    public void afterMethod() {
        vob = null;
    }

    public void creation() {
        Assert.assertTrue(vob.isValid());
    }

    public void checkNotNull() {
        Object obj = null;
        vob.checkNotNull(obj, "testObject");
        Assert.assertFalse(vob.isValid());
        Assert.assertEquals(vob.combinedMessages(), "testObject:" + ValidationCheck.CANNOT_BE_NULL + "  ");
    }

    public void checkCollectionGreaterThan() {
        Collection<?> collection = new ArrayList<Object>();
        vob.checkCollectionGreaterThan(collection, "testObject", 0);
        Assert.assertFalse(vob.isValid());
        Assert.assertEquals(vob.combinedMessages(), "testObject:" + ValidationCheck.MUST_BE_GREATER_THAN + "0  ");
    }
}
