package org.sc.binary;

import org.sc.java.Signature;
import org.sc.java.Utils;

/**
 * @author Niels Thykier
 *
 */
public class ScriptImpl implements Script {

    protected final String scriptName;

    protected final String[] strTable;

    protected final String[] dependencies;

    protected final Function functions[];

    protected boolean isResolved = false;

    public ScriptImpl(String scriptName, Function[] functions, String[] strTable, String[] dependencies) {
        this.scriptName = scriptName;
        this.strTable = strTable;
        this.dependencies = dependencies;
        this.functions = functions;
    }

    public String getName() {
        return scriptName;
    }

    public String[] getStringTable() {
        return (String[]) Utils.copyArray(strTable);
    }

    public boolean isResolved() {
        return isResolved;
    }

    public String[] getDependencies() {
        return (String[]) Utils.copyArray(dependencies);
    }

    public Function[] getFunctions() {
        return (Function[]) Utils.copyArray(functions);
    }

    public Function getFunction(String name, Signature sig) {
        for (int i = 0; i < functions.length; i++) {
            if (functions[i].getName().equals(name)) {
                return functions[i];
            }
        }
        return null;
    }

    public Function getFunction(int index) {
        return functions[index];
    }

    public int getFunctionCount() {
        return functions.length;
    }

    public String getDependency(int index) {
        return dependencies[index];
    }

    public int getDependencyCount() {
        return dependencies.length;
    }
}
