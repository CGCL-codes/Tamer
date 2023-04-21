package net.sf.colorer;

public class Scheme {

    String name;

    FileType type;

    public Scheme(String name, FileType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FileType getFileType() {
        return type;
    }

    public String toString() {
        return name;
    }
}

;
