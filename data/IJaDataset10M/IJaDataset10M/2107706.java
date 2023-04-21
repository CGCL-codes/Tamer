package Code.ExtendedTypes;

import Code.Code;
import SimulatorPack.Storage;
import Project.Assembled;

/**
 *
 * @author tim
 */
;

public class andiCode extends Code {

    private int source1;

    private int source2;

    private int target;

    public andiCode(int pc, int lineNumber, int target, int source1, int source2) {
        super(pc, lineNumber, new int[0], false);
        this.source1 = source1;
        this.source2 = source2;
        this.target = target;
    }

    public void execute(Assembled assembled, Storage memory, SimulatorPack.RegisterStorage registers, SimulatorPack.fileStack raf) {
        registers.setRegister(target, registers.getRegister(source1) & source2);
    }
}
