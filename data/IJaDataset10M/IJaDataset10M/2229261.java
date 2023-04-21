package com.liferay.portal.tools;

import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.util.ServerDetector;
import java.io.File;

/**
 * <a href="HookDeployer.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Bruno Farache
 *
 */
public class HookDeployer extends BaseDeployer {

    protected void copyXmls(File srcFile, String displayName, PluginPackage pluginPackage) throws Exception {
        super.copyXmls(srcFile, displayName, pluginPackage);
        if (appServerType.equals(ServerDetector.TOMCAT_ID)) {
            copyDependencyXml("context.xml", srcFile + "/META-INF");
        }
    }
}
