package Code;

import Assembler.Assembled;
import SimulatorPack.Storage;
import java.lang.reflect.Array;

/**
 *
 * @author tim
 */
public class RCode extends Code implements java.io.Serializable {

    public RCode(int[] code, int pc) {
        super(pc, code[Array.getLength(code) - 1], code, false);
    }

    public void execute(Assembled assembled, Storage memory, int[] register, int[] hl, SimulatorPack.fileStack raf) {
        int tempone;
        int temptwo;
        switch(code[6]) {
            case 4:
                register[code[4]] = register[code[3]] << register[code[2]];
                break;
            case 6:
                register[code[4]] = register[code[3]] >> register[code[2]];
                break;
            case 12:
                System.out.println("\n\narchaic syscall assembly\n");
                break;
            case 32:
                register[code[4]] = register[code[2]] + register[code[3]];
                break;
            case 33:
                tempone = register[code[2]];
                temptwo = register[code[3]];
                if (tempone < 0) tempone *= -1;
                if (temptwo < 0) temptwo *= -1;
                register[code[4]] = tempone + temptwo;
                break;
            case 34:
                register[code[4]] = register[code[2]] - register[code[3]];
                break;
            case 35:
                tempone = register[code[2]];
                temptwo = register[code[3]];
                if (tempone < 0) tempone *= -1;
                if (temptwo < 0) temptwo *= -1;
                register[code[4]] = tempone - temptwo;
                break;
            case 36:
                register[code[4]] = register[code[2]] & register[code[3]];
                break;
            case 37:
                register[code[4]] = register[code[2]] | register[code[3]];
                break;
            case 38:
                register[code[4]] = register[code[2]] ^ register[code[3]];
                break;
            case 43:
                if (register[code[2]] < register[code[3]]) register[code[4]] = 1; else register[code[4]] = 0;
                break;
        }
    }
}
