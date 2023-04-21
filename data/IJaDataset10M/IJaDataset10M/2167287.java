package com.google.gdt.eclipse.designer.moz.jsni;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import com.google.gdt.eclipse.designer.moz.jsni.LowLevelMoz32.DispatchMethod32;
import com.google.gdt.eclipse.designer.moz.jsni.LowLevelMoz32.DispatchObject32;
import com.google.gwt.dev.shell.CompilingClassLoader;
import com.google.gwt.dev.shell.JavaDispatch;
import com.google.gwt.dev.shell.JavaDispatchImpl;
import com.google.gwt.dev.shell.JsValue;
import com.google.gwt.dev.shell.JsValueGlue;
import com.google.gwt.dev.shell.MethodAdaptor;

/**
 * Wraps an arbitrary Java Object as a Dispatch component. The class was
 * motivated by the need to expose Java objects into JavaScript.
 * 
 * An instance of this class with no target is used to globally access all
 * static methods or fields.
 */
public class GeckoDispatchAdapter32 implements DispatchObject32 {

    private final CompilingClassLoader classLoader;

    private final JavaDispatch javaDispatch;

    /**
   * This constructor initializes as the static dispatcher, which handles only
   * static method calls and field references.
   * 
   * @param cl this class's classLoader
   */
    GeckoDispatchAdapter32(CompilingClassLoader cl) {
        javaDispatch = new JavaDispatchImpl(cl);
        classLoader = cl;
    }

    /**
   * This constructor initializes a dispatcher, around a particular instance.
   * 
   * @param cl this class's classLoader
   * @param target the object being wrapped as an IDispatch
   */
    GeckoDispatchAdapter32(CompilingClassLoader cl, Object target) {
        javaDispatch = new JavaDispatchImpl(cl, target);
        classLoader = cl;
    }

    /**
   * Retrieve a field and store in the passed JsValue. This function is called
   * exclusively from native code.
   * 
   * @param name name of the field to retrieve
   * @param jsValue a reference to the JsValue object to receive the value of
   *          the field
   */
    public void getField(String member, int jsRootedValue) {
        JsValueMoz32 jsValue = new JsValueMoz32(jsRootedValue);
        int dispId = getDispId(member);
        if (dispId < 0) {
            jsValue.setUndefined();
            return;
        }
        if (javaDispatch.isField(dispId)) {
            Field field = javaDispatch.getField(dispId);
            JsValueGlue.set(jsValue, classLoader, field.getType(), javaDispatch.getFieldValue(dispId));
            return;
        } else {
            MethodAdaptor method = javaDispatch.getMethod(dispId);
            AccessibleObject obj = method.getUnderlyingObject();
            DispatchMethod32 dispMethod = (DispatchMethod32) classLoader.getWrapperForObject(obj);
            if (dispMethod == null) {
                dispMethod = new MethodDispatch32(classLoader, method);
                classLoader.putWrapperForObject(obj, dispMethod);
            }
            jsValue.setWrappedFunction(method.toString(), dispMethod);
        }
    }

    public Object getTarget() {
        return javaDispatch.getTarget();
    }

    public void setField(String member, int jsRootedValue) {
        JsValue jsValue = new JsValueMoz32(jsRootedValue);
        int dispId = getDispId(member);
        if (dispId < 0) {
            throw new RuntimeException("No such field " + member);
        }
        if (javaDispatch.isMethod(dispId)) {
            throw new RuntimeException("Cannot reassign method " + member);
        }
        Field field = javaDispatch.getField(dispId);
        Object val = JsValueGlue.get(jsValue, classLoader, field.getType(), "setField");
        javaDispatch.setFieldValue(dispId, val);
    }

    private int getDispId(String member) {
        if (Character.isDigit(member.charAt(0))) {
            return Integer.valueOf(member);
        } else {
            return classLoader.getDispId(member);
        }
    }
}
