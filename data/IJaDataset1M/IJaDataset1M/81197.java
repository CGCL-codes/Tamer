package de.fzi.injectj.model;

import java.util.ArrayList;
import java.util.List;
import de.fzi.injectj.language.CodeMapper;
import de.fzi.injectj.model.exception.InvalidIdentifierException;
import de.fzi.injectj.model.exception.ModelException;
import de.fzi.injectj.model.exception.TypeNotFoundException;
import de.fzi.injectj.script.model.Type;

/**
 * @author kuttruff
 *
 * @inject export name=Array package=lang.weavepoints
 */
public abstract class ArrayWeavepoint extends AbstractWeavepoint implements TypeWeavepoint {

    /**
	 * Returns the type of the components of the array.
	 * If the array is multidimensional, a type with
	 * dimension == getDimensions()-1 is returned.
	 * @return TypeWeavepoint
	 * @inject export modifier=query
	 */
    public abstract TypeWeavepoint getComponentType();

    /**
	 * Returns the type of one single element in the array.
	 * If the array is multidimensional, the type with dimension == 0 is returned. 
	 * @return TypeWeavepoint
	 * @inject export modifier=query
	 */
    public abstract TypeWeavepoint getElementType();

    /**
	 * @inject export modifier=query
	 */
    public abstract int getDimensions();

    public boolean isLocalType() {
        return false;
    }

    public boolean isAnonymousClass() {
        return false;
    }

    public List getMemberTypes() {
        return new ArrayList(1);
    }

    public TypeWeavepoint getMemberType(String unqualifiedName) {
        return null;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#isArrayType()
	 */
    public boolean isArrayType() {
        return true;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#isClass()
	 */
    public boolean isClass() {
        return false;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#isInterface()
	 */
    public boolean isInterface() {
        return false;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#isPrimitiveType()
	 */
    public boolean isPrimitiveType() {
        return false;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.script.model.Type#getAskIdentifier()
	 */
    public String getAskIdentifier() {
        return getFullName();
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.script.model.Type#getTypeID()
	 */
    public int getTypeID() {
        return Type.ARRAYMASK | ((Type) getComponentType()).getTypeID();
    }

    public boolean isNestedType() {
        return false;
    }

    public void addToExtends(String name) throws ModelException {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addToExtends", getName());
        System.err.println(message);
    }

    public boolean existsAttribute(String name, boolean includeInherited) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "existsAttribute", getName());
        System.err.println(message);
        return false;
    }

    public List getAllSubTypes() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAllSubTypes", getName());
        System.err.println(message);
        return null;
    }

    public List getDirectSubTypes() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getDirectSubTypes", getName());
        System.err.println(message);
        return null;
    }

    public List getHierarchy() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getHierarchy", getName());
        System.err.println(message);
        return null;
    }

    public void addMemberClass(String modifierList, String className, FragmentType source) throws ModelException {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addMemberClass", getName());
        System.err.println(message);
    }

    public void addMemberInterface(String modifierList, String interfaceName, FragmentType source) throws ModelException {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addMemberInterface", getName());
        System.err.println(message);
    }

    public boolean canAddMemberClass(String modifierList, String className, FragmentType source) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "canAddMemberClass", getName());
        System.err.println(message);
        return false;
    }

    public boolean canAddMemberInterface(String modifierList, String interfaceName, FragmentType source) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "canAddMemberInterface", getName());
        System.err.println(message);
        return false;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getAbstractMethods()
	 */
    public List getAbstractMethods() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAbstractMethods", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getAllSuperTypes()
	 */
    public List getAllSuperTypes() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAllSuperTypes", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getDirectSuperTypes()
	 */
    public List getDirectSuperTypes() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getDirectSuperTypes", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @param i
	 * @throws ModelException
	 * @see de.fzi.injectj.model.TypeWeavepoint#addImport(java.lang.String)
	 */
    public void addImport(String i) throws ModelException {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addImport", getName());
        System.err.println(message);
    }

    /** 
	 * @param typeRef
	 * @throws ModelException
	 * @see de.fzi.injectj.model.TypeWeavepoint#addToExtends(de.fzi.injectj.model.TypeWeavepoint)
	 */
    public void addToExtends(TypeWeavepoint typeRef) throws ModelException {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addToExtends", getName());
        System.err.println(message);
    }

    /** 
	 * @param signature
	 * @param includeInherited
	 * @return
	 * @throws InvalidIdentifierException
	 * @see de.fzi.injectj.model.TypeWeavepoint#existsMethod(java.lang.String, boolean)
	 */
    public boolean existsMethod(String signature, boolean includeInherited) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "existsMethod", getName());
        System.err.println(message);
        return false;
    }

    /** 
	 * @param name
	 * @return
	 * @throws InvalidIdentifierException
	 * @see de.fzi.injectj.model.TypeWeavepoint#getAttribute(java.lang.String)
	 */
    public AttributeWeavepoint getAttribute(String name) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAttribute", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getAttributes()
	 */
    public List getAttributes() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAttributes", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @param match
	 * @param complementary
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getAttributes(java.lang.String, boolean)
	 */
    public List getAttributes(String match, boolean complementary) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getAttributes", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @param signature
	 * @return
	 * @throws InvalidIdentifierException
	 * @throws TypeNotFoundException
	 * @see de.fzi.injectj.model.TypeWeavepoint#getMethod(java.lang.String)
	 */
    public MethodWeavepoint getMethod(String signature) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getMethod", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getMethods()
	 */
    public List getMethods() {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getMethods", getName());
        System.err.println(message);
        return null;
    }

    /** 
	 * @param match
	 * @param complementary
	 * @return
	 * @see de.fzi.injectj.model.TypeWeavepoint#getMethods(java.lang.String, boolean)
	 */
    public List getMethods(String match, boolean complementary) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "getMethods", getName());
        System.err.println(message);
        return null;
    }

    public boolean inheritsFrom(TypeWeavepoint type) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "inheritsFrom", getName());
        System.err.println(message);
        return false;
    }

    public boolean inheritsFrom(String typeName) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "inheritsFrom", getName());
        System.err.println(message);
        return false;
    }

    public MethodWeavepoint addMethod(FragmentType javaSource) {
        String message = CodeMapper.getText("COMMAND_NOT_SUPPORTED", "addMethod", getName());
        System.err.println(message);
        return null;
    }
}
