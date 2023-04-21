package net.sf.mmm.util.pojo.descriptor.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.sf.mmm.util.pojo.descriptor.api.PojoDescriptor;
import net.sf.mmm.util.pojo.descriptor.api.PojoDescriptorBuilder;
import net.sf.mmm.util.pojo.descriptor.impl.dummy.MyPojo;

/**
 * This is the test-case for {@link PojoDescriptorBuilder} using public method
 * introspection.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("all")
public class PublicMethodPojoDescriptorBuilderTest extends AbstractMyPojoDescriptorBuilderTest {

    /**
   * {@inheritDoc}
   */
    protected PojoDescriptorBuilder getPojoDescriptorBuilder() {
        PojoDescriptorBuilderImpl builder = new PojoDescriptorBuilderImpl();
        builder.initialize();
        return builder;
    }

    @Test
    public void testPojoDescriptor() throws Exception {
        PojoDescriptorBuilder builder = getPojoDescriptorBuilder();
        PojoDescriptor<MyPojo> pojoDescriptor = builder.getDescriptor(MyPojo.class);
        assertEquals(MyPojo.class, pojoDescriptor.getPojoClass());
        MyPojo pojoInstance = new MyPojo();
        assertEquals(MyPojo.class, pojoDescriptor.getProperty(pojoInstance, "class"));
        checkPojo(pojoDescriptor, pojoInstance, builder);
        checkProperty(pojoDescriptor, "port", Integer.class, int.class);
        checkProperty(pojoDescriptor, "flag", Boolean.class, boolean.class);
        checkItems(pojoDescriptor, pojoInstance, true);
        checkValues(pojoDescriptor, pojoInstance, true);
    }
}
