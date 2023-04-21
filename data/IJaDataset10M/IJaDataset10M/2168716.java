package org.xmdl.taslak.service;

import java.util.Collection;
import org.xmdl.ida.lib.service.GenericManager;
import org.xmdl.taslak.model.*;
import org.xmdl.taslak.model.search.*;

/**
 *
 * OrderElement Service interface
 *  
 * $Id$
 *
 * @generated
 */
public interface OrderElementManager extends GenericManager<OrderElement, Long> {

    /**
     * @generated
     */
    Collection<OrderElement> search(OrderElementSearch orderElementSearch);

    /**
     * @generated
     */
    void copyFrom(Order source, Order destination);
}
