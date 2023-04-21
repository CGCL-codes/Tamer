package net.sourceforge.eclipsex.sdk.internal.sdk2;

import net.sourceforge.eclipsex.debugger.EXDebugTarget;
import net.sourceforge.eclipsex.sdk.internal.SDKVariable;
import net.sourceforge.eclipsex.sdk.internal.SDKVariableProvider;
import flash.tools.debugger.Session;
import flash.tools.debugger.Variable;

public class SDK2VariableProvider implements SDKVariableProvider {

    public SDKVariable create(final EXDebugTarget target, final Session session, final Variable variable) {
        return new SDKVariable(target, session, variable) {
        };
    }
}
