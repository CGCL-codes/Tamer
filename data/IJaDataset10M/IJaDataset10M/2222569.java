package edu.it.contingency.examples.minigrid;

import edu.it.contingency.ContingencyEvent;
import edu.it.contingency.ContingencyManager;

public class CPUContingencyEvent extends ContingencyEvent {

    private static final long serialVersionUID = -4366646934998674545L;

    private int load;

    public CPUContingencyEvent(ContingencyManager source, int load) {
        super(source);
        this.load = load;
    }

    /**
     * Get the CPU load in percentage.
     * @return the CPU load in percentage 0-100.
     */
    public int getLoad() {
        return load;
    }
}
