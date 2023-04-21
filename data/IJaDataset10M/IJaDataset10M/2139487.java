package fr.soleil.webserver.servlet;

import java.lang.reflect.Method;
import fr.soleil.util.parameter.ParameterManager;

public class ActionServletInit {

    /**
	 * Execute the init, use reflect api to call the application init method
	 * @throws Exception
	 */
    public static void init() throws Exception {
        String initClassName = ParameterManager.getStringParameter("", "INIT_CLASS");
        System.out.println("Init Class specified : " + initClassName);
        if (initClassName == null || "".equalsIgnoreCase(initClassName)) return;
        Class clazz = Class.forName(initClassName);
        Method method = clazz.getMethod("init", new Class[] {});
        method.invoke(null, new Object[] {});
        System.out.println(initClassName + " called");
    }
}
