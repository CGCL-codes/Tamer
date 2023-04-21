package org.megatome.knowndefect.ant.scan;

/**
 * Simple interface for filtering
 * <p>Code in this class borrowed and adapted from the Scannotation library
 * available from <a href="http://scannotation.sourceforge.net/">http://scannotation.sourceforge.net/</a></p>
 */
public interface Filter {

    /**
     * Determine if the input value is accepted by the filter.
     * @param paramString Value to check.
     * @return True if accepted, false otherwise.
     */
    public boolean accepts(String paramString);
}
