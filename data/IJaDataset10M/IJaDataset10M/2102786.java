package net.sf.orcc.ir.instructions;

import net.sf.orcc.ir.Location;
import net.sf.orcc.ir.Port;

/**
 * This class defines a ReadEnd instruction. This instruction is used in code
 * generation to signal that an action has finished reading from a FIFO.
 * 
 * @author J�r�me GORIN
 * 
 */
public class ReadEnd extends AbstractInstruction {

    private Port port;

    public ReadEnd(Location location, Port port) {
        super(location);
        setPort(port);
    }

    public ReadEnd(Read node) {
        super(node.getLocation());
        setPort(node.getPort());
    }

    @Override
    public Object accept(InstructionInterpreter interpreter, Object... args) {
        return interpreter.interpret(this, args);
    }

    @Override
    public void accept(InstructionVisitor visitor, Object... args) {
        visitor.visit(this, args);
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "readEnd(" + getPort() + ")";
    }
}
