package ru.jnano.math.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.jnano.math.utils.PropertyUtilsBean;

public class PropertyUtilsBeanTimeReadTest {

    private static final int NUMLOOPS = 100000000;

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Model model = new Model();
        int value = 0;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i <= NUMLOOPS; i++) {
            value = model.getSimpleIntField();
        }
        System.out.println("Time read direct: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + value);
        System.out.println("-------------------------------------------------------------------");
        currentTime = System.currentTimeMillis();
        for (int i = 0; i <= NUMLOOPS; i++) {
            value = (Integer) PropertyUtilsBean.getSimpleProperty(model, "simpleIntField");
        }
        System.out.println("Time read runtime: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + value);
        System.out.println("-------------------------------------------------------------------");
        currentTime = System.currentTimeMillis();
        Method readMethod = PropertyUtilsBean.getSimplePropertyReadMethod(model, "simpleIntField");
        for (int i = 0; i <= NUMLOOPS; i++) {
            value = (Integer) readMethod.invoke(model, new Object[0]);
        }
        System.out.println("Time read runtime: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + value);
        System.out.println("-------------------------------------------------------------------");
    }
}
