package com.homedepot.provisioning.utilities;

import java.util.Collection;
import java.util.Iterator;
import com.on.ccm.dbserver.api.CcmComputer;
import com.on.ccm.dbserver.api.CcmDbServer;
import com.on.ccm.dbserver.api.CcmDbServerEntry;
import com.on.ccm.dbserver.api.CcmException;
import com.on.ccm.dbserver.api.CcmServer;

/**
 * Login to a CCM Configuration Server. 
 */
public class CCMServer {

    CcmDbServerEntry ccmDBServerEntry = null;

    CcmServer configServer = null;

    CcmServer repositoryServer = null;

    CcmServer depotServer = null;

    String host = null;

    String username = "siadm";

    String password = "upd9t3m3";

    public CCMServer(String host) {
        this.host = host;
    }

    /**
	 * 
	 * Login to CCM configuration server
	 * 
	 * @throws CcmException
	 */
    @SuppressWarnings("unchecked")
    public void login() throws CcmException {
        CcmDbServer dbServer = null;
        String url = "//" + host + "/" + CcmDbServerEntry.RMI_REGISTRATION_NAME;
        ccmDBServerEntry = new CcmDbServerEntry(url);
        dbServer = ccmDBServerEntry.login(username, password);
        Collection<CcmServer> ccmServers = dbServer.getAllServers();
        Iterator<CcmServer> it = ccmServers.iterator();
        while (it.hasNext()) {
            CcmServer ccmServer = it.next();
            switch(ccmServer.getType()) {
                case CcmServer.SERVER_TYPE_CONFIG:
                    configServer = ccmServer;
                    break;
                case CcmServer.SERVER_TYPE_REPOSITORY:
                    repositoryServer = ccmServer;
                    break;
                case CcmServer.SERVER_TYPE_DEPOT:
                    depotServer = ccmServer;
                    break;
            }
        }
    }

    /**
	 * 
	 * Logout of the CCM Configuration Server
	 * 
	 * @throws CcmException
	 */
    public void logout() throws CcmException {
        if (ccmDBServerEntry == null) return;
        ccmDBServerEntry.logout();
    }

    public CcmDbServerEntry getCcmDBServerEntry() {
        return ccmDBServerEntry;
    }

    public CcmServer getConfigServer() {
        return configServer;
    }

    public CcmServer getRepositoryServer() {
        return repositoryServer;
    }

    public CcmServer getDepotServer() {
        return depotServer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
