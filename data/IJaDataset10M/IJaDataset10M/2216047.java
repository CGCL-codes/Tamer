package edu.mit.lcs.haystack.rdf.converters;

import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.RDFException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Dennis Quan
 */
public interface IGenerator {

    public void generate(IRDFContainer source, OutputStream os) throws RDFException, IOException;
}
