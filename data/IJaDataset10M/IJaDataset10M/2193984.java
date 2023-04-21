package com.techm.gisv.cqp.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.techm.gisv.cqp.PlugIn;
import com.techm.gisv.cqp.util.Constants;
import com.techm.gisv.cqp.util.ResourceUtil;

/**
 * 
 * CheckStyleProcessor extends BaseProcessor This calss will process
 * the checkstyle analysis
 * 
 */
public class CheckStyleProcessor extends BaseProcessor {

    private Logger logger = Logger.getLogger(CheckStyleProcessor.class);

    /**
     * Checkstyle process
     * 
     * @param resources list of resources to process
     */
    public void process(List<IResource> resources) {
        if (resources == null || resources.size() == 0) {
            return;
        }
        String[] configLocationList = getConfigLocation(resources.get(FIRST_RESOURCE).getProject());
        batchProcess(resources.get(FIRST_RESOURCE).getProject(), ResourceUtil.resourceListToFileList(resources), configLocationList[currentProfile]);
    }

    private void batchProcess(IProject project, List<File> files, String config) {
        Checker checker = initializeChecker(project, config);
        logger.info("starting analysis");
        if (PlugIn.isStopProcess()) {
            logger.info("CheckStyle Process Terminated by user");
            return;
        }
        for (int i = 0, j = files.size(); i < j; i += FILE_BATCH_SIZE) {
            logger.info("    starting batch @ " + i);
            if (PlugIn.isStopProcess()) {
                logger.info("CheckStyle Process Terminated by user");
                return;
            }
            if (i + FILE_BATCH_SIZE > files.size()) {
                checker.process(files.subList(i, files.size()).toArray(new File[1]));
            } else {
                checker.process(files.subList(i, i + FILE_BATCH_SIZE).toArray(new File[1]));
            }
            logger.info("    ending batch @ " + i);
            if (analyzer.getResultsBuilder().markersGreaterThanThreshold(project)) {
                return;
            }
        }
        logger.info("analysis done");
    }

    /**
     * create checkerObject for the Specified Project
     * 
     * @param project IProject
     * @param configLocation String
     * @return Checker
     * 
     * @throws IOException
     */
    private Checker initializeChecker(IProject project, String configLocation) {
        Properties props = PlugIn.loadProperties(configLocation);
        Configuration config = null;
        try {
            config = loadConfig(PlugIn.locateFileResource(configLocation).openStream(), props);
        } catch (IOException e) {
            logger.error(e, e);
        }
        AuditListener listener = new CheckstyleListener(analyzer.getResultsBuilder(), project);
        Checker checker = null;
        try {
            checker = new Checker();
            checker.setModuleFactory(null);
            checker.configure(config);
            checker.addListener(listener);
        } catch (Exception e) {
            logger.error("Unable to create Checker: " + e);
        }
        return checker;
    }

    /**
     * Loading the Configuration
     * 
     * @param inStream InputStream
     * @param aProps Properties
     * @return Configuration
     */
    private Configuration loadConfig(InputStream inStream, Properties aProps) {
        try {
            return ConfigurationLoader.loadConfiguration(inStream, new PropertiesExpander(aProps), false);
        } catch (Exception e) {
            logger.error("Error loading configuration file", e);
            return null;
        }
    }

    /**
     * Getting the Configuration
     * 
     * @param project IProject
     * @return String[]
     */
    private String[] getConfigLocation(IProject project) {
        IConfigurationElement[] profiles = analyzer.getSelectedProfile(project).getChildren();
        String[] locationList = new String[profiles.length];
        for (int j = 0; j < profiles.length; j++) {
            locationList[j] = profiles[j].getAttribute(Constants.LOCATION);
        }
        return locationList;
    }
}
