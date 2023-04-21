package net.sf.jcorrect.standard.constraintfactory;

import javax.validation.ConstraintFactory;
import net.sf.jcorrect.standard.ConfigOptions;
import org.junit.Ignore;
import org.junit.Test;

public class ConstraintFactorySourceTest {

    @Test
    @Ignore
    public void simpleFactory() {
        ConstraintFactory f = ConstraintFactorySource.getFactory();
        org.junit.Assert.assertNotNull(f);
        org.junit.Assert.assertEquals(ConstraintFactoryImpl.class, f.getClass());
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void errorInConfigurationNoSuchClass() {
        System.getProperties().put(ConfigOptions.CONSTRAINT_FACTORY_CREATOR_CLASS_NAME, "nosuchclass");
        ConstraintFactory f = ConstraintFactorySource.getFactory();
        org.junit.Assert.fail("The invocation should have failed - the factory creator class doesn't exist");
    }
}
