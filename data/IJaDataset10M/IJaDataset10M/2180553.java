package net.sourceforge.jdefprog.reflection.javarefl;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import javax.lang.model.type.TypeKind;
import net.sourceforge.jdefprog.reflection.AbstractMethod;
import net.sourceforge.jdefprog.reflection.TypeVariable;
import net.sourceforge.jdefprog.reflection.elementsrefl.TypeKindUtils;
import net.sourceforge.jdefprog.types.ClassDesc;
import net.sourceforge.jdefprog.types.ClassDescImpl;

/**
 * Information on a method obtained from the Java reflection.
 * 
 * @author Federico Tomassetti (f.tomassetti@gmail.com)
 */
class JrMethod extends AbstractMethod {

    private Method wrapped;

    public JrMethod(Method wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ClassDesc getDeclaringType() {
        return new ClassDescImpl(wrapped.getDeclaringClass().getCanonicalName());
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public ClassDesc[] getParamTypes() {
        List<ClassDesc> params = new LinkedList<ClassDesc>();
        for (Class<?> p : wrapped.getParameterTypes()) {
            params.add(new ClassDescImpl(p.getCanonicalName()));
        }
        return params.toArray(new ClassDesc[] {});
    }

    @Override
    public ClassDesc getReturnType() {
        return new ClassDescImpl(wrapped.getReturnType().getCanonicalName());
    }

    @Override
    public TypeKind getReturnTypeKind() {
        return TypeKindUtils.get(getReturnType());
    }

    @Override
    public int getModifiers() {
        return wrapped.getModifiers();
    }

    @Override
    public boolean isInInterface() {
        return wrapped.getDeclaringClass().isInterface();
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        throw new RuntimeException("Not implemented");
    }
}
