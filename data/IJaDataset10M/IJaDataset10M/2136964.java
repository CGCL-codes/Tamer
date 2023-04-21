package de.fzi.herakles.util;

import java.io.File;
import de.fzi.herakles.commons.configuration.HeraklesConfiguration;
import de.fzi.herakles.commons.configuration.ReasonerConfiguration;
import de.fzi.herakles.commons.configuration.impl.ReasonerConfigurationImp;
import de.fzi.herakles.core.HeraklesOWLlinkServer;

/**
 * Starter of the HERAKLES OWLlink Server.
 * The class provides a <code>main</code> method that loads the
 * configuration files <code>herakles.xml</code> and <code>reasoner.xml</code>,
 * and starts HERAKLES as OWLlink server.
 * <p>
 * This class is the main entry point of the executable JAR created for
 * the HERAKLES core module.
 * 
 * @author Matthias Stumpp
 * @author Juergen Bock
 *
 */
public class HeraklesOWLlinkStarter {

    /**
	 * Main entry point that start the HERAKLES OWLlink server.
	 * @param args Command line arguments (currently not used)
	 */
    public static void main(String[] args) {
        HeraklesConfiguration heraklesConfig = new HeraklesConfiguration();
        heraklesConfig.loadHeraklesConfiguration(new File("herakles.xml"));
        ReasonerConfiguration reasonerConfig = new ReasonerConfigurationImp();
        reasonerConfig.loadRemoteReasonerConfiguration(new File("reasoner.xml"));
        HeraklesOWLlinkServer herakles = new HeraklesOWLlinkServer(heraklesConfig);
        herakles.start(reasonerConfig);
    }
}
