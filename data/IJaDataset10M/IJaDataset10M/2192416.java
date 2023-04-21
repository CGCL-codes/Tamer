package system.cpu;

/**
 * Move from Lo<br>
 * <b>Syntax:</b> mflo rd<br>
 * <b>Result</b>: rd = lo<br>
 * <i>lo</i> contains the result of a division or the least-significant 32 bits
 * of the result of a multiplication
 */
@SuppressWarnings("serial")
public class MfloInstruction extends RFormat {

    @Override
    public void execute(MipsCPU cpu) {
        cpu.setRegisterValue(this.rd, cpu.getRegisterValue(MipsCPU.R_LO));
        cpu.incrementPC();
    }
}
