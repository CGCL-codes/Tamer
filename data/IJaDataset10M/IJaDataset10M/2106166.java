package net.sf.betterj.model.compile.method;

import net.sf.betterj.model.compile.type.IResetable;
import net.sf.betterj.model.parameter.IParameter;

/**
 * @author Viktor Halitsyn
 */
public interface IMethodParameterCompiler extends IResetable {

    String compile(IParameter parameter);
}
