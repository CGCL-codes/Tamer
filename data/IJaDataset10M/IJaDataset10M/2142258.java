package be.vds.jtbtaskplanner.client.core.businessdelegate;

import java.util.Map;
import org.jdesktop.swingx.ws.yahoo.search.Country;
import be.vds.jtbtaskplanner.core.exceptions.DataStoreException;
import be.vds.jtbtaskplanner.core.integration.businessdelegate.interfaces.GlossaryBusinessDelegate;
import be.vds.jtbtaskplanner.persistence.core.dao.interfaces.DaoFactory;

/**
 * This business delegate is used by the model to interact with persistence
 * layer. The business delegate cares for any connection to an application
 * server, a database, a file system, ...
 * 
 * @author gautier
 * 
 */
public class DAODirectGlossaryBusinessDelegate extends GlossaryBusinessDelegate {

    public DAODirectGlossaryBusinessDelegate() {
    }
}
