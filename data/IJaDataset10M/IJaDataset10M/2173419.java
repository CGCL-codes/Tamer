package net.sourceforge.javautil.bytecode.api.type;

import net.sourceforge.javautil.bytecode.BytecodeCompiler;
import net.sourceforge.javautil.bytecode.api.TypeMemberAccess;
import net.sourceforge.javautil.bytecode.api.TypeMemberAccess.Scope;

/**
 * A concrete class that can be instantiated.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JavaClassConcrete<C extends BytecodeContextType> extends JavaClass<C> {

    public JavaClassConcrete(BytecodeCompiler compiler, String name, Scope scope, boolean isStatic, boolean isFinal) {
        super(compiler, name, new TypeMemberAccess(scope, false, isStatic, isFinal));
    }

    @Override
    public JavaClassConcrete cloneAs(String name, boolean isStatic, boolean isFinal) {
        JavaClassConcrete jcc = new JavaClassConcrete(compiler, name, access.getScope(), isStatic, isFinal);
        this.internalClone(jcc);
        return jcc;
    }
}
