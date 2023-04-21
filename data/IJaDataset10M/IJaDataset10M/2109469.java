package org.mc4j.console.connection.install;

import org.mc4j.console.connection.ConnectionNode;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 30, 2004
 * @version $Revision: 538 $($Author: ghinkl $ / $Date: 2005-03-14 10:16:18 -0500 (Mon, 14 Mar 2005) $)
 */
public class WebsphereStudioConnectionTypeDescriptor extends WebsphereConnectionTypeDescriptor {

    public String getConnectionMessage() {
        return "You must use the IBM JVM for MC4J when connection to WebSphere Test Server 5.x. The Sun JVM " + "can only be used for WS 6.";
    }

    public String[] getConnectionClasspathEntries() {
        return new String[] { "runtimes/base_v51/lib/*", "runtimes/base_v51/java/jre/lib/ext/ibmjsse.jar", "runtimes/base_v51/java/jre/lib/ext/mail.jar", "runtimes/base_v51/deploytool/itp/plugins/org.apache.xerces_4.0.13/xercesImpl.jar", "runtimes/base_v51/deploytool/itp/plugins/org.apache.xerces_4.0.13/xmlParserAPIs.jar" };
    }

    public String getDisplayName() {
        return "WSAD Test Environment 5.x+";
    }

    public String getRecongnitionPath() {
        return "runtimes/base_v51/lib/runtime.jar";
    }
}
