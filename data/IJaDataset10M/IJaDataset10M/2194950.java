package com.google.gdt.eclipse.core.extensiontypes;

import org.eclipse.core.resources.IProject;

/**
 * Interface provides mechanism to test whether the Managed API functionality
 * should be activated -- generally due to potential problems with compatibility
 * with other plugins.
 */
public interface IManagedApiProjectStateTest {

    public boolean isValidToAddManagedApiProjectState(IProject project);
}
