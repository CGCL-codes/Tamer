package com.huntersoftwaregroup.gda.test;

import java.lang.reflect.*;

/**
 * Created by IntelliJ IDEA.
 * User: hasani
 * Date: Jan 30, 2007
 * Time: 1:09:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestValidation {

    public static void main(String[] args) {
        TestValidation validation = new TestValidation();
    }

    /**
     * Please comment this
     *
     * @created 02/20/07
     * @call-tree <pre>
     * java.lang.reflect.GenericDeclaration.getTypeParameters()
     * java.lang.reflect.TypeVariable.getName()
     * java.lang.Class.getTypeParameters()
     * java.lang.reflect.TypeVariable.getBounds()
     * com.huntersoftwaregroup.gda.test.TestValidationDelegate.validateForSave(SomeOtherTestGenericDataObject)
     * com.huntersoftwaregroup.gda.test.TestValidationDelegate.TestValidationDelegate()
     * java.io.PrintStream.println(java.lang.String)
     * java.lang.Object.getClass()
     * java.lang.Class.getMethod(java.lang.String,java.lang.Class...)
     * java.lang.reflect.Method.invoke(java.lang.Object,java.lang.Object...)
     * java.lang.reflect.Method.getTypeParameters()
     * com.huntersoftwaregroup.gda.test.SomeOtherTestGenericDataObject.SomeOtherTestGenericDataObject()
     * java.lang.reflect.Method.getName()
     * java.lang.reflect.TypeVariable.getGenericDeclaration()
     * java.io.PrintStream.println()
     * java.lang.Class.getDeclaredMethods()
     * java.lang.Object.Object()
     * java.lang.Throwable.printStackTrace()
     * </pre>
     */
    public TestValidation() {
        TestValidationDelegate validationObj = new TestValidationDelegate();
        SomeOtherTestGenericDataObject t = new SomeOtherTestGenericDataObject();
        Object obj = new Object();
        validationObj.validateForSave(t);
        Class c = validationObj.getValidationTypeClass();
        TypeVariable<? extends Class<? extends TestValidationDelegate>>[] var = validationObj.getClass().getTypeParameters();
        try {
            Method[] m = validationObj.getClass().getDeclaredMethods();
            Method method = validationObj.getClass().getMethod("validateForRemoval", Object.class);
            Class<?>[] o = method.getParameterTypes();
            method.invoke(validationObj, obj);
            if (method != null) {
                System.out.println(method.getName());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        for (TypeVariable currentVar : var) {
            String name = currentVar.getName();
            Type[] typeArray = currentVar.getBounds();
            GenericDeclaration className = currentVar.getGenericDeclaration();
            TypeVariable<?>[] var2 = currentVar.getGenericDeclaration().getTypeParameters();
            System.out.println();
        }
    }
}
