package net.sf.orcc.ir.instructions;

import net.sf.orcc.ir.LocalVariable;
import net.sf.orcc.ir.Location;
import net.sf.orcc.ir.Port;

/**
 * This class defines a Peek instruction.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class Peek extends AbstractFifoInstruction {

    public Peek(Location location, Port port, int numTokens, LocalVariable varDef) {
        super(location, port, numTokens, varDef);
    }

    @Override
    public Object accept(InstructionInterpreter interpreter, Object... args) {
        return interpreter.interpret(this, args);
    }

    @Override
    public void accept(InstructionVisitor visitor, Object... args) {
        visitor.visit(this, args);
    }

    @Override
    public String toString() {
        return getTarget() + " = peek(" + getPort() + ", " + getNumTokens() + ")";
    }
}
