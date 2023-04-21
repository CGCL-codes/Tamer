package name.angoca.zemucan.extra.grammarViewer.graphviz;

import name.angoca.zemucan.AbstractZemucanException;
import name.angoca.zemucan.grammarReader.api.GraphBuilder;
import name.angoca.zemucan.tools.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MainGraphviz method for the graph viewer. This call the Graphviz
 * implementation.
 * <p>
 * <b>This code will not be longer supported because grappa/Graphviz is
 * deprecated.</b>
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * <li>1.0.1 Logger, documentation.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.0.1 2010-08-22
 */
public class MainGraphviz {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainGraphviz.class);

    /**
     * MainGraphviz method that calls the graph creation and display. You can
     * execute this application by specifying a grammar file:
     * <ul>
     * <li><code>-DGrammarFileDescriptors=grammarFile.xml</code></li>
     * <li>Optionally, you can indicate the logger file with this JVM parameter
     * <code>
     * -Dlogback.configurationFile=logback-grammarViewer.xml</code></li>
     * <li>Or the configuration file with
     * <code>-DConfigFile=zemucan_conf_viewer.xml</code></li>
     * </ul>
     *
     * @param args
     *            Arguments, nothing.
     * @throws AbstractZemucanException
     *             If there is a problem retrieving the nodes.
     */
    public static void main(final String[] args) throws AbstractZemucanException {
        LOGGER.warn("> Process started");
        System.setProperty(Constants.WITHOUT_EXTRA_NODES, "true");
        final GraphCreatorGraphviz graph = new GraphCreatorGraphviz();
        GraphBuilder.createGraph(graph);
        GraphvizDisplayer.display(graph.getGrapicalGraph());
        LOGGER.warn("< Process finished.");
    }
}
