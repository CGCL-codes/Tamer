package de.grogra.ext.sunshine.kernel.bidirPT;

/**
 * @author Thomas
 *
 */
public interface Interruptable {

    public void setLoopParameter(int count, int steps);

    public boolean resume();

    public void reset();
}
