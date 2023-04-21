package com.ezsoft.ezpersistence;

import java.lang.reflect.*;
import java.beans.*;
import java.sql.*;

/**
 * <p>Title:        Persistence</p>
 * <p>  Description:</p>
 * <p> Copyright:    Copyright (c) 2001</p>
 * <p>  Company:      http://www.ez-softinc.com</p>
 * @author Michael Lee
 * @version 0.9
 */
public class ReflectionTest {

    private String stringOne = "one";

    public ReflectionTest(Object stringTwo) {
        if (stringTwo.getClass().getName() == "java.lang.Integer") {
            this.stringOne = ((Integer) stringTwo).toString();
            System.out.println("Stringone:" + stringOne);
        }
        if (stringTwo.getClass().getName() == "java.lang.String") {
            this.stringOne = (String) stringTwo;
        }
    }

    public void setStringOne(String stringOne) {
        this.stringOne = stringOne;
    }

    public String getStringOne() {
        return stringOne;
    }

    public void testReflection() {
        System.out.println("Get Class(getName): " + this.getClass().getName());
        Constructor constructor[] = this.getClass().getConstructors();
        System.out.println("Get Class : " + constructor[0]);
        System.out.println("Get Class (getConstructors): " + constructor[0].getName());
        Class classes[] = constructor[0].getParameterTypes();
        System.out.println("Get Class: (constructorParameterLength)" + classes.length);
        System.out.println("Get Class: (constructorParameterName)" + classes[0].getName());
        Method method[] = this.getClass().getDeclaredMethods();
        for (int loop = 0; loop < method.length; loop++) {
            System.out.println("Methods getName " + loop + ":" + method[loop].getName());
            System.out.println("Methods " + loop + ":" + method[loop]);
        }
        Field field[] = this.getClass().getDeclaredFields();
        for (int loop = 0; loop < field.length; loop++) {
            System.out.println("Fields getName " + loop + ":" + field[loop].getName());
            System.out.println("Fields " + loop + ":" + field[loop]);
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), this.getClass().getSuperclass());
            MethodDescriptor md[] = beanInfo.getMethodDescriptors();
            for (int loop = 0; loop < md.length; loop++) {
                System.out.println("Get Class: (getMethodDescriptor)" + loop + " : " + md[loop].getName());
                System.out.println("Get Class: (getDisplayName)" + loop + " : " + md[loop].getDisplayName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            OneReflectionTest one = new OneReflectionTest(new Integer(10));
            Thread.sleep(6000);
            one.create();
            Thread.sleep(4000);
            one.setOne(new Integer(9));
            one.setTwo("twoooo");
            one.setThree("threeee");
            one.update();
            Thread.sleep(4000);
        } catch (PersistenceError ex) {
            ex.printStackTrace();
            System.out.println(ex.getErrorMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
