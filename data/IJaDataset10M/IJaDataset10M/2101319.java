package org.mortbay.jetty.plus.jmx;

import javax.management.MBeanException;
import org.mortbay.jetty.plus.PlusWebAppContext;
import org.mortbay.jetty.servlet.jmx.WebApplicationContextMBean;

/**
 * @author janb
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PlusWebAppContextMBean extends WebApplicationContextMBean {

    private PlusWebAppContext _plusWebAppContext;

    /** Constructor. 
     * @exception MBeanException 
     */
    public PlusWebAppContextMBean() throws MBeanException {
    }

    protected void defineManagedResource() {
        super.defineManagedResource();
        defineAttribute("ENC");
        defineOperation("addEnvEntry", new String[] { STRING, OBJECT }, IMPACT_ACTION);
        defineOperation("addEnvEntry", new String[] { STRING, STRING }, IMPACT_ACTION, ON_MBEAN);
        _plusWebAppContext = (PlusWebAppContext) getManagedResource();
    }

    /** Method only on JMX bean to deal with String values
     * @param name
     * @param value
     */
    public void addEnvEntry(String name, String value) {
        _plusWebAppContext.addEnvEntry(name, value);
    }

    /** 
     * @param ok 
     */
    public void postRegister(Boolean ok) {
        super.postRegister(ok);
    }

    public void postDeregister() {
        super.postDeregister();
    }
}
