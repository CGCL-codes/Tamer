package net.sf.staccatocommons.restrictions.instrument;

import static org.junit.Assert.*;
import net.sf.staccatocommons.instrument.InstrumentationRunner;
import net.sf.staccatocommons.io.Directory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author flbulgarelli
 * 
 */
public class InstrumentationIntegrationTest {

    /** setup */
    @BeforeClass
    public static void before() throws Exception {
        InstrumentationRunner.runInstrumentation(new RestrictionConfigurer(true, true, false), new Directory("target/test-classes"), "");
    }

    /** Test the {@link ConstantHandler} */
    @Test
    public void testConst() throws Exception {
        Assert.assertSame(Mock.getDateConstructor(), Mock.getDateConstructor());
    }

    /**
   * Test that the {@link ConstantHandler} does not process code with less than
   * two max locals
   */
    @Test
    public void testConstLessThan2MaxLocals() throws Exception {
        Assert.assertSame(Mock.getDateFieldAcess(), Mock.getDateFieldAcess());
        Assert.assertNotSame(Mock.getDateInvokationWithNoArgs(), Mock.getDateInvokationWithNoArgs());
        Assert.assertNotSame(Mock.getDateInvokationWithArg(), Mock.getDateInvokationWithArg());
    }

    /**
   * Test that Constant works in instance methods
   * 
   * @throws Exception
   */
    @Test
    public void testConstInstance() throws Exception {
        Mock m1 = new Mock();
        assertSame(m1.getBigDecimal(), m1.getBigDecimal());
        Mock m2 = new Mock();
        assertNotSame(m2.getBigDecimal(), m1.getBigDecimal());
    }
}
