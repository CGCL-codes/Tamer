package net.sf.brightside.dentalwizard.metamodel.spring;

import net.sf.brightside.dentalwizard.core.spring.AbstractSpringTest;
import net.sf.brightside.dentalwizard.metamodel.Tooth;
import org.testng.annotations.Test;

public class ToothTest extends AbstractSpringTest {

    private Tooth toothUnderTest;

    protected Tooth createUnderTest() {
        return (Tooth) getApplicationContext().getBean(Tooth.class.getName());
    }

    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        toothUnderTest = createUnderTest();
    }

    @Test
    public void testExist() {
        assertNotNull(toothUnderTest);
    }
}
