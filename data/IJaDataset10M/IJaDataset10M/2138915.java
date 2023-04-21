package jse.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Swang
 */
public class ReflectDemo {

    public static void main(String[] args) {
        try {
            Class personClass = Class.forName("jse.reflect.Person");
            System.out.println("Name: " + personClass.getName());
            System.out.println("SimpleName: " + personClass.getSimpleName());
            System.out.println("Array SimpleName: " + (new boolean[3][4][5][6][7][8][9]).getClass().getSimpleName());
            Field[] fields = personClass.getFields();
            System.out.println("Field Length: " + fields.length);
            Field[] dFields = personClass.getDeclaredFields();
            System.out.println("DField Length: " + dFields.length);
            for (Field df : dFields) {
                System.out.println("Name: " + df.getName());
                System.out.println("Type Name: " + df.getType().getSimpleName());
                Type type = df.getGenericType();
                System.out.println("type: " + type);
                if (type instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) type;
                    System.out.println("raw type: " + pt.getRawType());
                    System.out.println("owner type: " + pt.getOwnerType());
                    System.out.println("actual type args:");
                    for (Type t : pt.getActualTypeArguments()) {
                        System.out.println("    " + t);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReflectDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
