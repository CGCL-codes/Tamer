package es.randres.jdo.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

/**
 * Activity DTO.
 */
@ProxyFor(es.randres.jdo.server.domain.Activity.class)
public interface ActivityProxy extends EntityProxy {

    public String getId();

    public String getDescription();

    public void setId(String payloadId);

    public void setDescription(String description);

    EntityProxyId<ActivityProxy> stableId();
}
