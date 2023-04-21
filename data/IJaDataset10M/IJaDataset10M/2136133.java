package net.sourceforge.processdash.i18n;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class SafeTemplateClassLoader extends ClassLoader {

    protected Class findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    protected URL findResource(String name) {
        final String name1 = name.replace('$', '.');
        try {
            return (URL) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    return findResourceImpl(name1);
                }
            });
        } catch (PrivilegedActionException e) {
            if (e.getException() instanceof RuntimeException) throw (RuntimeException) e.getException(); else throw new RuntimeException(e);
        }
    }

    protected URL findResourceImpl(String mappedName) {
        if (!mappedName.startsWith("/")) mappedName = "/" + mappedName;
        mappedName = "Templates" + mappedName;
        return Resources.class.getClassLoader().getResource(mappedName);
    }
}
