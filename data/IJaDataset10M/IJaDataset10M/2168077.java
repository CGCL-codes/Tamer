package org.simpleframework.servlet.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LayoutDeployer implements Deployer {

    private final Layout layout;

    public LayoutDeployer(Layout layout) {
        this.layout = layout;
    }

    public List<Deployment> getDeployments() throws Exception {
        List<Deployment> list = new ArrayList<Deployment>();
        File webPath = layout.getContext();
        File[] fileList = webPath.listFiles();
        for (File file : fileList) {
            String name = file.getName();
            if (!file.isDirectory()) {
                if (name.endsWith(".war")) {
                    String directory = name.replaceAll("\\.war", "");
                    File createDir = new File(webPath, directory);
                    if (!createDir.exists()) {
                        createDir.mkdirs();
                    } else {
                        if (new File(createDir, "WEB-INF").exists()) {
                            System.err.println("ignoring " + name + " as already extracted");
                            continue;
                        }
                    }
                    System.err.println("extracting " + name + " to " + createDir);
                    Extractor.extract(file, createDir);
                    Deployment deployment = new FileDeployment(layout, directory);
                    list.add(deployment);
                }
            } else {
                if (new File(file, "WEB-INF").exists()) {
                    Deployment deployment = new FileDeployment(layout, name);
                    list.add(deployment);
                }
            }
        }
        return list;
    }
}
