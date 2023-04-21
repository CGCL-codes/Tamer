package net.sf.brightside.moljac.service;

import org.testng.annotations.Test;
import net.sf.brightside.moljac.core.spring.AbstractSpringTest;

public class ShowBookBuyerTest extends AbstractSpringTest {

    private ShowBookBuyer objectUnderTest;

    protected ShowBookBuyer createShowBookBuyer() {
        return (ShowBookBuyer) applicationContext.getBean("ShowBookBuyer");
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        objectUnderTest = createShowBookBuyer();
    }

    @Test
    public void testExist() {
        assertNotNull(objectUnderTest);
    }
}
