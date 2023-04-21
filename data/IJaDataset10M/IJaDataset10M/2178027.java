package com.loribel.commons.abstraction;

import java.io.Writer;

/**
 * Abstraction to represent a writer for {@link GB_SimpleNode}.
 *
 * @author Gregory Borelli
 */
public interface GB_SimpleNodeWriter {

    public void writeTree(Writer a_writer, GB_SimpleNode a_node) throws Exception;
}
