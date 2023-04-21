package thinwire.apps.formcreator;

import java.util.*;

/**
 * @author Joshua J. Gertzen
 */
public class ProjectDefinition {

    private String name;

    private String fileName = "";

    private List<XODFile> files = new ArrayList<XODFile>();

    public ProjectDefinition() {
        this(null);
    }

    public ProjectDefinition(String name) {
        setName(name);
    }

    public void initFileName(String fileName) {
        if (!this.fileName.equals("")) throw new IllegalStateException("this.fileName{" + this.fileName + "} != null");
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) name = "";
        this.name = name;
    }

    public List<XODFile> getFiles() {
        return files;
    }
}
