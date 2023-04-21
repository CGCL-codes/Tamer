package avrora.sim.mcu;

import avrora.core.Program;
import avrora.sim.clock.ClockDomain;
import avrora.sim.Simulation;

/**
 * The <code>MicrocontrollerFactory</code> interface is implemented by a class that is capable of making
 * repeated copies of a particular microcontroller for use in simulation.
 *
 * @author Ben L. Titzer
 */
public interface MicrocontrollerFactory {

    /**
     * The <code>newMicrocontroller()</code> method is used to instantiate a microcontroller instance for the
     * particular program. It will construct an instance of the <code>Simulator</code> class that has all the
     * properties of this hardware device and has been initialized with the specified program.
     *
     * @param sim the simulation
     * @param p the program to load onto the microcontroller @return a <code>Microcontroller</code> instance that represents the specific hardware device with the
     *         program loaded onto it
     */
    public Microcontroller newMicrocontroller(int id, Simulation sim, ClockDomain cd, Program p);
}
