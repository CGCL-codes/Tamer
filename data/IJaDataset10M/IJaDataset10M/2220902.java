package com.google.web.bindery.requestfactory.shared.impl;

import com.google.web.bindery.requestfactory.server.impl.FindService;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

/**
 * Request selector interface for implementing a find method.
 */
@Service(FindService.class)
public interface FindRequest extends RequestContext {

    /**
   * Use the implicit lookup in passing EntityProxy types to service methods.
   */
    <P extends EntityProxy> Request<P> find(EntityProxyId<P> proxyId);
}
