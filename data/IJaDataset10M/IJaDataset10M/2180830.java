package org.mulgara.mrg.writer;

import java.io.OutputStream;
import java.net.URI;
import org.mulgara.mrg.Graph;
import org.mulgara.mrg.parser.GraphParser;
import org.mulgara.mrg.parser.XMLGraphParser;

public class RdfXmlWriterTest extends WriterTest {

    /**
   * Create a new graph writer.
   * @param g The graph to be written.
   * @param base The base URI to write the graph to.
   * @return A new graph writer.
   */
    GraphWriter newWriter(Graph g, URI base) {
        return new XMLWriter(g, base);
    }

    /**
   * Creates a parser for the tested output type.
   * @param s A string to be parsed
   * @return A graph parser.
   */
    GraphParser newParser(String s) throws Exception {
        return new XMLGraphParser(s);
    }

    /**
   * Asks a graph to perform an export, using the tested output type.
   * @param g The graph to export.
   * @param out The output stream to write to.
   * @param base The base URI for the export.
   */
    void export(Graph g, OutputStream out, URI base) throws Exception {
        g.exportXML(out, base);
    }
}
