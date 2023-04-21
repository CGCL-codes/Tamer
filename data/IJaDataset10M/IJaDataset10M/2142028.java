package org.nodal.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import org.nodal.Repository;
import org.nodal.model.Node;
import org.nodal.nav.Path;
import org.nodal.util.Name;

/**
 * A Document in a Repository.
 */
public interface Document {

    /**
   * The Repository that contains this Document.
   */
    Repository repository();

    /**
   * A Path to this Document.
   */
    Path path();

    /**
   * The schema for this document type.
   */
    DocumentFormat format();

    /**
   * The root Node of this Document's graph.
   */
    Node root();

    /**
   * The Document node for this document.
   * @return the Document Node for this Document
   */
    Node docNode();

    /**
   * Return the Node with the given Name in this Document.
   * @param name the Name of the Node
   * @return the Node so named or null if none
   */
    Node nodeNamed(Name name);

    /**
   * Return the Node with the given Name in this Document.
   * @param name a String representing the Node
   * @return the Node so named or null if none
   */
    Node nodeNamed(String name);

    /**
   * Treat this Document as a Directory.  If it <i>is</i> a Directory, then
   * return the {@link Directory} interface, otherwise return null.  Can be
   * used both as a cast and test.
   * @return a Directory interface if this Document is one, otherwise null.
   */
    Directory asDirectory();

    /**
   * Write the encoding of this Document on the given OutputStream.
   * @param s the stream to write onto
   */
    void write(OutputStream s) throws IOException;
}
