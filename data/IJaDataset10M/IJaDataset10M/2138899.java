package org.jalcedo.client.ws.gen;

import java.net.URL;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;

public interface WsClientGenerator {

    public void generate(URL url, IPackageFragment pkg) throws CoreException;
}
