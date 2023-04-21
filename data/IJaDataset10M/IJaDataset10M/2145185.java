package org.gjt.sp.jedit.bsh;

import java.util.Hashtable;

/**
	The map of extended features supported by the runtime in which we live.
	<p>

	This class should be independent of all other bsh classes!
	<p>

	Note that tests for class existence here do *not* use the 
	BshClassManager, as it may require other optional class files to be 
	loaded.  
*/
public class Capabilities {

    private static boolean accessibility = false;

    public static boolean haveSwing() {
        return classExists("javax.swing.JButton");
    }

    public static boolean canGenerateInterfaces() {
        return classExists("java.lang.reflect.Proxy");
    }

    /**
		If accessibility is enabled
		determine if the accessibility mechanism exists and if we have
		the optional bsh package to use it.
		Note that even if both are true it does not necessarily mean that we 
		have runtime permission to access the fields... Java security has
	 	a say in it.
		@see org.gjt.sp.jedit.bsh.ReflectManager
	*/
    public static boolean haveAccessibility() {
        return accessibility;
    }

    public static void setAccessibility(boolean b) throws Unavailable {
        if (b == false) {
            accessibility = false;
            return;
        }
        if (!classExists("java.lang.reflect.AccessibleObject") || !classExists("org.gjt.sp.jedit.bsh.reflect.ReflectManagerImpl")) throw new Unavailable("Accessibility unavailable");
        try {
            String.class.getDeclaredMethods();
        } catch (SecurityException e) {
            throw new Unavailable("Accessibility unavailable: " + e);
        }
        accessibility = true;
    }

    private static Hashtable classes = new Hashtable();

    /**
		Use direct Class.forName() to test for the existence of a class.
		We should not use BshClassManager here because:
			a) the systems using these tests would probably not load the
			classes through it anyway.
			b) bshclassmanager is heavy and touches other class files.  
			this capabilities code must be light enough to be used by any
			system **including the remote applet**.
	*/
    public static boolean classExists(String name) {
        Object c = classes.get(name);
        if (c == null) {
            try {
                c = Class.forName(name);
            } catch (ClassNotFoundException e) {
            }
            if (c != null) classes.put(c, "unused");
        }
        return c != null;
    }

    /**
		An attempt was made to use an unavailable capability supported by
		an optional package.  The normal operation is to test before attempting
		to use these packages... so this is runtime exception.
	*/
    public static class Unavailable extends UtilEvalError {

        public Unavailable(String s) {
            super(s);
        }
    }
}
