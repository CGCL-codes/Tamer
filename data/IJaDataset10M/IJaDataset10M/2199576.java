package puggle;

import java.io.IOException;
import puggle.Resources.Resources;

/**
 *
 * @author gvasil
 */
public class MainPortable extends Main {

    public static void main(String args[]) throws IOException {
        Resources.setPortable(true);
        Main.main(args);
    }
}
