package org.xmi.model2text.maven;

import java.io.File;

public class Artifact {

    private File file;

    private String type = "jar";

    private String classifier;

    public void setFile(File localFile) {
        this.file = localFile;
    }

    public File getFile() {
        return this.file;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getClassifier() {
        return this.classifier;
    }
}
