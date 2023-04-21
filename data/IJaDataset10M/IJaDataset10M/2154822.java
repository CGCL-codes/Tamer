package com.ibm.celldt.environment.launcher.internal.macros;

import org.eclipse.cdt.managedbuilder.internal.macros.IMacroSubstitutor;
import org.eclipse.cdt.managedbuilder.macros.BuildMacroException;
import com.ibm.celldt.environment.launcher.macros.LaunchMacroException;

/**
 * This is the utility class used to resolve macro references and that provides
 * other functionality related to the macro resolving
 * 
 * This class tries to use the most from
 * org.eclipse.cdt.managedbuilder.internal.macros.MacroResolver that it can.
 * 
 * @author laggarcia
 * @since 3.0.0
 */
public class MacroResolver {

    public static String resolveToString(String string, IMacroSubstitutor substitutor) throws LaunchMacroException {
        try {
            return org.eclipse.cdt.managedbuilder.internal.macros.MacroResolver.resolveToString(string, substitutor);
        } catch (BuildMacroException bme) {
            throw new LaunchMacroException(bme);
        }
    }
}
