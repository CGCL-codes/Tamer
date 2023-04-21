package de.dgrid.bisgrid.secure.proxy.deployment;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;
import de.dgrid.bisgrid.secure.proxy.service.factory.ISecureWebServiceProxyFactory;
import de.dgrid.bisgrid.secure.proxy.service.factory.SecureWebServiceProxyFactoryHome;
import de.fzj.unicore.wsrflite.utils.deployment.DeploymentManager;

public class RunDeploySecureProxy {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        DeploySecureProxyService secproxy = new DeploySecureProxyService();
        new Thread(secproxy).run();
    }
}
