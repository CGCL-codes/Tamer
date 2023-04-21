package org.xmlcml.cml.element.test;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.element.NamespaceToUnitListMap;

/**
 * tests CMLScalar.
 *
 * @author pmr
 *
 */
public class NumericTest extends AbstractTest {

    /**
	 * making this static speeds reading greatly. because file is only read
	 * once.
	 */
    protected static NamespaceToUnitListMap unitsUnitListMap = null;

    /**
	 * setup.
	 *
	 * @throws Exception
	 */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (unitsUnitListMap == null) {
            try {
                unitsUnitListMap = new NamespaceToUnitListMap(CMLUtil.getResource(UNIT_RESOURCE + U_S + CATALOG_XML));
            } catch (IOException e) {
                Assert.fail("should not throw " + e);
            }
        }
    }
}
