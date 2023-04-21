package org.mc4j.console.connection.install;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 30, 2004
 * @version $Revision: 480 $($Author: ghinkl $ / $Date: 2004-10-05 01:17:41 -0400 (Tue, 05 Oct 2004) $)
 */
public class JBossConnectionTypeDescriptor extends AbstractConnectionTypeDescriptor implements ConnectionTypeDescriptor {

    public boolean isMEJBCompliant() {
        return true;
    }

    public String getDisplayName() {
        return "JBoss";
    }

    public String getRecongnitionPath() {
        return "server/all/lib/jboss.jar";
    }

    public String getDefaultServerUrl() {
        return "jnp://localhost:1099";
    }

    public String getDefaultJndiName() {
        return "jmx/rmi/RMIAdaptor";
    }

    public String getDefaultInitialContext() {
        return "org.jnp.interfaces.NamingContextFactory";
    }

    public String getDefaultPrincipal() {
        return "";
    }

    public String getDefaultCredentials() {
        return "";
    }

    public String getConnectionType() {
        return "JBoss";
    }

    public String getConnectionMessage() {
        return null;
    }

    public String[] getConnectionClasspathEntries() {
        return new String[] { "jboss-jmx.jar", "jboss-common.jar", "jboss-system.jar", "jbossall-client.jar", "log4j.jar", "jboss.jar", "concurrent.jar", "jboss-jsr77-client.jar", "jboss-transaction.jar", "dom4j.jar", "jnp-client.jar", "jmx-rmi-connector-client.jar", "jboss-j2ee.jar" };
    }

    public String getConnectionNodeClassName() {
        return "org.mc4j.console.connection.JBossConnectionNode";
    }
}
