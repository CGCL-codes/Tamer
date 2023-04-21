package eu.keep.characteriser.registry;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import eu.keep.softwarearchive.pathway.Pathway;

/**
 * This class represents Totem's implementation of the
 * {@code Registry} interface {@link eu.keep.characteriser.registry.Registry}.
 * Currently a stub since the registry has not been updated yet for
 * allowing this interaction between the two systems.
 * Returns an empty list of Pathway objects
 * 
 * @author Edo Noordermeer
 */
public class TotemRegistry implements Registry {

    private static final Logger logger = Logger.getLogger(TotemRegistry.class.getName());

    /**
     * @inheritDoc
     */
    @Override
    public List<Pathway> getEmulationPathWays(String fileFormat) {
        List<Pathway> pathways = new ArrayList<Pathway>();
        logger.info("Querying the TOTEM technical registry for an emulation pathway for format " + fileFormat + "...");
        logger.info("TOTEM provides the following pathways for format " + fileFormat + ": " + pathways);
        return pathways;
    }
}
