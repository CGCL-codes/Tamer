package org.vespene.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.vespene.spring.model.SpringDefinitions;
import org.vespene.spring.model.SpringServices;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

public class SpringProperties {

    private static final String SETTINGS_FILE = "/.settings/org.vespene.spring.xml";

    private IProject proj;

    public SpringProperties(IProject proj, SpringDefinitions springDefinitions) {
        super();
        this.proj = proj;
    }

    public SpringProperties(IProject proj) {
        super();
        this.proj = proj;
    }

    public void storeSpringDefinitions(SpringDefinitions springDefinitions) {
        Serializer serializer = new Persister();
        File result = new File(proj.getLocation().toString() + SETTINGS_FILE);
        try {
            serializer.write(springDefinitions, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SpringDefinitions loadSpringDefinitions() {
        SpringDefinitions springDefinitions = new SpringDefinitions();
        if ((new File(proj.getLocation().toString() + SETTINGS_FILE)).exists()) {
            Serializer serializer = new Persister();
            File source = new File(proj.getLocation().toString() + SETTINGS_FILE);
            try {
                springDefinitions = serializer.read(SpringDefinitions.class, source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return springDefinitions;
    }
}
