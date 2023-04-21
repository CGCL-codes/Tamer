package org.opennms.netmgt.dao;

import org.opennms.netmgt.model.RdbmsUser;

/**
 * @author min zhang
 *
 */
public interface RdbmsUserDao extends OnmsDao<RdbmsUser, Integer> {

    public abstract RdbmsUser getByUserid(int userid);

    public abstract RdbmsUser getByUsername(String username);
}
