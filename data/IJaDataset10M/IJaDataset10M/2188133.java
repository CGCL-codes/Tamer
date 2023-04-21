package jfun.yan.web;

import java.beans.IntrospectionException;
import jfun.yan.Component;
import jfun.yan.xml.NutsUtils;
import jfun.yan.xml.nuts.ArgumentsAndPropertiesNut;

/**
 * Web integration related utilities.
 * <p>
 * @author Ben Yu
 * Jan 17, 2006 1:24:11 PM
 */
public class WebUtils {

    /**
   * The default property name for receiving ServletContext instance.
   */
    public static final String DEFAULT_SERVLET_CONTEXT_PROPERTY = "servletContext";

    /**
   * Wrap a Component to set the service object identified by
   * {@link YanLoader#SERVLET_CONTEXT_KEY} to the specified property.
   * <p>
   * Nothing happens if the property cannot be found, or if the property type doesn't match
   * or if the property is already explicitly specified in the tag
   * or if the property is implied by a wildcard.
   * </p>
   * @param c The Component object to wrap.
   * @param name the property name.
   * @param nut the Nut object.
   * @return the Component that's responsible for setting the property if any.
   */
    public static final Component setPossibleServletContext(Component c, String name, ArgumentsAndPropertiesNut nut) {
        if (name == null || name.length() == 0) return c;
        if (nut.isAllProperties() || nut.containsExplicitProperty(name)) return c;
        final Object servletcontext = nut.getNutEnvironment().findService(YanLoader.SERVLET_CONTEXT_KEY);
        if (servletcontext == null) {
            return c;
        }
        try {
            return NutsUtils.setPossiblePropertyValue(c, name, servletcontext);
        } catch (IntrospectionException e) {
            throw nut.raise(e);
        }
    }
}
