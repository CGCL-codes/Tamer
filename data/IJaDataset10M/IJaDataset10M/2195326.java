package kortsoft.kmx.deployer;

import java.io.File;
import javax.management.MBeanRegistrationException;

/**
 * <a href="BaseDeployer.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Alvaro
 * @version $Revision: 1.1 $
 *
 */
public abstract class BaseDeployer implements Deployer {

    /**
     * @param class1
     * @param file
     * @param e1
     */
    protected void notifyRegistrationProblem(Class<? extends Deployer> deployerClass, File file, MBeanRegistrationException e1) {
    }

    /**
     * @param name
     */
    protected void instanceAlreadyExists(String name) {
    }
}
