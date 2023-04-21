package com.waxayaz.TomcatMI.core.utils.repoManager;

import java.awt.geom.IllegalPathStateException;
import java.io.File;
import java.io.Serializable;
import com.waxayaz.TomcatMI.core.utils.TomcatMIConfig;
import com.waxayaz.TomcatMI.core.utils.ZipUtils;

/**
 * @author Waxayaz
 *
 */
public class TomcatPackage implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 336043655492573878L;

    private String zipFilePath;

    private String zipFileName;

    private String packageName;

    private String version;

    public TomcatPackage(File pkg) {
        zipFilePath = pkg.getAbsolutePath();
        zipFileName = pkg.getName();
        loadPackageDatas();
    }

    private void loadPackageDatas() {
        String tmp = new String(zipFileName);
        String preffix = "apache-tomcat-";
        String extension = ".zip";
        if (!tmp.startsWith(preffix) || !tmp.endsWith(extension)) throw new IllegalPathStateException("Invalid package name");
        String tmp2 = tmp.substring(preffix.length());
        int pos = tmp2.indexOf(".zip");
        version = tmp2.substring(0, pos);
        packageName = preffix + version;
    }

    public void installAsHome(String homePath) {
        ZipUtils.unzip(zipFilePath, homePath);
        File tomcatDir = new File(homePath + File.separator + packageName);
        if (!tomcatDir.exists()) return;
    }

    public String installAsInstance(String name) {
        String instancesPath = TomcatMIConfig.getConfiguration().getProperty("tomcatmi.instances.path");
        String path = instancesPath + File.separatorChar + name;
        File instanceDir = new File(path);
        if (!instanceDir.exists()) {
            instanceDir.mkdirs();
        }
        ZipUtils.unzip(zipFilePath, path);
        return path;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersion() {
        return version;
    }
}
