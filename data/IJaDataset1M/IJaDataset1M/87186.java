package org.owasp.jxt.container;

import junit.framework.Test;

/**
 * Jetty7xTest
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
public class Jetty7xTest extends ContainerSuite {

    public static Test suite() throws Exception {
        return suite(Jetty7xTest.class, "jetty7x", "http://dist.codehaus.org/jetty/jetty-hightide-7.1.6/jetty-hightide-7.1.6.v20100715.zip");
    }
}
