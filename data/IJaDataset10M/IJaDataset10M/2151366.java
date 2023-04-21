package org.openacs;

import javax.ejb.EJBLocalObject;

/**
 *
 * @author Administrator
 */
public interface HostPropertyLocal extends EJBLocalObject {

    Integer getParentId();

    String getName();

    String getValue();

    void setValue(String value);

    HostsLocal getHost();

    void setHost(HostsLocal host);
}
