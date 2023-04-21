package org.nexopenframework.module.kernel.config;

import org.nexopenframework.module.kernel.KernelComponent;
import org.nexopenframework.module.kernel.support.AbstractKernelComponent;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>Mock object for testing support</p>
 *
 * @see org.nexopenframework.module.kernel.support.AbstractKernelComponent
 * @see org.nexopenframework.module.kernel.KernelComponent
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-06-28 19:48:44 +0100 $ 
 * @since 2.0.0.GA
 */
public class SimpleKernelComponent extends AbstractKernelComponent implements KernelComponent {

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.module.Component#getName()
	 */
    public String getName() {
        return "openfrwk.module.simple_kernel_component";
    }
}
