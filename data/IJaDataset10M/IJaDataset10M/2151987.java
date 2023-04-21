package com.ibm.celldt.environment.launcher.macros;

/**
 * This interface is similar to the one provided by
 * 
 * @see org.eclipse.cdt.managedbuilder.macros.IBuildMacroProvider.
 * 
 * @author laggarcia
 * @since 3.0.0
 */
public interface ILaunchMacroProvider {

    public static final int CONTEXT_WORKSPACE = 5;

    public static final int CONTEXT_LAUNCH = 9;

    public String resolveValue(String value, String nonexistentMacrosValue, String listDelimiter, int contextType, Object contextData) throws LaunchMacroException;
}
