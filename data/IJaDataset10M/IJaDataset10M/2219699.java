package org.allcolor.ywt.filter.authenticator;

import org.apache.catalina.Realm;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Quentin Anciaux
 * @version 0.1.0
 */
public interface IAuthenticator {

    /**
	 * DOCUMENT ME!
	 *
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 */
    public void handleLogin(final HttpServletRequest request, final HttpServletResponse response);

    /**
	 * DOCUMENT ME!
	 *
	 * @param request DOCUMENT ME!
	 */
    public void login(final HttpServletRequest request);

    /**
	 * 
	DOCUMENT ME!
	 *
	 * @author Quentin Anciaux
	 * @version 0.1.0
	 */
    public static class CHelper {

        /**
		 * DOCUMENT ME!
		 *
		 * @param name DOCUMENT ME!
		 * @param value DOCUMENT ME!
		 * @param realm DOCUMENT ME!
		 */
        @SuppressWarnings(value = { "unchecked" })
        public static final void processParam(final String name, final String value, final Realm realm) {
            try {
                final Method methods[] = realm.getClass().getMethods();
                for (final Method element : methods) {
                    final String mname = element.getName();
                    if (mname.toLowerCase().equals("set" + name.toLowerCase())) {
                        final Class clazzes[] = element.getParameterTypes();
                        if (clazzes.length == 1) {
                            if (clazzes[0].isAssignableFrom(String.class)) {
                                element.invoke(realm, new Object[] { value });
                            } else if (clazzes[0].isAssignableFrom(boolean.class)) {
                                element.invoke(realm, new Object[] { Boolean.valueOf(value) });
                            } else if (clazzes[0].isAssignableFrom(int.class)) {
                                element.invoke(realm, new Object[] { new Integer(value) });
                            } else if (clazzes[0].isAssignableFrom(long.class)) {
                                element.invoke(realm, new Object[] { new Long(value) });
                            } else if (clazzes[0].isAssignableFrom(float.class)) {
                                element.invoke(realm, new Object[] { new Float(value) });
                            } else if (clazzes[0].isAssignableFrom(double.class)) {
                                element.invoke(realm, new Object[] { new Double(value) });
                            }
                            break;
                        }
                    } else if (mname.toLowerCase().equals(name.toLowerCase())) {
                        final Class clazzes[] = element.getParameterTypes();
                        if (clazzes.length == 1) {
                            if (clazzes[0].isAssignableFrom(String.class)) {
                                element.invoke(realm, new Object[] { value });
                            } else if (clazzes[0].isAssignableFrom(boolean.class)) {
                                element.invoke(realm, new Object[] { Boolean.valueOf(value) });
                            } else if (clazzes[0].isAssignableFrom(int.class)) {
                                element.invoke(realm, new Object[] { new Integer(value) });
                            } else if (clazzes[0].isAssignableFrom(long.class)) {
                                element.invoke(realm, new Object[] { new Long(value) });
                            } else if (clazzes[0].isAssignableFrom(float.class)) {
                                element.invoke(realm, new Object[] { new Float(value) });
                            } else if (clazzes[0].isAssignableFrom(double.class)) {
                                element.invoke(realm, new Object[] { new Double(value) });
                            }
                            break;
                        }
                    }
                }
            } catch (final Exception e) {
                System.err.println("Problem setting attribute " + name + " on " + ((realm != null) ? realm.getInfo() : "realm not set !"));
            }
        }
    }
}
