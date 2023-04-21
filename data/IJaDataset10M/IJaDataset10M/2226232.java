package org.jcompany.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.jcompany.maven.appserver.IAppServer;

public class PlcBehaviourDeployFactory {

    public PlcBehaviourDeployFactory() {
    }

    public static IBehaviourDeploy getIComportamentoDeDeployInstance(IAppServer server) throws MojoExecutionException {
        if (IBehaviourDeploy.Servers.TOMCAT.equals(server.getServerName())) {
            return new PlcBehaviourDeployTomcat();
        } else if (IBehaviourDeploy.Servers.GLASSFISH.equals(server.getServerName())) {
            return new PlcBehaviourDeployGlassfish();
        }
        throw new MojoExecutionException("N�o sabe fazer deploy para: " + server.getServerName());
    }
}
