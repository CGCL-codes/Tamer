package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface PSystem {

    List<File> exportDiagrams(File suggestedFile, FileFormatOption fileFormatOption) throws IOException, InterruptedException;

    void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormatOption) throws IOException;

    int getNbImages();

    String getDescription();

    String getMetadata();

    String getWarningOrError();

    UmlSource getSource();
}
