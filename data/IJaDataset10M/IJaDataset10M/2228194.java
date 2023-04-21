package org.echarts;

/**
   Port used to exchange messages between a machine and it ancestors
   or its descendants so that it is possible for messages to fire
   machine message transitions. The port has one queue to be used for
   receiving messages from other machines or for sending messages to
   other machines.
*/
public final class InternalPort extends OutputPort {

    public static final long serialVersionUID = 1;

    /**
	   Identifier for the port used for monitoring and debugging.
	*/
    private final String name;

    /**
	   Machine that created this port. 
	 */
    final TransitionMachine machine;

    /**
	   This port's monitor properties. Accessed via
	   getMonitorProperties().
	 */
    private InternalPortProperties monitorProperties = null;

    /**
	   @param machine - machine in which port declared ("owning" machine)
	   @param name - symbolic name used in debug output
	 */
    public InternalPort(final TransitionMachine machine, final String name) {
        this.name = (name == null || name.equals("")) ? "InternalPort" : name;
        this.machine = machine;
    }

    /**
	   Puts message in port's input queue. Port argument of input
	   PortMessage indicates destination port (this port). Ensures
	   that specified otherMachine is an ancestor/descendant of the
	   port's creating machine, otherwise throws an exception.
	*/
    public final void output(final Object message, final Machine otherMachine) throws Exception {
        if (machine.code.isSubmachineOf(otherMachine.code) || otherMachine.code.isSubmachineOf(machine.code)) inputQueue.put(new PortMessage(this, message)); else throw new PortException("Attempt to output message to " + name + " from non-ancestor/non-descendant machine");
    }

    /**
	   Clears contents of port's queue from specified machine.
	 */
    public final void clear(final Machine otherMachine) throws Exception {
        if (machine.code.isSubmachineOf(otherMachine.code) || otherMachine.code.isSubmachineOf(machine.code)) inputQueue.clear(); else throw new PortException("Attempt to clear " + name + " from non-ancestor/non-descendant machine");
    }

    /**
	   Flush contents of this port's queue to specified otherPort's
	   queue from specified machine.
	 */
    public final void flush(final OutputPort otherPort, final Machine otherMachine) throws Exception {
        Object obj = null;
        while ((obj = inputQueue.getNoBlock()) != null) otherPort.output(obj, otherMachine);
    }

    /**
	   Gets this port's monitor properties.
	 */
    public final PortProperties getMonitorProperties() {
        if (monitorProperties == null) monitorProperties = new InternalPortProperties(this);
        return monitorProperties;
    }

    public final String toString() {
        return name;
    }
}
