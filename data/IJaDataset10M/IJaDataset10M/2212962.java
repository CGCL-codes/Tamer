package org.nexopenframework.module.kernel.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.nexopenframework.module.config.ModuleDescriptor;
import org.nexopenframework.module.kernel.Kernel;
import org.nexopenframework.module.kernel.spring3.Spring3Kernel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>TODO Document me</p>
 *
 * @see org.nexopenframework.module.kernel.config.KernelNamespaceHandler
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-06-20 18:48:44 +0100 $ 
 * @since 2.0.0.GA
 */
@ContextConfiguration(locations = "/openfrwk-modules-cfg1.xml")
public class KernelNamespaceHandler1Test extends AbstractJUnit4SpringContextTests {

    @Test
    public void kernel() {
        assertNotNull(Kernel.Holder.getKernel());
        assertTrue(Kernel.Holder.getKernel() instanceof Spring3Kernel);
    }

    @Test
    public void descriptor() {
        assertNotNull(ModuleDescriptor.getInstance());
        assertEquals("testModule", ModuleDescriptor.getInstance().getName());
    }
}
