package system.cpu;

import system.ByteTool;

/**
 * <b>Multiplication</b> (2 parameter instruction): R Format Instruction<br>
 * <b>Syntax:</b> mult rs, rt<br>
 * <b>Result</b>: hilo = (signed) s * (signed) t<br>
 * where <i>hilo</i> is the concatenation of <i>hi</i> and <i>lo</i>. Each of
 * <i>hi</i> and <i>lo</i> holds the same number of bits as a machine
 * register, so <i>hilo</i> can hold a 64-bit integer. <br>
 * Multiply results appear in two separate register <i>hi</i> and <i>lo</i>.
 * <i>hi</i> contains the most-significant bits and <i>lo</i> the least ones.
 * Their values can only be accessed with the two special instructions <i><b>mfhi</b></i>
 * and <i><b>mflo</b></i>
 */
@SuppressWarnings("serial")
public class MultInstruction extends RFormat {

    @Override
    public void execute(MipsCPU cpu) {
        long result;
        byte[] reg1 = cpu.getRegisterValue(this.rs);
        byte[] reg2 = cpu.getRegisterValue(this.rt);
        result = (long) ByteTool.byteArrayToInt(reg1) * (long) ByteTool.byteArrayToInt(reg2);
        byte[] resultArray = ByteTool.longToByteArray(result);
        byte[] hi = new byte[4];
        byte[] lo = new byte[4];
        for (int i = 0; i < 4; i++) {
            lo[i] = resultArray[i];
            hi[i] = resultArray[i + 4];
        }
        cpu.setRegisterValue(MipsCPU.R_HI, hi);
        cpu.setRegisterValue(MipsCPU.R_LO, lo);
        cpu.incrementPC();
    }
}
