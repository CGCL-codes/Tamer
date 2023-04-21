package com.ibm.celldt.environment.launcher.internal.macros;

import org.eclipse.cdt.managedbuilder.internal.macros.DefaultMacroSubstitutor;
import org.eclipse.cdt.managedbuilder.internal.macros.IMacroContextInfo;
import org.eclipse.cdt.managedbuilder.internal.macros.EclipseVariablesMacroSupplier;
import org.eclipse.cdt.managedbuilder.internal.macros.IMacroSubstitutor;
import com.ibm.celldt.environment.launcher.macros.ILaunchMacroProvider;
import com.ibm.celldt.environment.launcher.macros.LaunchMacroException;

/**
 * This class is similar to
 * org.eclipse.cdt.managedbuilder.internal.macros.BuildMacroProvider
 * 
 * @author laggarcia
 * @since 3.0.0
 */
public class LaunchMacroProvider implements ILaunchMacroProvider {

    private static LaunchMacroProvider instance;

    public static LaunchMacroSupplier launchMacroSupplier = LaunchMacroSupplier.getInstance();

    public static EclipseVariablesMacroSupplier eclipseVariablesMacroSupplier = EclipseVariablesMacroSupplier.getInstance();

    public LaunchMacroProvider() {
    }

    public static LaunchMacroProvider getDefault() {
        if (instance == null) instance = new LaunchMacroProvider();
        return instance;
    }

    public String resolveValue(String value, String nonexistentMacrosValue, String listDelimiter, int contextType, Object contextData) throws LaunchMacroException {
        IMacroContextInfo info = getMacroContextInfo(contextType, contextData);
        if (info != null) return MacroResolver.resolveToString(value, getMacroSubstitutor(info, nonexistentMacrosValue, listDelimiter));
        return null;
    }

    public IMacroContextInfo getMacroContextInfo(int contextType, Object contextData) {
        LaunchMacroContextInfo info = new LaunchMacroContextInfo(contextType, contextData);
        if (info.getSuppliers() != null) return info;
        return null;
    }

    public IMacroSubstitutor getMacroSubstitutor(IMacroContextInfo info, String inexistentMacroValue, String listDelimiter) {
        return new DefaultMacroSubstitutor(info, inexistentMacroValue, listDelimiter);
    }
}
