package pcode;

import jaguar.Jaguar;
import jaguar.JaguarRectangle;
import jaguar.JaguarVM;
import java.io.File;

/**
 * @author peter
 * 
 * <p>
 * <b>railroad:</b>
 * <pre>
 * ---- LOOKONCE --- rectangle --- bounding rectangle----------------------------------------------------|
 *                                                     |                   ^  |                     ^
 *                                                     +-- SKIP -- count --+  +-- BW -- threshold --+
 *                                                                            |                     ^
 *                                                                            +-- BG -- palette   --+
 * </pre>
 * When the location of the rectangle is at (0,0) a LOOKUP rectangle bounding rectangle
 * is performed.
 * When the rectangle has been looked up before then a LOOKUP rectangle copy_of_rectangle
 * is performed.
 * Scans the bounding rectangle for the image stored in the rectangle,
 * the status register is set to OK (zero) when the image is found, otherwise
 * it is set to NOK (one).
 * <p>
 * The rectangles x and y coordinates are updated when found.
 * @see JaguarVM#functions(String)
 * @see JaguarDRAW
 * @see JaguarFETCH
 * @see JaguarRECT
 * @see JaguarLOOKUP 
 */
public class JaguarLOOKONCE extends JaguarLOOKUP {

    /**
	 * @param vm
	 * @param src
	 * @param line
	 * @param arg 
	 */
    public JaguarLOOKONCE(JaguarVM vm, File src, String line, String arg) {
        super(vm, src, line, arg);
    }

    protected void execute() {
        JaguarRectangle r = vm.getRect(lowerCaseToken(0));
        if (r == null || (r.x == 0 && r.y == 0)) super.execute(); else {
            Jaguar.setFocusable(true);
            vm.setStatus(JaguarVM.OK);
            JaguarRectangle b = new JaguarRectangle(r, false);
            lookup(r, b, 0, bw, bg);
        }
    }
}
