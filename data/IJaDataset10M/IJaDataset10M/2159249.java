package com.eaio.plateau.examples.filters;

import tests.Multiplier;
import tests.MultiplierImpl;
import tests.PlateauRegistryImplBaseTest;
import com.eaio.plateau.PlateauRegistry;
import com.eaio.plateau.examples.filters.ANDBindingFilter;
import com.eaio.plateau.filter.BindingFilter;
import com.eaio.plateau.filter.ClassBindingFilter;

/**
 * A test case for the
 * {@link com.eaio.plateau.examples.filters.ANDBindingFilter} class.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: ANDBindingFilterTest.java,v 1.1 2005/11/20 20:31:40 grnull Exp $
 */
public class ANDBindingFilterTest extends PlateauRegistryImplBaseTest {

    /**
  * Constructor for ANDBindingFilterTest.
  * @param arg0
  */
    public ANDBindingFilterTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(ANDBindingFilterTest.class);
    }

    public void testANDBindingFilter() {
        ClassBindingFilter f1 = new ClassBindingFilter(Multiplier.class);
        ClassBindingFilter f2 = new ClassBindingFilter(MultiplierImpl.class);
        ANDBindingFilter filter = new ANDBindingFilter(new BindingFilter[] { f1, f2 });
        try {
            String[] keys = ((PlateauRegistry) registryTwo.lookup("plateau://127.0.0.1:1099/")).listFiltered(filter);
            assertEquals(2, keys.length);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.toString());
        }
        try {
            filter = new ANDBindingFilter(null);
            fail("Did not throw NPE.");
        } catch (Exception ex) {
            assertTrue(ex instanceof NullPointerException);
        }
        try {
            filter = new ANDBindingFilter(new BindingFilter[0]);
            fail("Did not throw IAE.");
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }
}
