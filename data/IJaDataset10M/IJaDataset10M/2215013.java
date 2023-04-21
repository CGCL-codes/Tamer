package deesel.parser.com.nodes;

import deesel.lang.module.Module;
import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.exceptions.COMIntegrityException;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.exceptions.GeneralParserFailureException;
import deesel.util.logging.Logger;

/**
 * AdaptiveTypes will blow your mind! They are a type that do not decide their
 * actual type until they are used. If you attempt to ask any information about
 * them they immediately take on the form of the default type they are given. If
 * however you use them for an assignment for example then the caller can change
 * the underlying type and then reparse the code.
 * <p/>
 * This is a bit of a mind mangler, but basically in the example of closures it
 * means that you can pass back to the closure the type it should be.
 *
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class AdaptiveType extends AbstractCOMNode implements DeeselClass {

    private static Logger log = Logger.getLogger(AdaptiveType.class);

    DeeselClass actualType;

    private boolean concrete;

    private String adaptiveName = "<adaptive class not made concrete>";

    public boolean equals(Object obj) {
        return actualType.equals(obj);
    }

    public AdaptiveType(ASTNode astNode, DeeselClass actualType) {
        super(astNode);
        this.actualType = actualType;
    }

    public int hashCode() {
        return actualType.hashCode();
    }

    public DeeselClass getActualType() {
        return actualType;
    }

    public void setActualType(DeeselClass actualType) {
        if (isConcrete()) {
            throw new GeneralParserFailureException("Tried to change the type of a concreted adaptive type.");
        }
        this.actualType = actualType;
    }

    public boolean isConcrete() {
        return concrete;
    }

    public void setConcreteRecursively(boolean concrete) {
        log.debug("** Adaptive type '" + getClass().getName() + "' made concrete (recursively) as " + getFullName() + ". **");
        this.concrete = concrete;
        if (actualType instanceof AdaptiveType) {
            ((AdaptiveType) actualType).setConcreteRecursively(concrete);
        }
    }

    public void setConcrete(boolean concrete) {
        log.debug("** Adaptive type '" + getClass().getName() + "' made concrete as " + getFullName() + ". **");
        this.concrete = concrete;
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public DeeselClass getType() {
        return this;
    }

    public COMNode[] getAllChildren() {
        failIfNotConcrete();
        return actualType.getAllChildren();
    }

    private void failIfNotConcrete() {
        if (!concrete) {
            fail("You attempted to acquire information about an adaptive type before it was made concrete.");
        }
    }

    public String getEmptyValue() {
        failIfNotConcrete();
        return actualType.getEmptyValue();
    }

    public boolean isPrimitive() {
        failIfNotConcrete();
        return actualType.isPrimitive();
    }

    public boolean isPublic() {
        failIfNotConcrete();
        return actualType.isPublic();
    }

    public boolean isPrivate() {
        failIfNotConcrete();
        return actualType.isPrivate();
    }

    public boolean isProtected() {
        failIfNotConcrete();
        return actualType.isProtected();
    }

    public boolean isAbstract() {
        failIfNotConcrete();
        return actualType.isAbstract();
    }

    public boolean isStatic() {
        failIfNotConcrete();
        return actualType.isStatic();
    }

    public boolean isFinal() {
        failIfNotConcrete();
        return actualType.isFinal();
    }

    public boolean isStrict() {
        failIfNotConcrete();
        return actualType.isStrict();
    }

    public DeeselField[] getFields() {
        failIfNotConcrete();
        return actualType.getFields();
    }

    public boolean isAdaptable() {
        return !isConcrete();
    }

    public DeeselPackage getPackage() {
        failIfNotConcrete();
        return actualType.getPackage();
    }

    public DeeselClass[] getClasses() {
        failIfNotConcrete();
        return actualType.getClasses();
    }

    public Module getModule() {
        failIfNotConcrete();
        return actualType.getModule();
    }

    public DeeselClass[] getExtensionClassesForClass(DeeselClass clazz) {
        return actualType.getExtensionClassesForClass(clazz);
    }

    public Requires[] getRequiresList() {
        return actualType.getRequiresList();
    }

    public DeeselClass getUnboxType() {
        failIfNotConcrete();
        return actualType.getUnboxType();
    }

    public DeeselClass getAutoboxType() {
        failIfNotConcrete();
        return actualType.getAutoboxType();
    }

    public DeeselClass getFieldType(String name) throws NoSuchFieldException {
        failIfNotConcrete();
        return actualType.getFieldType(name);
    }

    public DeeselClass getMethodReturnType(String name) throws NoSuchMethodException {
        failIfNotConcrete();
        return actualType.getMethodReturnType(name);
    }

    public String getName() {
        return actualType.getName();
    }

    public String getFullName() {
        return actualType.getFullName();
    }

    public DeeselClass getSuperClass() {
        failIfNotConcrete();
        return actualType.getSuperClass();
    }

    public DeeselMethod getMethod(String name, DeeselClass[] parameterTypes) throws NoSuchMethodException {
        failIfNotConcrete();
        return actualType.getMethod(name, parameterTypes);
    }

    public boolean isArray() {
        failIfNotConcrete();
        return actualType.isArray();
    }

    public boolean isInterface() {
        failIfNotConcrete();
        return actualType.isInterface();
    }

    public Constructor[] getDeclaredConstructors() {
        failIfNotConcrete();
        return actualType.getDeclaredConstructors();
    }

    public DeeselClass[] getConstructorExceptionTypes(DeeselClass[] parameters) throws NoSuchMethodException {
        failIfNotConcrete();
        return actualType.getConstructorExceptionTypes(parameters);
    }

    public Constructor getConstructor(DeeselClass[] parameters) throws NoSuchMethodException {
        failIfNotConcrete();
        return actualType.getConstructor(parameters);
    }

    public Object clone() throws CloneNotSupportedException {
        failIfNotConcrete();
        return actualType.clone();
    }

    public int getArrayDepth() {
        failIfNotConcrete();
        return actualType.getArrayDepth();
    }

    public boolean isInstance(DeeselClass parameter) {
        return actualType.isInstance(parameter);
    }

    public DeeselClass getArrayOf(int depth) {
        failIfNotConcrete();
        return actualType.getArrayOf(depth);
    }

    public DeeselMethod[] getDeclaredMethods() {
        failIfNotConcrete();
        return actualType.getDeclaredMethods();
    }

    public DeeselClass getComponentType(int depth) {
        failIfNotConcrete();
        return actualType.getComponentType(depth);
    }

    public DeeselClass getComponentType() {
        return actualType.getComponentType();
    }

    public String getClassName() {
        return actualType.getClassName();
    }

    public DeeselField getField(String name) throws NoSuchFieldException {
        return actualType.getField(name);
    }

    public String getLocalRefName() {
        return actualType.getLocalRefName();
    }

    public boolean canAdaptTo(DeeselClass destClass) {
        return !concrete;
    }

    public DeeselClass[] getInterfaces() {
        failIfNotConcrete();
        return actualType.getInterfaces();
    }

    public DeeselClass getMutualParent(DeeselClass rhsType) {
        return actualType.getMutualParent(rhsType);
    }

    public void adaptTo(DeeselClass returnType) {
        log.debug("Adapting.");
        setActualType(returnType);
        setConcrete(true);
    }

    public String getAdaptiveName() {
        return adaptiveName;
    }

    public void setAdaptiveName(String adaptiveName) {
        this.adaptiveName = adaptiveName;
    }

    public void validate() throws COMIntegrityException {
        super.validate();
        if (!isConcrete()) {
            fail("This type is never used.");
        }
    }
}
