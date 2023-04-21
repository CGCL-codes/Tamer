package net.sf.betterj.model.compile.type;

import net.sf.betterj.model.type.ICustomType;

/**
 * @author Oleh Sklyarenko
 */
public interface ICustomTypeCompiler extends IResetable {

    void compile(ICustomType customType);

    void reset();
}
