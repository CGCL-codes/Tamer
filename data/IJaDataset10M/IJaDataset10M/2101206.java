package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.beanutils.MethodUtils;

public class TestEnum {

    public void setAEnum(AEnum e) {
        System.out.println("What you set is " + e.toString());
    }

    public static void main(String[] args) throws Exception {
        TestEnum e = new TestEnum();
        Method[] methods = e.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Class[] clazzs = method.getParameterTypes();
            for (int j = 0; j < clazzs.length; j++) {
                Class class1 = clazzs[j];
                if (class1.isEnum()) {
                    Object obj = Enum.valueOf(class1, "A");
                    MethodUtils.invokeMethod(e, "setAEnum", obj);
                }
                System.out.println();
            }
        }
    }
}
