package uk.ac.shef.dcs.dynamite;

/**
 * An evolver can take a process and cause it to evolve into
 * a new process via one or more transitions.
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 */
public interface Evolver {

    /**
     * Takes the given process and evolves it one or more
     * times.
     *
     * @param process the process to evolve.
     */
    void evolve(Process process);
}
