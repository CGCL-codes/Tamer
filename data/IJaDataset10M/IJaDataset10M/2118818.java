package org.shava.syntax.ast;

import java.util.List;

/**
 * @author Julián Gutierrez Oschmann
 *
 */
public class ScriptInvocation extends ScriptInstruction {

    /**
	 * Script name.
	 */
    String scriptName;

    /**
	 * Script arguments.
	 */
    List<Argument> args;

    /**
	 * @return the scriptName
	 */
    public String getScriptName() {
        return scriptName;
    }

    /**
	 * @param scriptName the scriptName to set
	 */
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
	 * @return the args
	 */
    public List<Argument> getArgs() {
        return args;
    }

    /**
	 * @param args the args to set
	 */
    public void setArgs(List<Argument> args) {
        this.args = args;
    }
}
