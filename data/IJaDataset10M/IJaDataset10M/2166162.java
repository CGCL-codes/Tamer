package org.rubato.composer;

import org.rubato.composer.network.JNetwork;
import org.rubato.composer.rubette.JRubette;

/**
 * An instance of this class describes a problem occurring during running.
 * 
 * @author Gérard Milmeister
 */
public class Problem {

    /**
     * Creates a Problem with the given message.
     * @param jnetwork the JNetwork where the problem occurred
     * @param jrubette the JRubette where the problem occurred
     */
    public Problem(String msg, JNetwork jnetwork, JRubette jrubette) {
        this.msg = msg;
        this.jrubette = jrubette;
        this.jnetwork = jnetwork;
    }

    /**
     * Returns the JRubette where the problem occurred.
     */
    public JRubette getJRubette() {
        return jrubette;
    }

    /**
     * Returns the JNetwork where the problem occurred.
     */
    public JNetwork getJNetwork() {
        return jnetwork;
    }

    public String toString() {
        return jnetwork + ":" + jrubette + ": " + msg;
    }

    private String msg;

    private JRubette jrubette;

    private JNetwork jnetwork;
}
