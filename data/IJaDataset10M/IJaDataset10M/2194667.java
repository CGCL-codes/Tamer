package org.jagent.service.boot;

import java.net.URL;
import java.io.File;

public interface DeploymentFactory {

    ServiceDeployment newInstance(URL location);

    ServiceDeployment newInstance(File location);
}
