package org.openedc.core.domain.model;

import org.openedc.core.persistence.Persistent;

/**
 *
 * @author openedc
 */
public interface Subscription extends Persistent {

    String getDescription();

    String getName();

    SystemInfo getSystemInfo();

    void setDescription(String description);

    void setName(String name);

    void setSystemInfo(SystemInfo systemInfo);
}
