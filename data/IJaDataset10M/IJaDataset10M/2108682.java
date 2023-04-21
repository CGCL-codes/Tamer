package com.manning.sdmia.ch09;

import java.util.ArrayList;
import java.util.List;
import org.springframework.osgi.test.platform.OsgiPlatform;
import junit.framework.Assert;

/**
 * @author acogoluegnes
 *
 */
public class ConfigureTomcatTest extends AbstractOsgiTest {

    public void testLaunchTomcatWithConfigurationFragment() throws Exception {
        Thread.sleep(5 * 1000);
        String pageContent = getTextResponse("http://localhost:8090/simplewebmvcapp/hello.htm");
        Assert.assertTrue(pageContent.contains("Hello from chapter 09! Today we are"));
    }

    @Override
    protected String[] getTestBundlesNames() {
        List<String> col = new ArrayList<String>();
        col.addAll(getJavaEe4WebArtifacts());
        col.add(SPRING_OSGI_GROUP + ", jasper.osgi, 5.5.23-SNAPSHOT");
        col.add(SPRING_OSGI_GROUP + ", commons-el.osgi, 1.0-SNAPSHOT");
        col.add(SPRING_OSGI_GROUP + ", jstl.osgi, 1.1.2-SNAPSHOT");
        col.addAll(getTomcat5Artifacts());
        col.add("com.manning.sdmia.ch09, tomcat-configuration-fragment, 1.0.0");
        col.addAll(getSpringDmWebArtifacts());
        col.add("org.springframework, org.springframework.web, " + getSpringVersion());
        col.add("org.springframework, org.springframework.web.servlet, " + getSpringVersion());
        col.add("org.springframework, org.springframework.context.support, " + getSpringVersion());
        col.add("com.manning.sdmia.ch09, simple-web-mvc-app, 1.0.0");
        return (String[]) col.toArray(new String[col.size()]);
    }

    @Override
    protected OsgiPlatform createPlatform() {
        OsgiPlatform osgiPlatform = super.createPlatform();
        osgiPlatform.getConfigurationProperties().setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        return osgiPlatform;
    }
}
