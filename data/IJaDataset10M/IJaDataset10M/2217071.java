package org.inqle.experiment.rapidminer.activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.inqle.core.util.InqleInfo;
import org.osgi.framework.BundleContext;
import com.rapidminer.RapidMiner;

/**
 * @author David Donohue
 * Apr 18, 2008
 */
public class Activator extends Plugin {

    private static final String SYSTEM_PROPERTY_RAPIDMINER_HOME = "rapidminer.home";

    /**
	 * 
	 */
    public Activator() {
        super();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        String rapidMinerHome = InqleInfo.getPluginsDirectory() + context.getBundle().getSymbolicName();
        System.setProperty(SYSTEM_PROPERTY_RAPIDMINER_HOME, rapidMinerHome);
        boolean addWekaOperators = false;
        boolean searchJDBCInLibDir = false;
        boolean searchJDBCInClasspath = false;
        boolean addPlugins = true;
        RapidMiner.init(addWekaOperators, searchJDBCInLibDir, searchJDBCInClasspath, addPlugins);
        super.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        RapidMiner.cleanUp();
        super.stop(context);
    }
}
