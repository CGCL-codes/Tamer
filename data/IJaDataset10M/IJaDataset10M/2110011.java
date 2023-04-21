package com.oozinoz.planning;

import java.util.Date;
import com.oozinoz.machine.Machine;

/**
*  A planner for estimating when a shell assembler will
*  become available.
*/
public class ShellPlanner extends MachinePlanner {

    public ShellPlanner(Machine m) {
        super(m);
    }

    /**
    *  Say when this planner's machine will be available; this
    *  method is not yet actually implemented.
    */
    public Date getAvailable() {
        return new Date();
    }
}
