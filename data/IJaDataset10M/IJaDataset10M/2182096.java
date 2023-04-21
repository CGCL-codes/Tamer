package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.esa.ExplicitSemanticAnalysis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;

/**
 * An executable class for running {@link ExplicitSemanticAnalysis} (ESA) from
 * the command line.  This class takes in several command line arguments.
 *
 * <ul>
 *
 * <li> {@code --overwrite=<boolean>} specifies whether to overwrite the
 *      existing output files.  The default is {@code true}.  If set to {@code
 *      false}, a unique integer is inserted into the file name.
 *
 * <li> {@code --verbose | -v} specifies whether to print runtime
 *      information to standard out
 *
 * </ul>
 *
 * <p>
 *
 * @see ExplicitSemanticAnalysis
 *
 * @author David Jurgens
 */
public class ESAMain extends GenericMain {

    private ESAMain() {
    }

    /**
     * {@inheritDoc} 
     */
    protected void addExtraOptions(ArgOptions options) {
    }

    public static void main(String[] args) {
        ESAMain esa = new ESAMain();
        try {
            esa.run(args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * {@inheritDoc} 
     */
    public SemanticSpace getSpace() {
        try {
            return new ExplicitSemanticAnalysis();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    /**
     * {@inheritDoc} 
     */
    protected SSpaceFormat getSpaceFormat() {
        return SSpaceFormat.SPARSE_BINARY;
    }

    /**
     * {@inheritDoc} 
     */
    public Properties setupProperties() {
        Properties props = System.getProperties();
        return props;
    }
}
