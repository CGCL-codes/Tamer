package com.MainTesting;

import java.util.Calendar;
import com.openospc.processform.template.GroupGeneratorTemplate;
import com.openospc.util.DateUtil;
import com.openospc.velocity.HTMLVelocity;
import com.openospc.velocity.TestingVelocity;

public class Testing {

    /**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println(System.nanoTime());
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();
        Class klazz = cl.loadClass("com.openospc.processform.template.Data_Summary_Grp");
        Object class1 = klazz.newInstance();
        if (class1 instanceof GroupGeneratorTemplate) {
            ((GroupGeneratorTemplate) class1).generateTemplate();
        }
    }
}
