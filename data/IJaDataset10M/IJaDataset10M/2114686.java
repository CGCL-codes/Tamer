package org.rsp.example.http.servlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rsp.http.HttpActivator;

public class Activator extends HttpActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        webXml.addContextParam(HttpActivator.BUNDLE_URI_NAMESPACE, "/servletexample");
        webXml.addServlet(MyServlet.class).addMapping("/myservlet").addInitParam("myparamname", "myparamvalue").addInitParam("myparamname2", "myparamvalue2");
        webXml.addContextParam("mycontextparamname", "mycontextparamvalue").addContextParam("mycontextparamname2", "mycontextparamvalue2");
        super.start(context);
    }
}
