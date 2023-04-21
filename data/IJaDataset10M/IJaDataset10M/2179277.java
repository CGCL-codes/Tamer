package tcl.lang;

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;

class AdaptorClassLoader extends ClassLoader {

    private static int genCount = 0;

    private AdaptorGen adaptorGen = new AdaptorGen();

    private synchronized String getNewClassName() {
        return "tcl.lang.Adaptor" + (genCount++);
    }

    synchronized Class loadEventAdaptor(Interp interp, EventSetDescriptor desc) {
        String classToGen = getNewClassName();
        byte classData[] = adaptorGen.generate(desc, EventAdaptor.class, classToGen);
        Class cls = defineClass(classToGen, classData, 0, classData.length);
        resolveClass(cls);
        return cls;
    }

    protected synchronized Class loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class cls = Class.forName(className);
        if (resolve) {
            resolveClass(cls);
        }
        return cls;
    }
}
