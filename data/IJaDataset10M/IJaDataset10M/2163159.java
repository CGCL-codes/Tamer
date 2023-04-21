package net.sourceforge.plantuml;

import java.awt.geom.AffineTransform;

public class FileFormatOption {

    private final FileFormat fileFormat;

    private final AffineTransform affineTransform;

    public FileFormatOption(FileFormat fileFormat) {
        this(fileFormat, null);
    }

    public FileFormatOption(FileFormat fileFormat, AffineTransform at) {
        this.fileFormat = fileFormat;
        this.affineTransform = at;
    }

    public final FileFormat getFileFormat() {
        return fileFormat;
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }
}
