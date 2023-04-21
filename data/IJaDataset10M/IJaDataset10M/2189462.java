package org.robotframework.jvmconnector.launch.jnlp;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class JarImpl implements Jar {

    private JarFile jarFile;

    public JarImpl(File file) {
        this(createJarFile(file));
    }

    JarImpl(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public String getPath() {
        return jarFile.getName();
    }

    public String getMainClass() {
        try {
            return jarFile.getManifest().getMainAttributes().getValue("Main-Class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JarFile createJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
