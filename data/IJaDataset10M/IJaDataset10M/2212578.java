package es.optsicom.lib.graph.matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import es.optsicom.lib.graph.Graph;

public abstract class MatrixGraphLoader {

    public MatrixGraph loadGraph(File file) throws IOException, FormatException {
        return loadGraph(new FileInputStream(file));
    }

    public Graph loadGraph(File file, int totalNodes) throws IOException, FormatException {
        FileInputStream fis = new FileInputStream(file);
        Graph g = loadGraph(fis, totalNodes);
        fis.close();
        return g;
    }

    public MatrixGraph loadGraph(InputStream is) throws IOException, FormatException {
        return loadGraph(is, -1);
    }

    public abstract MatrixGraph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException;
}
