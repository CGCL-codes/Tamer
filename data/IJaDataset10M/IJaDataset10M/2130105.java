package net.cw.talos.hibernate;

import java.util.ArrayList;
import java.util.List;
import net.cw.dataaccess.DataAccessException;
import net.cw.talos.Securable;
import net.cw.talos.persistence.DefaultSecurable;
import net.cw.talos.persistence.ObjectACL;
import net.cw.talos.persistence.SecurableId;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author <a href="mailto:nowicki@commworld.de">Pawel Nowicki </a>
 * @version $Header:
 *          /cvsroot/talos/talos/src/main/net/cw/talos/hibernate/HibernateAuthorizationDataAccess.java,v
 *          1.2 2005/07/20 14:28:45 novy Exp $
 */
public class HibernateAuthorizationDataAccess extends HibernateDaoSupport implements AuthorizationDataAccess {

    /**
	 * @see net.cw.talos.hibernate.AuthorizationDataAccess#getACL(java.io.Serializable,
	 *      java.lang.String)
	 */
    public ObjectACL getACL(Securable securable, Long zoneId) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Get ACL for [" + securable + "] in zone [" + zoneId + "]");
        }
        SecurableId lookupid = new SecurableId(securable);
        DefaultSecurable wrappedsecurable = loadSecurable(lookupid);
        return wrappedsecurable.getACL(zoneId);
    }

    public List getACLs(Long zoneId) {
        try {
            StringBuilder buf = new StringBuilder("from ");
            buf.append(ObjectACL.class.getName());
            buf.append(" as o inner join fetch o.securable");
            if (zoneId != null) {
                buf.append(" where o.zoneId = :zoneid");
            } else {
                buf.append(" where o.zoneId is null");
            }
            Query q = getSession(false).createQuery(buf.toString());
            if (zoneId != null) {
                q.setParameter("zoneid", zoneId);
            }
            return q.list();
        } catch (HibernateException e) {
            this.logger.warn(e);
            throw new DataAccessException(e);
        }
    }

    public void deleteACLs(ObjectACL objACL) {
        try {
            objACL.getSecurable().removeACL(objACL);
            getSession(false).delete(objACL);
        } catch (HibernateException e) {
            this.logger.warn(e);
            throw new DataAccessException(e);
        }
    }

    public List getACLs(Securable securable) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Get ACLs for [" + securable + "]");
        }
        return new ArrayList(loadSecurable(new SecurableId(securable)).getACLs());
    }

    /**
	 * Loads a DefaultSecurable Object from the Database. If it doesn�t exist,
	 * it will be created.
	 *
	 * @param id
	 * @return
	 */
    public DefaultSecurable loadSecurable(SecurableId id) {
        DefaultSecurable def = (DefaultSecurable) getSession(false).get(DefaultSecurable.class, id);
        if (def == null) {
            def = new DefaultSecurable(id, null);
            saveDefaultSecurable(def);
        }
        return def;
    }

    private void saveDefaultSecurable(DefaultSecurable sec) {
        getSession(false).save(sec);
    }

    public void delete(DefaultSecurable securable) {
        try {
            getSession(false).delete(securable);
        } catch (HibernateException e) {
            this.logger.warn(e);
            throw new DataAccessException(e);
        }
    }
}
