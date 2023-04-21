package jaguar.machine.exceptions;

/**
 * MachineNotFoundException.java
 *
 *
 * Created: Sat Apr 14 16:58:03 2001
 *
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:04 $
 */
public class MachineNotFoundException extends Exception {

    public MachineNotFoundException() {
        super();
    }

    public MachineNotFoundException(String s) {
        super(s);
    }
}
