package ru.jnano.math.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.jnano.math.utils.PropertyUtilsBean;

public class PropertyUtilsBeanTimeWriteTest {

    private static final int NUMLOOPS = 1000000000;

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Model model = new Model();
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i <= NUMLOOPS; i++) {
            model.setSimpleIntField(1000);
        }
        System.out.println("Time write direct: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + model.getSimpleIntField());
        System.out.println("-------------------------------------------------------------------");
        currentTime = System.currentTimeMillis();
        Method writeMethod = PropertyUtilsBean.getSimplePropertyWriteMethod(model, "simpleIntField");
        writeMethod.setAccessible(true);
        Integer integer = new Integer(1002);
        for (int i = 0; i <= NUMLOOPS; i++) {
            writeMethod.invoke(model, integer);
        }
        System.out.println("Time write runtime: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + model.getSimpleIntField());
        System.out.println("-------------------------------------------------------------------");
        currentTime = System.currentTimeMillis();
        writeMethod = Model.class.getDeclaredMethod("setSimpleIntField", int.class);
        writeMethod.setAccessible(true);
        integer = new Integer(1002);
        for (int i = 0; i <= NUMLOOPS; i++) {
            writeMethod.invoke(model, integer);
        }
        System.out.println("Time write runtime: " + (System.currentTimeMillis() - currentTime) + " ��. Value=" + model.getSimpleIntField());
        System.out.println("-------------------------------------------------------------------");
    }
}
