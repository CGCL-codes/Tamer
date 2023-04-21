package lebah.portal;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MerakConfig implements PortletConfig {

    protected String portletName;

    protected PortletContext portletContext;

    protected java.util.ResourceBundle resourceBundle;

    protected String initParameter;

    protected Enumeration initParameterNames;

    protected Hashtable parameters;

    protected PortletInfo portletInfo;

    public String getPortletName() {
        return portletName;
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public java.util.ResourceBundle getResourceBundle(java.util.Locale locale) {
        if (portletInfo == null) {
            System.out.println("WARNING!! portletInfo reference to NULL.");
        }
        if (resourceBundle == null) {
            StringBuffer sb = new StringBuffer();
            try {
                sb.append("javax.portlet.title");
                sb.append("=");
                sb.append(portletInfo.title);
                sb.append("\n");
                resourceBundle = new PropertyResourceBundle(new ByteArrayInputStream(sb.toString().getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resourceBundle;
    }

    public String getInitParameter(java.lang.String name) {
        return (String) parameters.get(name);
    }

    public java.util.Enumeration getInitParameterNames() {
        return initParameterNames;
    }

    public String getId() {
        return portletInfo.id;
    }
}
