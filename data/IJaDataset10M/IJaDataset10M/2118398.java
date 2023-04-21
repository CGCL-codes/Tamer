package org.mozilla.javascript;

import org.mozilla.javascript.debug.DebuggableScript;

/**
 * This class implements the Function native object.
 * See ECMA 15.3.
 * @author Norris Boyd
 */
public abstract class NativeFunction extends BaseFunction {

    public final void initScriptFunction(Context cx, Scriptable scope) {
        ScriptRuntime.setFunctionProtoAndParent(this, scope);
    }

    /**
     * @param indent How much to indent the decompiled result
     *
     * @param flags Flags specifying format of decompilation output
     */
    final String decompile(int indent, int flags) {
        String encodedSource = getEncodedSource();
        if (encodedSource == null) {
            return super.decompile(indent, flags);
        } else {
            UintMap properties = new UintMap(1);
            properties.put(Decompiler.INITIAL_INDENT_PROP, indent);
            return Decompiler.decompile(encodedSource, flags, properties);
        }
    }

    public int getLength() {
        int paramCount = getParamCount();
        if (getLanguageVersion() != Context.VERSION_1_2) {
            return paramCount;
        }
        Context cx = Context.getContext();
        NativeCall activation = ScriptRuntime.findFunctionActivation(cx, this);
        if (activation == null) {
            return paramCount;
        }
        return activation.originalArgs.length;
    }

    public int getArity() {
        return getParamCount();
    }

    /**
     * @deprecated Use {@link BaseFunction#getFunctionName()} instead.
     * For backwards compatibility keep an old method name used by
     * Batik and possibly others.
     */
    public String jsGet_name() {
        return getFunctionName();
    }

    /**
     * Get encoded source string.
     */
    public String getEncodedSource() {
        return null;
    }

    public DebuggableScript getDebuggableView() {
        return null;
    }

    protected abstract int getLanguageVersion();

    /**
     * Get number of declared parameters. It should be 0 for scripts.
     */
    protected abstract int getParamCount();

    /**
     * Get number of declared parameters and variables defined through var
     * statements.
     */
    protected abstract int getParamAndVarCount();

    /**
     * Get parameter or variable name.
     * If <tt>index < {@link #getParamCount()}</tt>, then return the name of the
     * corresponding parameter. Otherwise return the name of variable.
     */
    protected abstract String getParamOrVarName(int index);

    /**
     * Get parameter or variable const-ness.
     * If <tt>index < {@link #getParamCount()}</tt>, then return the const-ness
     * of the corresponding parameter. Otherwise return whether the variable is
     * const.
     */
    protected abstract boolean getParamOrVarConst(int index);
}
