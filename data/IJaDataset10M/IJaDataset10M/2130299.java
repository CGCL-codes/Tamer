package cs.ubbcluj.ro.mps.states.interfaces;

import java.io.IOException;
import cs.ubbcluj.ro.mps.data.interfaces.Mps;
import cs.ubbcluj.ro.mps.reader.MpsInputStream;

/**
 * Represents a state of the finite state machine to parse mps files
 * 
 * @author avadas
 * @version 1.0
 */
public interface State {

    /**
     * Returns the next state of the finite state machine
     * 
     * @param input
     *            the inputstream to parse
     * @param mps
     *            the mps to fill
     * @return the next state of the finite state machine
     * @throws IOException
     *             see {@link IOException}
     */
    State nextState(MpsInputStream input, Mps mps) throws IOException;
}
