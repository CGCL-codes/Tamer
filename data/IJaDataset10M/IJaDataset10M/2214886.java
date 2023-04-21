package org.frameworkset.spi.remote.webservice;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.jaxws.spi.ProviderImpl;
import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;

/**
 * <p>Title: WSLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-23 ����05:04:15
 * @author biaoping.yin
 * @version 1.0
 */
public class WSLoader {

    private static final Logger logger = Logger.getLogger(WSLoader.class);

    public void loadAllWebService(ClassLoader classLoader) {
        try {
            loadDefaultWebService(classLoader);
        } catch (Exception e) {
            logger.error(e);
        }
        try {
            loadModulesWebService(classLoader);
        } catch (Exception e) {
            logger.error(e);
        }
        try {
            loadMvcWebService(classLoader);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void loadDefaultWebService(ClassLoader classLoader) {
        ProList webservices = WSUtil.webservices;
        if (webservices != null) {
            for (int i = 0; i < webservices.size(); i++) {
                try {
                    Pro pro = webservices.getPro(i);
                    String servicePort_ = pro.getStringExtendAttribute("servicePort");
                    if (servicePort_ == null || servicePort_.equals("")) {
                        logger.warn("Webservice [" + pro.getName() + "] in " + pro.getConfigFile() + " has not defined servicePort,please check this definition,ignored this regist action.. ");
                        continue;
                    }
                    String servicePort = servicePort_.startsWith("ws:") ? servicePort_ : "ws:" + servicePort_;
                    if (WebServicePublisherUtil.hasPublished(servicePort)) {
                        logger.warn("Webservice [" + pro.getName() + "] in " + pro.getConfigFile() + " has been registed in " + WebServicePublisherUtil.getPublishedWSInfo(servicePort).getConfigFile() + ",ignored this regist action.");
                        return;
                    }
                    Object webservice = pro.getBeanObject();
                    String mtom = pro.getStringExtendAttribute("mtom");
                    ProviderImpl providerimpl = new ProviderImpl();
                    Endpoint ep = providerimpl.createAndPublishEndpoint(WebServicePublisherUtil.convertServicePort(servicePort_, classLoader), webservice);
                    SOAPBinding binding = (SOAPBinding) ep.getBinding();
                    if (mtom != null && mtom.equalsIgnoreCase("true")) binding.setMTOMEnabled(true);
                    WebServicePublisherUtil.tracePublishedWSInfo(servicePort, pro);
                } catch (Exception e) {
                    logger.warn(e);
                }
            }
        }
    }

    /**
	 * ��Ҫȷ��mvc��������webservice��������֮ǰ�����������ȡ�����κ���mvc��������õ�webservice����
	 */
    private void loadMvcWebService(ClassLoader classLoader) {
        try {
            org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
            WebServicePublisherUtil.loaderContextWebServices((BaseApplicationContext) context.getBeanObject("webapplicationcontext"), classLoader);
        } catch (Exception e) {
            logger.warn(e);
        }
    }

    private void loadModulesWebService(ClassLoader classLoader) {
        org.frameworkset.spi.BaseApplicationContext context = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/ws/webserivce-modules.xml");
        String[] cxf_webservices_modules = context.getStringArray("cxf.webservices.modules");
        if (cxf_webservices_modules == null || cxf_webservices_modules.length == 0) return;
        org.frameworkset.spi.BaseApplicationContext context_ = null;
        for (String t : cxf_webservices_modules) {
            context_ = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext(t);
        }
        for (String t : cxf_webservices_modules) {
            context_ = org.frameworkset.spi.DefaultApplicationContext.getApplicationContext(t);
            WebServicePublisherUtil.loaderContextWebServices(context_, classLoader);
        }
    }
}
