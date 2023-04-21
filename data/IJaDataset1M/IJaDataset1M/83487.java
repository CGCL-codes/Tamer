package org.personalsmartspace.psw3p.cls.sw;

import org.osgi.service.http.HttpService;

public class PSWServletRegistered {

    PSWClientServiceWallet serviceWallet;

    HttpService m_httpservice;

    String servletAlias;

    public PSWServletRegistered(HttpService httpservice, PSWClientServiceWallet serviceWallet) {
        this.m_httpservice = httpservice;
        this.serviceWallet = serviceWallet;
    }

    public void registerPSWServlet() {
        System.out.println("try to Register PSW Http Service");
        try {
            servletAlias = "/PSWService";
            if (m_httpservice != null) {
                m_httpservice.registerServlet(servletAlias, new PSWServiceServlet(serviceWallet), null, null);
                System.out.println(" *** Servlet Registered with alias: servletAlias *** ");
            } else {
                System.out.println(" ???? Could not get http services ???? ");
            }
        } catch (Throwable ex) {
            System.out.println(" Error while registering Http Service ");
            ex.printStackTrace();
        }
    }
}
