package com.intel.gpe.client2.common.clientwrapper;

import com.intel.gpe.clients.api.WSRPClient;

/**
 * @version $Id: StringUpdater.java,v 1.1 2006/05/25 07:31:11 vashorin Exp $
 * @author Valery Shorin
 */
public interface StringUpdater<ClientType extends WSRPClient> extends Updater<ClientType, String> {

    public void update(ClientWrapper<ClientType, String> client) throws Exception;
}
