package org.mobicents.servlet.sip.ctf.core.environment.msstomcat7;

import org.jboss.weld.environment.AbstractContainer;
import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.jboss.weld.environment.tomcat7.Tomcat7Container;
import org.mobicents.servlet.sip.startup.ConvergedApplicationContextFacade;

/**
 * @author gvagenas 
 * gvagenas@gmail.com
 * 
 */
public class MSSTomcat7Container extends AbstractContainer {

    public static Container INSTANCE = new MSSTomcat7Container();

    protected String classToCheck() {
        return "org.apache.tomcat.InstanceManager";
    }

    public void initialize(ContainerContext context) {
        try {
            SipWeldForwardingInstanceManager.replacInstanceManager(context.getEvent(), context.getManager());
            log.info("MSS 2.x on Tomcat 7 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
        } catch (Exception e) {
            log.error("Unable to replace MSS 2.x - Tomcat 7 InstanceManager. CDI injection will not be available in Servlets, Filters, or Listeners", e);
        }
    }

    @Override
    public boolean touch(ContainerContext context) throws Exception {
        boolean result = super.touch(context);
        if (!(context.getContext() instanceof ConvergedApplicationContextFacade)) {
            result = false;
        }
        return result;
    }
}
