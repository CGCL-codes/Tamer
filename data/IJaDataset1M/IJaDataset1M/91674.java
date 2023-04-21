package com.google.gdt.eclipse.core.launch;

/**
 * An attribute that can be read or written to a launch configuration that also
 * has some default value.
 */
public interface ILaunchConfigurationAttribute {

    /**
   * @return the default value, or null if there is no constant default value
   */
    Object getDefaultValue();

    String getQualifiedName();
}
