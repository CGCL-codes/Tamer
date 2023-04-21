package net.sf.stump.api.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Joni Freeman
 */
public class ProjectMetadata {

    private String contextRoot = "src/webapp/";

    private List<String> cssPaths = new ArrayList<String>();

    public void clearCssPaths() {
        cssPaths.clear();
    }

    public void addCssPath(String css) {
        cssPaths.add(css);
    }

    public List<String> getCssPaths() {
        return cssPaths;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public ProjectMetadata load() throws IOException {
        return load(System.getProperty("user.dir"));
    }

    public ProjectMetadata load(String baseDir) throws IOException {
        if (baseDir == null) {
            return this;
        }
        File file = new File(baseDir, ".wicketprops");
        if (!file.exists()) {
            return this;
        }
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(file);
        props.load(in);
        contextRoot = props.getProperty("web.contextRoot", contextRoot);
        setCssPaths(props.getProperty("web.cssPaths", ""));
        in.close();
        return this;
    }

    public void setCssPaths(String pathString) {
        cssPaths.clear();
        String[] paths = pathString.split(",");
        for (String path : paths) {
            cssPaths.add(path.trim());
        }
    }
}
