package org.jalcedo.client.ws.gen.impl;

import java.net.URL;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.jalcedo.client.ws.WsClientActivator;
import org.jalcedo.client.ws.WsClientGeneratorConfig;
import org.jalcedo.client.ws.gen.WsClientGenerator;

public class WsClientGeneratorImpl implements WsClientGenerator {

    public void generate(URL url, IPackageFragment pkg) throws CoreException {
        this.runWsimport(url, pkg);
    }

    private void runWsimport(URL url, IPackageFragment pkg) throws CoreException {
        String gfPath = this.getGlassFishPath();
        IPackageFragmentRoot sourceDir = (IPackageFragmentRoot) pkg.getParent();
        String wsCmd = gfPath + " -s " + sourceDir.getElementName() + " -p " + pkg.getElementName();
        System.out.println("command:" + wsCmd);
        Runtime rt = Runtime.getRuntime();
        try {
            wsCmd = "";
            Process pr = rt.exec(wsCmd);
        } catch (Exception e) {
            System.out.println("[WsClientGeneratorImpl] exec error.");
            throw new CoreException(new Status(IStatus.ERROR, WsClientActivator.PLUGIN_ID, IStatus.OK, "[WsClientGeneratorImpl] exec error.", e));
        }
    }

    private String getGlassFishPath() {
        WsClientGeneratorConfig config = WsClientActivator.getConfig();
        return config.getGlassFishPath();
    }
}
