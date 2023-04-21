package org.signserver.groupkeyservice.server;

import javax.persistence.EntityManager;
import org.signserver.common.WorkerConfig;
import org.signserver.server.cryptotokens.IExtendedCryptoToken;

/**
 * Base class of a BaseGroup Key Service taking care of basic functionality
 * such as initializing and creating the extended crypto token.
 * 
 * @author Philip Vendil 23 nov 2007
 * @version $Id: BaseGroupKeyService.java 2267 2012-03-23 08:20:31Z netmackan $
 */
public abstract class BaseGroupKeyService implements IGroupKeyService {

    protected int workerId;

    protected WorkerConfig config;

    protected EntityManager em;

    protected IExtendedCryptoToken ect;

    /**
     * @see org.signserver.server.IWorker#init(int, org.signserver.common.WorkerConfig, org.signserver.server.WorkerContext, javax.persistence.EntityManager)
     */
    @Override
    public void init(int workerId, WorkerConfig config, EntityManager em, IExtendedCryptoToken ect) {
        this.workerId = workerId;
        this.config = config;
        this.em = em;
        this.ect = ect;
    }
}
