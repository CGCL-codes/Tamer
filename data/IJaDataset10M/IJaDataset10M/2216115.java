package net.sourceforge.plantuml;

import java.io.File;

public class GeneratedImage implements Comparable<GeneratedImage> {

    private final File pngFile;

    private final String description;

    private final PSystem system;

    public GeneratedImage(File pngFile, String description, PSystem system) {
        this.system = system;
        this.pngFile = pngFile;
        this.description = description;
    }

    public File getPngFile() {
        return pngFile;
    }

    public String getDescription() {
        return description;
    }

    public boolean isError() {
        return system instanceof PSystemError;
    }

    @Override
    public String toString() {
        return pngFile.getAbsolutePath() + " " + description;
    }

    public int compareTo(GeneratedImage this2) {
        final int cmp = this.pngFile.compareTo(this2.pngFile);
        if (cmp != 0) {
            return cmp;
        }
        return this.description.compareTo(this2.description);
    }

    @Override
    public int hashCode() {
        return pngFile.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        final GeneratedImage this2 = (GeneratedImage) obj;
        return this2.pngFile.equals(this.pngFile) && this2.description.equals(this.description);
    }
}
