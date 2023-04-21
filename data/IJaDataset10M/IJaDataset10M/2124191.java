package com.shenxg.test.reflect;

import java.lang.reflect.Field;
import java.util.List;

public class MainRun {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Test1Object o1 = new Test1Object();
        try {
            Field field = Test1Object.class.getDeclaredField("privateList");
            field.setAccessible(true);
            Object fieldValue = field.get(o1);
            List<String> privateList = (List<String>) fieldValue;
            privateList.add("added value to private");
            o1.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
