package net.sf.brightside.beautyshop.metamodel.spring;

import org.testng.annotations.Test;
import net.sf.brightside.beautyshop.metamodel.HairCut;
import net.sf.brightside.beautyshop.spring.AbstractSpringTest;

public class HairCutTest extends AbstractSpringTest {

    HairCut hairCut;

    @Override
    protected Object createUnderTest() {
        return getApplicationContext().getBean(HairCut.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        hairCut = (HairCut) createUnderTest();
    }

    @Test
    public void testExists() {
        assertNotNull(hairCut);
    }
}
