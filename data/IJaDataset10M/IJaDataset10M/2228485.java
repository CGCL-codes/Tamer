package jmetric.parser.java;

import java.util.Enumeration;
import java.util.Vector;
import jmetric.parser.ClassComplete;
import jmetric.util.StringCollection;

public class Metrics implements JParserInterface {

    private JClassCollection classCollection;

    private StringCollection importCollection;

    private JClass currentClass = null;

    private int currObjectType;

    private StringCollection packages;

    private String currentPackage;

    private ClassComplete classComplete;

    private int currPass = 0;

    public Metrics(ClassComplete to) {
        classCollection = new JClassCollection();
        currentClass = new JClass("DEFAULT");
        importCollection = new StringCollection();
        packages = new StringCollection();
        currentPackage = null;
        classComplete = to;
    }

    @SuppressWarnings("unchecked")
    public void tmpDisplay() {
        System.out.println("Number of Classes: " + classCollection.size());
        System.out.println("Methods by Class");
        Enumeration<JClass> e = classCollection.elements();
        while (e.hasMoreElements()) {
            JClass currClass = (JClass) e.nextElement();
            System.out.println("CLASS: " + currClass.getName());
            Enumeration<?> g = currClass.getMethods().elements();
            Method currMethod;
            while (g.hasMoreElements()) {
                currMethod = (Method) g.nextElement();
                System.out.println(currMethod.getName());
            }
        }
    }

    public void setClassAnonymous() {
        currentClass.setClassAnonymous();
    }

    @SuppressWarnings("unchecked")
    public void resolveChildren() {
        JClass childClass;
        JClass currClass;
        String superClassName;
        String childClassName;
        String currClassName;
        Enumeration<JClass> e = classCollection.elements();
        while (e.hasMoreElements()) {
            childClass = (JClass) e.nextElement();
            childClassName = childClass.getName();
            if (childClass.hasSuperClass()) {
                superClassName = childClass.getSuperClassName();
                for (int i = 0; i < classCollection.size(); i++) {
                    currClass = (JClass) classCollection.elementAt(i);
                    currClassName = currClass.getName();
                    if (superClassName.equals(currClassName)) {
                        currClass.addChildClass(childClass);
                        childClass.setSuperClass(currClass);
                    }
                }
            }
        }
    }

    public void setClassInterface() {
        currentClass.setInterface();
    }

    public void setClassName(String name) {
        currentClass.setName(name);
    }

    public void setClassStatic() {
        currentClass.setStatic();
    }

    public void setClassAbstract() {
        currentClass.setAbstract();
    }

    public void setClassFinal() {
        currentClass.setFinal();
    }

    public void setClassPublic() {
        currentClass.setPublic();
    }

    public void setClassPrivate() {
        currentClass.setPrivate();
    }

    public void setClassProtected() {
        currentClass.setProtected();
    }

    @SuppressWarnings("unchecked")
    public void startClass() {
        currentClass = new JClass("DEFAULT");
        classCollection.addElement(currentClass);
        if (currentPackage == null) {
            currentClass.addPackage("Unknown");
        } else {
            currentClass.addPackage(currentPackage);
        }
        Enumeration e = importCollection.elements();
        while (e.hasMoreElements()) {
            currentClass.addImportStatement((String) e.nextElement());
        }
    }

    public void endClass() {
        if (currentPackage == null) {
            packages.add("Unknown");
        }
        currentClass.resolveAllCalls();
        classComplete.classComplete(currentClass);
        currentClass.destroy();
        System.gc();
    }

    public void startInnerClass() {
        currentClass.startInnerClass();
    }

    public void endInnerClass() {
        currentClass.endInnerClass();
    }

    public void addPackage(String name) {
        packages.addUnique(name);
        currentPackage = name;
    }

    public void addMethod(String name, boolean isConstructor) {
        currentClass.addMethod(name, isConstructor);
    }

    public void setMethodIsInitializer() {
        currentClass.setMethodIsInitializer();
    }

    public void addMethodAttributes(boolean isStatic, boolean isAbstract, boolean isFinal, boolean isSynchronized, boolean isNative) {
        currentClass.addMethodAttributes(isStatic, isAbstract, isFinal, isSynchronized, isNative);
    }

    public void addLiteral(String type) {
        currentClass.addLiteral(type);
    }

    public void startBlock() {
        currentClass.startBlock();
    }

    public void endBlock() {
        currentClass.endBlock();
    }

    public void addPrefix(int type, String s) {
        currentClass.addPrefix(s, type);
    }

    public void addSuperClassCall() {
        currentClass.addSuperClassCall();
    }

    public void addSelfCall() {
        currentClass.addSelfCall();
    }

    public void addMethodVisibility(String type) {
        currentClass.addMethodVisibility(type);
    }

    public void addMethodReturnType(String type) {
        currentClass.addMethodReturnType(type);
    }

    public void addStatement(boolean isEmpty, int type) {
        currentClass.addStatement(isEmpty, type);
    }

    @SuppressWarnings("unchecked")
    public void addImportStatement(String name) {
        importCollection.addElement(new String(name));
    }

    public void addSuperClass(String name) {
        if (name.trim().length() > 0) currentClass.addSuperClass(name);
    }

    public void addInterface(String name) {
        currentClass.addInterface(name);
    }

    public void addMethodParameter() {
        currentClass.addMethodParameter();
    }

    public void addExceptionParameter() {
        currentClass.addExceptionParameter();
    }

    public void endMethod() {
        currentClass.endMethod();
    }

    public void setVarName(String n) {
        currentClass.setVarName(n);
    }

    public void setVarReturnType(String type) {
        currentClass.setVarReturnType(type);
    }

    public void setVarVisibility(String vis) {
        currentClass.setVarVisibility(vis);
    }

    public void setVarAttributes(boolean isStatic, boolean isFinal, boolean isTransient, boolean isVolatile) {
        currentClass.setVarAttributes(isStatic, isFinal, isTransient, isVolatile);
    }

    public void addInstanceVariable() {
        currentClass.addInstanceVariable();
    }

    public void addLocalVariable() {
        currentClass.addLocalVariable();
    }

    public void resetCurrentVariable() {
        currentClass.resetCurrentVariable();
    }

    public void setVarInitialised() {
        currentClass.setVarInitialised();
    }

    public void setVarIsArray() {
        currentClass.setVarIsArray();
    }

    public void resolveCalls() {
        Enumeration<JClass> e = classCollection.elements();
        while (e.hasMoreElements()) {
            JClass c = (JClass) e.nextElement();
            c.resolveAllCalls();
        }
    }

    public Vector<JClass> getClasses() {
        return classCollection;
    }

    public void reset() {
        importCollection.removeAllElements();
        System.gc();
        classCollection = new JClassCollection();
        currentClass = new JClass("<DEFAULT>");
        importCollection = new StringCollection();
        System.gc();
    }

    public int getImportCount() {
        return importCollection.size();
    }

    private void destroyClasses() {
        Enumeration<JClass> e = classCollection.elements();
        while (e.hasMoreElements()) {
            ((JClass) e.nextElement()).destroy();
        }
        classCollection.removeAllElements();
    }

    public StringCollection getPackages() {
        return packages;
    }
}
