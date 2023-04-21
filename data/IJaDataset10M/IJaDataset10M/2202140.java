package tests.speed;

import java.rmi.RemoteException;
import com.eaio.plateau.PlateauRegistryImpl;

/**
 * Registers the {@link tests.Multiplier} instance and waits indefinitely.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: PlateauSpeedTestSetup.java,v 1.1 2005/11/20 20:15:54 grnull Exp $
 */
public class PlateauSpeedTestSetup {

    public static void main(String[] args) throws Exception {
        new PlateauRegistryImpl().bind("/SpeedTest", new PlateauSpeedTestImpl());
    }

    public static class PlateauSpeedTestImpl implements SpeedTest {

        /**
   * @see tests.speed.SpeedTest#hello()
   */
        public String hello() throws RemoteException {
            return "Hello World!";
        }

        /**
   * @see tests.speed.SpeedTest#multiply(int, int)
   */
        public int multiply(int a, int b) throws RemoteException {
            return a * b;
        }

        /**
   * @see tests.speed.SpeedTest#concatenate(java.lang.String, java.lang.String)
   */
        public String concatenate(String a, String b) throws RemoteException {
            return a + b;
        }
    }
}
