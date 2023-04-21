package org.databene.commons.bean;

import static junit.framework.Assert.*;
import org.databene.commons.ConfigurationError;
import org.databene.commons.UpdateFailedException;
import org.databene.commons.bean.PropertyGraphMutator;
import org.databene.commons.bean.PropertyMutatorFactory;
import org.databene.commons.bean.TypedPropertyMutator;
import org.databene.commons.bean.UntypedPropertyMutator;
import org.junit.Test;

/**
 * Tests the {@link PropertyMutatorFactory}.<br/><br/>
 * Created: 20.02.2007 08:52:49
 * @author Volker Bergmann
 */
public class PropertyMutatorFactoryTest {

    @Test
    public void testSimpleProperty() {
        assertEquals(TypedPropertyMutator.class, PropertyMutatorFactory.getPropertyMutator(ABean.class, "name", true).getClass());
        assertEquals(TypedPropertyMutator.class, PropertyMutatorFactory.getPropertyMutator(ABean.class, "doesntExsist", false).getClass());
        try {
            PropertyMutatorFactory.getPropertyMutator(ABean.class, "doesntExsist");
            fail("ConfigurationError expected");
        } catch (ConfigurationError e) {
        }
        assertEquals(UntypedPropertyMutator.class, PropertyMutatorFactory.getPropertyMutator("name").getClass());
    }

    @Test
    public void testNavigatedProperty() throws UpdateFailedException {
        assertEquals(PropertyGraphMutator.class, PropertyMutatorFactory.getPropertyMutator(ABean.class, "b.name").getClass());
        assertEquals(PropertyGraphMutator.class, PropertyMutatorFactory.getPropertyMutator("b.name").getClass());
        assertEquals(PropertyGraphMutator.class, PropertyMutatorFactory.getPropertyMutator(ABean.class, "doesnt.exist", false).getClass());
        try {
            PropertyMutatorFactory.getPropertyMutator(ABean.class, "doesnt.exist", true);
            fail("ConfigurationError expected");
        } catch (ConfigurationError e) {
        }
    }
}
