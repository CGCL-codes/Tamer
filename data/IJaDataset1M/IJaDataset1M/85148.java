package org.velogen.core;

import antlr.collections.AST;
import java.io.Writer;

/**
 *
 * @author ala
 */
public interface IGenerator {

    void generate(IItem itemt, String template, String destination, String destinationNamePattern);
}
