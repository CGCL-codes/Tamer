package de.sicari.webservice;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.apache.axis.deployment.wsdd.providers.WSDDJavaRPCProvider;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;

/**
 * This is the plug-in class for the {@link SicariProvider} required by
 * the <i>Apache Axis</i> framework.
 *
 * @author Matthias Pressfreund
 * @version "$Id: WSDDJavaSicariProvider.java 204 2007-07-11 19:26:55Z jpeters $"
 */
public class WSDDJavaSicariProvider extends WSDDJavaRPCProvider {

    /**
     * The <code>Logger</code> instance for this class
     */
    private static Logger log_ = LoggerFactory.getLogger("webservice");

    /**
     * The identifier of the {@link SicariProvider}
     */
    public static final String PROVIDER_SICARI = "SicAri";

    /**
     * Create a <code>WSDDJavaSicariProvider</code>.
     */
    public WSDDJavaSicariProvider() {
        super();
        log_.debug("Instance successfully created");
    }

    public String getName() {
        return PROVIDER_SICARI;
    }

    public Handler newProviderInstance(WSDDService service, EngineConfiguration registry) throws Exception {
        return new SicariProvider();
    }
}
