package net.sf.istcontract.aws.gcs.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    private String basePath;

    public ConfigurationRepositoryImpl(String basePath) {
        this.basePath = basePath;
    }

    public String getConfiguration(String configurationName) throws IOException {
        File file = new File(generateFilename(configurationName));
        FileReader fileReader = new FileReader(file);
        StringBuffer data = new StringBuffer();
        char[] buffer = new char[1024];
        int count = fileReader.read(buffer);
        while (count != -1) {
            data.append(buffer, 0, count);
            count = fileReader.read(buffer);
        }
        return data.toString();
    }

    public String getVersion(String configurationName) throws FileNotFoundException {
        File file = new File(generateFilename(configurationName));
        Long versionNumber = file.lastModified();
        if (versionNumber == 0L) {
            throw new FileNotFoundException("Configuration '" + configurationName + "' not found");
        }
        return Long.toString(versionNumber);
    }

    private String generateFilename(String configurationName) {
        return basePath + "/" + configurationName + ".xml";
    }
}
