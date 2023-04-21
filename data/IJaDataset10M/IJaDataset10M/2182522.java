package org.jefb.service.impl;

import java.io.File;
import org.jefb.service.IDirectoryManagerService;
import org.jefb.util.service.impl.Config;

public class DirectoryManagerService implements IDirectoryManagerService {

    private Config config;

    public void deleteFile(File file, String workspace) {
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
