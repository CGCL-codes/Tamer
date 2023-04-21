package com.manning.sdmia.web.wicket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.osgi.framework.Bundle;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * @author Thierry Templier
 */
public class WicketTest extends AbstractConfigurableBundleCreatorTests {

    public void testWebapp() throws Exception {
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            System.out.println("- " + bundle.getSymbolicName() + " - " + bundle.getState() + " (" + Bundle.ACTIVE + ")");
        }
        Thread.sleep(2000);
        assertTrue(getTextResponse("http://localhost:8080/wicket/").contains("Cogoluegnes"));
    }

    @Override
    protected String[] getTestFrameworkBundlesNames() {
        return new String[] { "org.aopalliance, com.springsource.org.aopalliance, 1.0.0", "org.apache.log4j, com.springsource.org.apache.log4j, 1.2.15", "org.junit, com.springsource.junit, 3.8.2", "org.objectweb.asm, com.springsource.org.objectweb.asm, 2.2.3", "org.slf4j, com.springsource.slf4j.api, 1.5.6", "org.slf4j, com.springsource.slf4j.log4j, 1.5.6", "org.slf4j, com.springsource.slf4j.org.apache.commons.logging, 1.5.6", "org.springframework, org.springframework.aop, 2.5.6", "org.springframework, org.springframework.beans, 2.5.6", "org.springframework, org.springframework.context, 2.5.6", "org.springframework, org.springframework.core, 2.5.6", "org.springframework, org.springframework.test, 2.5.6", "org.springframework.osgi, spring-osgi-annotation, 1.2.0", "org.springframework.osgi, spring-osgi-core, 1.2.0", "org.springframework.osgi, spring-osgi-extender, 1.2.0", "org.springframework.osgi, spring-osgi-io, 1.2.0", "org.springframework.osgi, spring-osgi-test, 1.2.0" };
    }

    @Override
    protected String[] getTestBundlesNames() {
        return new String[] { "org.springframework.osgi, spring-osgi-web, 1.2.0", "org.springframework.osgi, spring-osgi-web-extender, 1.2.0", "org.springframework, org.springframework.context.support, 2.5.6", "org.springframework, org.springframework.web, 2.5.6", "org.springframework, org.springframework.web.servlet, 2.5.6", "net.sourceforge.cglib, com.springsource.net.sf.cglib, 2.1.3", "org.apache.log4j, com.springsource.org.apache.log4j, 1.2.15", "org.springframework.osgi, catalina.osgi, 5.5.23-SNAPSHOT", "org.springframework.osgi, catalina.start.osgi, 1.0-SNAPSHOT", "javax.el, com.springsource.javax.el, 1.0.0", "javax.servlet, com.springsource.javax.servlet, 2.5.0", "javax.servlet, com.springsource.javax.servlet.jsp, 2.1.0", "javax.servlet, com.springsource.javax.servlet.jsp.jstl, 1.1.2", "org.apache.commons, com.springsource.org.apache.commons.el, 1.0.0", "org.apache.taglibs, com.springsource.org.apache.taglibs.standard, 1.1.2", "org.aspectj, com.springsource.org.aspectj.runtime, 1.6.1", "org.aspectj, com.springsource.org.aspectj.weaver, 1.6.1", "org.springframework.osgi, jasper.osgi, 5.5.23-SNAPSHOT", "org.apache.wicket, com.springsource.org.apache.wicket, 1.3.3", "org.apache.wicket, com.springsource.org.apache.wicket.extensions, 1.3.3", "org.apache.wicket, com.springsource.org.apache.wicket.injection, 1.3.3", "org.apache.wicket, com.springsource.org.apache.wicket.spring, 1.3.3", "org.apache.wicket, com.springsource.org.apache.wicket.spring.injection.annot, 1.3.3", "com.manning.sdmia.ch08, ch08-directory, 1.0-SNAPSHOT", "com.manning.sdmia.ch08, ch08-wicket, 1.0-SNAPSHOT" };
    }

    private String getTextResponse(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        BufferedReader in = null;
        try {
            con.connect();
            assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            return builder.toString();
        } finally {
            if (in != null) {
                in.close();
            }
            con.disconnect();
        }
    }
}
