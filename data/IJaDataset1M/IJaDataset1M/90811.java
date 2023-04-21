package pl.wcislo.sbql4j.tools.apt.mirror.type;

import pl.wcislo.sbql4j.mirror.declaration.TypeParameterDeclaration;
import pl.wcislo.sbql4j.mirror.type.TypeVariable;
import pl.wcislo.sbql4j.mirror.util.TypeVisitor;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.TypeSymbol;

/**
 * Implementation of TypeVariable
 */
public class TypeVariableImpl extends TypeMirrorImpl implements TypeVariable {

    protected Type.TypeVar type;

    TypeVariableImpl(AptEnv env, Type.TypeVar type) {
        super(env, type);
        this.type = type;
    }

    /**
     * Returns the simple name of this type variable.  Bounds are
     * not included.
     */
    public String toString() {
        return type.tsym.name.toString();
    }

    /**
     * {@inheritDoc}
     */
    public TypeParameterDeclaration getDeclaration() {
        TypeSymbol sym = (TypeSymbol) type.tsym;
        return env.declMaker.getTypeParameterDeclaration(sym);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(TypeVisitor v) {
        v.visitTypeVariable(this);
    }
}
