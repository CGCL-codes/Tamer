package org.nixinet.netgrapher.io;

import org.nixinet.netgrapher.ui.FileType;
import prefuse.data.Graph;

public class FileLoader {

    private Parser parser;

    private FileType fileType;

    private String fileName;

    public FileLoader(String fileName, String fileType) {
        this.fileType = FileType.valueOf(fileType);
        this.fileName = fileName;
    }

    public Graph load() {
        switch(fileType) {
            case graphml:
                return loadGraphml(fileName);
            case ndl:
                return loadNdl(fileName);
            case tcl:
                return loadTcl(fileName);
            case dot:
                return loadDot(fileName);
            default:
                return null;
        }
    }

    private Graph loadTcl(String fileName) {
        parser = new TclParser();
        Graph g = parser.loadFile(fileName);
        return g;
    }

    private Graph loadNdl(String fileName) {
        parser = new NdlParser();
        Graph g = parser.loadFile(fileName);
        return g;
    }

    private Graph loadGraphml(String fileName) {
        parser = new GraphmlParser();
        Graph g = parser.loadFile(fileName);
        return g;
    }

    private Graph loadDot(String fileName) {
        parser = new DotParser();
        Graph g = parser.loadFile(fileName);
        return g;
    }
}
