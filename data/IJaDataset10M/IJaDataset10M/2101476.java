package de.flexiprovider;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jcryptool.core.operations.providers.AbstractProviderController;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.nf.FlexiNFProvider;
import de.flexiprovider.pqc.FlexiPQCProvider;

/**
 * @author tkern
 *
 */
public class FlexiProviderController extends AbstractProviderController {

    /** The log4j logger */
    private static final Logger logger = FlexiProviderPlugin.getLogManager().getLogger(FlexiProviderController.class.getName());

    /**
	 * 
	 */
    public FlexiProviderController() {
    }

    /**
	 * @see org.jcryptool.core.operations.providers.AbstractProviderController#addProviders()
	 */
    @Override
    public List<String> addProviders() {
        List<String> providers = new ArrayList<String>(4);
        Provider flexiCore = new FlexiCoreProvider();
        flexiCore.remove("SecureRandom.BBS");
        flexiCore.remove("SecureRandom.BBSRandom");
        flexiCore.remove("Alg.Alias.SecureRandom.BBSRandom");
        int pos = Security.insertProviderAt(flexiCore, 1);
        providers.add(flexiCore.getName() + AbstractProviderController.SEPARATOR + flexiCore.getInfo());
        logger.debug("Security Provider '" + flexiCore.getName() + "' added to pos: " + pos);
        Provider flexiEC = new FlexiECProvider();
        pos = Security.insertProviderAt(flexiEC, 2);
        providers.add(flexiEC.getName() + AbstractProviderController.SEPARATOR + flexiEC.getInfo());
        logger.debug("Security Provider '" + flexiEC.getName() + "' added to pos: " + pos);
        Provider flexiPQC = new FlexiPQCProvider();
        pos = Security.insertProviderAt(flexiPQC, 3);
        providers.add(flexiPQC.getName() + AbstractProviderController.SEPARATOR + flexiPQC.getInfo());
        logger.debug("Security Provider '" + flexiPQC.getName() + "' added to pos: " + pos);
        Provider flexiNF = new FlexiNFProvider();
        pos = Security.insertProviderAt(flexiNF, 4);
        providers.add(flexiNF.getName() + AbstractProviderController.SEPARATOR + flexiNF.getInfo());
        logger.debug("Security Provider '" + flexiNF.getName() + "' added to pos: " + pos);
        return providers;
    }
}
