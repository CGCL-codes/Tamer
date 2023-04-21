package net.sf.opendf.cal.interpreter.environment;

import net.sf.opendf.cal.interpreter.Context;

/**
 * An environment factory constructs an environment frame that works 
 * with a particular, specified context.
 * 
 * @author jornj
 */
public interface EnvironmentFactory {

    Environment createEnvironment(Environment env, Context context);
}
