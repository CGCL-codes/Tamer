package net.sf.brightside.beautyshop.metamodel.spring;

import net.sf.brightside.beautyshop.metamodel.AppointmentType;
import net.sf.brightside.beautyshop.spring.AbstractSpringTest;
import org.testng.annotations.Test;

public class AppointmentTypeTest extends AbstractSpringTest {

    private AppointmentType type;

    @Override
    protected Object createUnderTest() {
        return getApplicationContext().getBean(AppointmentType.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        type = (AppointmentType) createUnderTest();
    }

    @Test
    public void testExists() {
        assertNotNull(type);
    }
}
