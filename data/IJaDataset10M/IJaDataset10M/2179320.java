package hr.fer.zemris.java.tecaj_3.dz2.impl.instructions;

import java.util.List;
import hr.fer.zemris.java.tecaj_3.dz2.Computer;
import hr.fer.zemris.java.tecaj_3.dz2.Instruction;
import hr.fer.zemris.java.tecaj_3.dz2.InstructionArgument;

public class InstrHalt implements Instruction {

    public InstrHalt(List<InstructionArgument> arguments) {
        if (arguments.size() != 0) throw new IllegalArgumentException("HALT: Expected no arguments");
    }

    @Override
    public boolean execute(Computer computer) {
        return true;
    }
}
