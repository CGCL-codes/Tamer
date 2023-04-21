package Code.ExtendedTypes;

import Code.Code;
import SimulatorPack.Storage;
import Project.Assembled;

/**
 *
 * @author tim
 */
;

public class mfhiCode extends Code {

    private int target;

    public mfhiCode(int pc, int lineNumber, int target) {
        super(pc, lineNumber, new int[0], false);
        this.target = target;
    }

    public void execute(Assembled assembled, Storage memory, SimulatorPack.RegisterStorage registers, SimulatorPack.fileStack raf) {
        registers.setRegister(target, registers.getHiLo(1));
    }
}
