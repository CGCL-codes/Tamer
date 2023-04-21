package com.ibatis.sqlmap.engine.accessplan;

import java.util.Map;

/**
 * Factory to get an accesss plan appropriate for an object
 */
public class AccessPlanFactory {

    private static boolean bytecodeEnhancementEnabled = false;

    private AccessPlanFactory() {
    }

    /**
   * Creates an access plan for working with a bean
   *
   * @param clazz
   * @param propertyNames
   * @return An access plan
   */
    public static AccessPlan getAccessPlan(Class clazz, String[] propertyNames) {
        AccessPlan plan;
        boolean complex = false;
        if (clazz == null || propertyNames == null) {
            complex = true;
        } else {
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].indexOf('[') > -1 || propertyNames[i].indexOf('.') > -1) {
                    complex = true;
                    break;
                }
            }
        }
        if (complex) {
            plan = new ComplexAccessPlan(clazz, propertyNames);
        } else if (Map.class.isAssignableFrom(clazz)) {
            plan = new MapAccessPlan(clazz, propertyNames);
        } else {
            if (bytecodeEnhancementEnabled) {
                try {
                    plan = new EnhancedPropertyAccessPlan(clazz, propertyNames);
                } catch (Throwable t) {
                    try {
                        plan = new PropertyAccessPlan(clazz, propertyNames);
                    } catch (Throwable t2) {
                        plan = new ComplexAccessPlan(clazz, propertyNames);
                    }
                }
            } else {
                try {
                    plan = new PropertyAccessPlan(clazz, propertyNames);
                } catch (Throwable t) {
                    plan = new ComplexAccessPlan(clazz, propertyNames);
                }
            }
        }
        return plan;
    }

    /**
   * Tells whether or not bytecode enhancement (CGLIB, etc) is enabled
   *
   * @return true if bytecode enhancement is enabled
   */
    public static boolean isBytecodeEnhancementEnabled() {
        return bytecodeEnhancementEnabled;
    }

    /**
   * Turns on or off bytecode enhancement (CGLIB, etc)
   *
   * @param bytecodeEnhancementEnabled - the switch
   */
    public static void setBytecodeEnhancementEnabled(boolean bytecodeEnhancementEnabled) {
        AccessPlanFactory.bytecodeEnhancementEnabled = bytecodeEnhancementEnabled;
    }
}
