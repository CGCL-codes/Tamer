package pl.wcislo.sbql4j.tools.apt.mirror.type;

import java.util.Collection;
import pl.wcislo.sbql4j.mirror.declaration.TypeDeclaration;
import pl.wcislo.sbql4j.mirror.type.DeclaredType;
import pl.wcislo.sbql4j.mirror.type.InterfaceType;
import pl.wcislo.sbql4j.mirror.type.TypeMirror;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.TypeTags;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;

/**
 * Implementation of DeclaredType
 */
abstract class DeclaredTypeImpl extends TypeMirrorImpl implements DeclaredType {

    protected Type.ClassType type;

    protected DeclaredTypeImpl(AptEnv env, Type.ClassType type) {
        super(env, type);
        this.type = type;
    }

    /**
     * Returns a string representation of this declared type.
     * This includes the type's name and any actual type arguments.
     * Type names are qualified.
     */
    public String toString() {
        return toString(env, type);
    }

    /**
     * {@inheritDoc}
     */
    public TypeDeclaration getDeclaration() {
        return env.declMaker.getTypeDeclaration((ClassSymbol) type.tsym);
    }

    /**
     * {@inheritDoc}
     */
    public DeclaredType getContainingType() {
        if (type.getEnclosingType().tag == TypeTags.CLASS) {
            return (DeclaredType) env.typeMaker.getType(type.getEnclosingType());
        }
        ClassSymbol enclosing = type.tsym.owner.enclClass();
        if (enclosing != null) {
            return (DeclaredType) env.typeMaker.getType(env.jctypes.erasure(enclosing.type));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<TypeMirror> getActualTypeArguments() {
        return env.typeMaker.getTypes(type.getTypeArguments());
    }

    /**
     * {@inheritDoc}
     */
    public Collection<InterfaceType> getSuperinterfaces() {
        return env.typeMaker.getTypes(env.jctypes.interfaces(type), InterfaceType.class);
    }

    /**
     * Returns a string representation of this declared type.
     * See {@link #toString()} for details.
     */
    static String toString(AptEnv env, Type.ClassType c) {
        return c.toString();
    }
}
