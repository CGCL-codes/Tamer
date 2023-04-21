package ch.ethz.dcg.spamato.filter.razor.config;

import java.io.File;
import java.util.logging.Logger;
import ch.ethz.dcg.spamato.base.common.util.StringUtils;
import ch.ethz.dcg.spamato.filter.razor.*;

/**
 * @author simon
 */
public class NominationServerList extends ServerList {

    private DiscoveryServerList discoveryServers;

    private Logger logger;

    public NominationServerList(File file, DiscoveryServerList discoveryServers, Logger logger) {
        this.discoveryServers = discoveryServers;
        this.logger = logger;
        init(file, logger);
    }

    protected String getServerListFileComment() {
        return "sorted nomination server list, do not edit unless you know what you're doing!";
    }

    protected String getServerListServerKey() {
        return RazorConstants.NOMINATION_SERVER_KEY;
    }

    protected String getServerListLastUpdateKey() {
        return RazorConstants.NOMINATION_LASTUPDATE_KEY;
    }

    protected String getServerListUpdateIntervallKey() {
        return RazorConstants.NOMINATION_UPDATE_INTERVALL_KEY;
    }

    protected void doUpdate() throws RazorServerConnectionException {
        RazorCommunicationEngine commEngine = new RazorCommunicationEngine(discoveryServers, logger);
        String[] nServers = null;
        try {
            nServers = commEngine.askForNominationServerList();
        } catch (RazorServerConnectionException e) {
            try {
                discoveryServers.forceUpdate();
                commEngine = new RazorCommunicationEngine(discoveryServers, logger);
                nServers = commEngine.askForNominationServerList();
            } catch (RazorServerConnectionException rsce) {
                throw rsce;
            }
        }
        StringUtils.randomize(nServers);
        if ((nServers != null) && (nServers.length > 0)) setNewServers(nServers);
    }

    public void forceUpdate() throws RazorServerConnectionException {
        discoveryServers.forceUpdate();
        super.forceUpdate();
    }
}
