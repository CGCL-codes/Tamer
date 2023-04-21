package es.ulpgc.dis.heuriskein.utils;

/**
 * Base Class for all Project Members Files.
 * @author �scar Alejandro Ferrer Bernal
 */
public class XMLSerializable extends java.util.Observable {

    private String name;

    private String path;

    private boolean changed;

    public String getFilename() {
        return path + System.getProperty("file.separator") + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChanged() {
        return changed;
    }

    public XMLSerializable() {
    }

    public String toString() {
        return getName();
    }

    public boolean equals(Object a) {
        if (name.equals(a.toString())) {
            return true;
        }
        return false;
    }
}
