package com.hba.web.logger.server.manager;

import com.google.gwt.user.client.rpc.RemoteService;
import com.hba.web.logger.server.business.ApplicationLog;

/**
 * The client side stub for the RPC service.
 */
public interface LoggerService extends RemoteService {

    ApplicationLog[] load() throws IllegalArgumentException;
}
