package org.localdb;

import java.io.File;

/**
 *
 * @author Owner
 */
public class locate {

    private String configUrl;

    public locate(String configUrl) {
        this.configUrl = configUrl;
    }

    public String readConfig(String configUrl) {
        String confResult = null;
        confResult = new XMLParser().parse(configUrl);
        return confResult;
    }

    public String getUrl(String folderUrlPath) {
        String result = null;
        File file = new File(folderUrlPath);
        if (file.exists()) {
            result = file.getAbsolutePath();
        }
        return result;
    }

    public String start() {
        String confUrl = this.readConfig(configUrl);
        String result = this.getUrl(confUrl);
        return result;
    }
}
