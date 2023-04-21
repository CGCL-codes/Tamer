package app.sentinel;

import java.util.Properties;
import remedium.Remedium;

/**
 * The global definition for this application
 * @author Nuno Brito
 */
public class global {

    public boolean start(Remedium rem, Properties parameters, long assignedRemLock) {
        return true;
    }
}
