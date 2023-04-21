package net.sourceforge.jdefprog.mcl.interpret.context.type;

import net.sourceforge.jdefprog.mcl.interpret.context.Context;
import net.sourceforge.jdefprog.reflection.*;
import net.sourceforge.jdefprog.types.ClassDesc;

/**
 * 
 * Note that the value "null" is indicated as type of "null" values.
 * 
 * @author Federico Tomassetti (f.tomassetti@gmail.com)
 */
public interface TypedContext extends Context<ClassDesc, NotExecBehaviorInfo> {
}
