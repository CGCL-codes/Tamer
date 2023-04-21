package com.beanstalktech.common.script;

import com.beanstalktech.common.context.*;

/**
 * Specification of a Beanstalk Component Scripting Language
 * (BCSL) block start command token. A block start command is any command
 * that controls execution of a succeeding block of commands.
 * <P>
 * @author Stuart Sugarman/Beanstalk Technologies LLC
 * @version 1.0 1/8/2002
 * @since Beanstalk V2.1
 */
public interface BlockStartScriptToken {

    /**
     * Evaluates the token's value based on its qualifiers.
     *
     * @param evt Application event for the current connection.
     * @param scriptcontext Context for the current script.
     * @param currentcontext The context to be used as the basis for variable substitution.
     * Variable names with no qualifiers are expected to exist in this context.
     * @param hint An (optional )object providing additional
     * information about the current script.
     * @return The boolean result of the evaluation.
     */
    public boolean evaluate(AppEvent evt, Context scriptContext, Context currentContext, Object hint) throws ScriptException;

    /**
     * Resets the block. Used primarily to reset counters in iterating
     * blocks.
     */
    public void reset();
}
