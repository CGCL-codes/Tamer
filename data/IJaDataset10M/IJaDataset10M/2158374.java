package pcode;

import jaguar.Jaguar;
import jaguar.JaguarVM;
import java.io.File;

/**
 * @author peter
 *
 * @see JaguarKEY
 */
public class JaguarSHIFT extends JaguarKEY {

    /**
	 * @param vm
	 * @param src
	 * @param line
	 * @param arg 
	 */
    public JaguarSHIFT(JaguarVM vm, File src, String line, String arg) {
        super(vm, src, line, arg);
    }

    protected void execute() {
        int repcnt = 1;
        if (isNumToken(1)) repcnt = intToken(1);
        if (isNumToken(0)) keyStatement(SHIFT, intToken(0), repcnt); else keynameStatement(SHIFT, stringToken(0), repcnt);
        Jaguar.sleep(100);
    }
}
