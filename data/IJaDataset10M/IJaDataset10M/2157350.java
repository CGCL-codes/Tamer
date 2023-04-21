package eu.keep.gui.wizard.swa.model;

public class FileFormat {

    public final boolean newFormat;

    public final String fileformat_id, name, version, description, reference;

    public FileFormat() {
        this(false, null, null, null, null, null);
    }

    public FileFormat(boolean newFormat, String fileformat_id, String name, String version, String description, String reference) {
        this.newFormat = newFormat;
        this.fileformat_id = fileformat_id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.reference = reference;
    }

    public boolean isDummy() {
        return fileformat_id == null;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "<select>";
        }
        return name;
    }
}
