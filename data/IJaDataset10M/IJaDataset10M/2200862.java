package net.sf.brightside.beautyshop.metamodel.spring;

import net.sf.brightside.beautyshop.metamodel.Appointment;
import net.sf.brightside.beautyshop.metamodel.Trinkets;
import net.sf.brightside.beautyshop.spring.AbstractSpringTest;
import org.testng.annotations.Test;

public class TrinketsTest extends AbstractSpringTest {

    private Trinkets trinkets;

    @Override
    protected Object createUnderTest() {
        return getApplicationContext().getBean(Trinkets.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        trinkets = (Trinkets) createUnderTest();
    }

    @Test
    public void testExists() {
        assertNotNull(trinkets);
    }
}
