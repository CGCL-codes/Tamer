package net.sf.brightside.qualifications.core.beans;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

public class BaseBeanTest {

    private BaseBean baseBean;

    protected BaseBean createUnderTest() {
        return new BaseBean();
    }

    @BeforeMethod
    public void setUp() {
        baseBean = createUnderTest();
    }

    @Test
    public void test_id() {
        Long testId = new Long(16);
        assertNull(baseBean.get_id());
        baseBean.set_id(testId);
        assertSame(testId, baseBean.get_id());
    }
}
