package com.quesofttech.business.domain.production.iface;

import java.util.List;
import com.quesofttech.business.common.exception.BusinessException;
import com.quesofttech.business.common.exception.DoesNotExistException;
import com.quesofttech.business.domain.production.Routing;

/**
 * The <code>IRoutingServiceRemote</code> bean exposes the business methods
 * in the interface.
 */
public interface IRoutingServiceRemote {

    Routing findRouting(Long id) throws DoesNotExistException;

    List<Routing> findRoutings() throws DoesNotExistException;

    void updateRouting(Routing routing) throws BusinessException;

    void addRouting(Routing routing) throws BusinessException;

    void logicalDeleteRouting(Routing routing) throws BusinessException;
}
