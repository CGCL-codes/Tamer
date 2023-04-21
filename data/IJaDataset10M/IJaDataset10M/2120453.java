package com.google.gdt.eclipse.designer.hosted.tdz;

import com.google.gdt.eclipse.designer.hosted.IHostedModeSupport;
import com.google.gdt.eclipse.designer.hosted.IHostedModeSupportFactory;
import com.google.gdt.eclipse.designer.hosted.IModuleDescription;

/**
 * Implementation for {@link IHostedModeSupportFactory} for GWT 2.0.
 * 
 * @author mitin_aa
 */
public class HostedModeSupportFactory implements IHostedModeSupportFactory {

    public IHostedModeSupport create(String version, ClassLoader parentClassLoader, IModuleDescription moduleDescription) throws Exception {
        if ("2.0".equals(version) || "2.1".equals(version)) {
            return new HostedModeSupport(parentClassLoader, moduleDescription);
        }
        return null;
    }
}
