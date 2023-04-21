package org.eclipse.cdt.managedbuilder.llvm.ui;

import org.eclipse.cdt.managedbuilder.core.IManagedIsToolChainSupported;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.osgi.framework.Version;

/**
 * Based on MingwIsToolChainSupported.
 *  
 */
public class LlvmIsToolChainSupported implements IManagedIsToolChainSupported {

    private final boolean supported;

    /**
	 * Constructor.
	 * LLVM Toolchain is supported if binary path for LLVM Tools can be found.
	 */
    public LlvmIsToolChainSupported() {
        this.supported = LlvmEnvironmentVariableSupplier.getBinPath() != null;
    }

    /**
	 * Return a boolean value that informs if the LLVM Toolchain is supported.
	 */
    public boolean isSupported(IToolChain toolChain, Version version, String instance) {
        return this.supported;
    }
}
