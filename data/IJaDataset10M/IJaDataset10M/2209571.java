package com.jigen.ant;

public class Associate {

    private String extension, contentType, description, command;

    private Icon icon;

    public void setExtension(String extension) {
        if (this.extension != null) throw new AssertionError("Several extension tags defined");
        this.extension = extension;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addIcon(Icon icon) {
        if (this.icon != null) throw new AssertionError("Several icon tags defined.");
        this.icon = icon;
    }

    void checkConsistency() throws AssertionError {
        if (extension == null || contentType == null || command == null || description == null || icon == null) {
            throw new AssertionError("Inconsistent resource tag. Check mandatory values.");
        }
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }

    public Icon getIcon() {
        return icon;
    }
}
