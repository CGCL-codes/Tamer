package org.simpleframework.servlet.deploy;

import java.io.File;

public class FileDeployment implements Deployment {

    private final Layout layout;

    private final String context;

    private final String prefix;

    public FileDeployment(Layout layout, String context) {
        this.prefix = "/" + context;
        this.layout = layout;
        this.context = context;
    }

    public ClassLoader getClassLoader() throws Exception {
        return layout.getClassLoader(prefix);
    }

    public File getContext() throws Exception {
        return layout.getContext(prefix);
    }

    public File getDescriptor() throws Exception {
        return layout.getDescriptor(prefix);
    }

    public String getPrefix() throws Exception {
        return prefix;
    }

    public String getName() throws Exception {
        return context;
    }
}
