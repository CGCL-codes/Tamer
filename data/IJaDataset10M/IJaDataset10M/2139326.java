package net.sf.lavalamp.properties;

import java.io.File;
import java.io.FileNotFoundException;
import org.ho.yaml.Yaml;

public class ApplicationPropertiesLoader {

    public ApplicationProperties load(String fileName) throws FileNotFoundException {
        File input = new File(fileName);
        ApplicationProperties applicationProperties = Yaml.loadType(input, ApplicationProperties.class);
        applicationProperties.check();
        return applicationProperties;
    }
}
