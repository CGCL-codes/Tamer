package graphlab.gui.plugins.main.saveload;

import graphlab.gui.core.graph.graph.Graph;
import graphlab.gui.plugins.main.saveload.core.GraphIOException;
import graphlab.gui.plugins.main.saveload.core.extension.GraphReaderExtension;
import graphlab.gui.plugins.main.saveload.xmlparser.GraphmlHandlerImpl;
import graphlab.gui.plugins.main.saveload.xmlparser.GraphmlParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Load implements GraphReaderExtension {

    /**
     * loads a graph from a file, note that this method clears the graph first
     * @param selectedFile
     * @param g
     */
    public static void loadGraphFromFile(File selectedFile, Graph g) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream istream = new FileInputStream(selectedFile);
        g.destroy();
        GraphmlHandlerImpl ghi = new GraphmlHandlerImpl(g);
        GraphmlParser.parse(new InputSource(istream), ghi);
        istream.close();
    }

    public boolean accepts(File file) {
        return ExampleFileFilter.getExtension(file).equals(getExtension());
    }

    public String getName() {
        return "GraphML";
    }

    public String getExtension() {
        return "xml";
    }

    public void read(File file, Graph graph) throws GraphIOException {
        try {
            loadGraphFromFile(file, graph);
        } catch (Exception e) {
            throw new GraphIOException(e.getMessage());
        }
    }

    public String getDescription() {
        return "GraphML Standard File Format for Graphs";
    }
}
