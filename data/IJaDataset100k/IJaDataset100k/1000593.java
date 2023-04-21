package recoder.abstraction;

import java.util.List;
import recoder.ModelException;
import recoder.service.ImplicitElementInfo;
import recoder.service.ProgramModelInfo;

/**
 * @author Tobias
 *
 */
public class ErasedMethod implements Method {

    private final Method genericMethod;

    private final ImplicitElementInfo service;

    /**
	 * 
	 */
    public ErasedMethod(Method genericMethod, ImplicitElementInfo service) {
        this.service = service;
        this.genericMethod = genericMethod;
        assert !(genericMethod instanceof ErasedMethod || genericMethod instanceof ParameterizedMethod);
    }

    public Method getGenericMethod() {
        return genericMethod;
    }

    public List<ClassType> getExceptions() {
        return service.getExceptions(this);
    }

    public Type getReturnType() {
        return service.getReturnType(this);
    }

    public List<Type> getSignature() {
        return service.getSignature(this);
    }

    public List<? extends TypeParameter> getTypeParameters() {
        return genericMethod.getTypeParameters();
    }

    public boolean isAbstract() {
        return genericMethod.isAbstract();
    }

    public boolean isNative() {
        return genericMethod.isNative();
    }

    public boolean isSynchronized() {
        return genericMethod.isSynchronized();
    }

    public boolean isVarArgMethod() {
        return genericMethod.isVarArgMethod();
    }

    public List<? extends AnnotationUse> getAnnotations() {
        return getGenericMethod().getAnnotations();
    }

    public ClassType getContainingClassType() {
        return genericMethod.getContainingClassType().getErasedType();
    }

    public boolean isFinal() {
        return genericMethod.isFinal();
    }

    public boolean isPrivate() {
        return genericMethod.isPrivate();
    }

    public boolean isProtected() {
        return genericMethod.isProtected();
    }

    public boolean isPublic() {
        return genericMethod.isPublic();
    }

    public boolean isStatic() {
        return genericMethod.isStatic();
    }

    public boolean isStrictFp() {
        return genericMethod.isStrictFp();
    }

    public String getFullName() {
        return genericMethod.getFullName();
    }

    public String getBinaryName() {
        return genericMethod.getBinaryName();
    }

    public ImplicitElementInfo getProgramModelInfo() {
        return service;
    }

    public void setProgramModelInfo(ProgramModelInfo pmi) {
        throw new RuntimeException();
    }

    public String getName() {
        return genericMethod.getName();
    }

    public void validate() throws ModelException {
    }

    public ClassTypeContainer getContainer() {
        return genericMethod.getContainer();
    }

    public Package getPackage() {
        return genericMethod.getPackage();
    }

    public List<? extends ClassType> getTypes() {
        return service.getTypes(this);
    }

    @Override
    public String toString() {
        return genericMethod.toString() + "%ERASED%";
    }

    @Override
    public Method getGenericMember() {
        return genericMethod;
    }
}
