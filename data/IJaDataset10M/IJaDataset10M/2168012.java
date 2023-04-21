package edu.rice.cs.cunit.instrumentors;

import edu.rice.cs.cunit.classFile.ClassFile;

/**
 * Interface to instrument classes during loading.
 *
 * @author Mathias Ricken
 */
public interface IInstrumentationStrategy {

    /**
     * Instrument the class.
     *
     * @param cf class file info
     */
    public void instrument(ClassFile cf);

    /**
     * Instrumentation of all classes is done.
     */
    public void done();
}
