package kortsoft.kmx.deployer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 * <a href="KmxDaemonMBean.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Alvaro
 * @version $Revision: 1.2 $
 *
 */
public interface KmxDaemonMBean {

    public void runDaemon();

    public void setWatchDirectories(List<File> watchDirectories);

    public List<File> getWatchDirectories();

    public void addExtraDeployer(Class<? extends Deployer> deployerType, FilenameFilter filter);

    public ClassLoader getBaseClassLoader();
}
