package de.juwimm.cms.safeguard.model;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import de.juwimm.cms.util.SequenceHelper;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmSimplePwHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: RealmSimplePwHbmDaoImpl.java 42 2009-02-27 23:14:16Z skulawik $
 */
public class RealmSimplePwHbmDaoImpl extends RealmSimplePwHbmDaoBase {

    private static Logger log = Logger.getLogger(RealmSimplePwHbmImpl.class);

    @Override
    public RealmSimplePwHbm create(RealmSimplePwHbm realmSimplePwHbm) {
        try {
            Integer id = SequenceHelper.getSequenceSession().getNextSequenceNumber("realm_simple_pw.simple_pw_realm_id");
            realmSimplePwHbm.setSimplePwRealmId(id);
        } catch (Exception e) {
            log.error("Error creating primary key", e);
        }
        return super.create(realmSimplePwHbm);
    }

    @Override
    protected void handleCreate(Element element) throws Exception {
        RealmSimplePwHbm realm = RealmSimplePwHbm.Factory.newInstance();
        String name = XercesHelper.getNodeValue(element, "realmName");
        realm.setRealmName(name);
        String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
        realm.setLoginPageId(loginPageId);
        this.create(realm);
    }

    @Override
    protected void handleCreate(Element element, boolean newId) throws Exception {
        RealmSimplePwHbm realm = RealmSimplePwHbm.Factory.newInstance();
        String name = XercesHelper.getNodeValue(element, "realmName");
        realm.setRealmName(name);
        String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
        realm.setLoginPageId(loginPageId);
        if (newId) {
            this.create(realm);
        } else {
            realm.setSimplePwRealmId(Integer.valueOf(XercesHelper.getNodeValue(element, "simplePwRealmId")));
            getHibernateTemplate().save(realm);
        }
    }

    @Override
    protected void handleCreate(String realmName, String owner, Integer siteId) throws Exception {
        RealmSimplePwHbm realm = RealmSimplePwHbm.Factory.newInstance();
        realm.setRealmName(realmName);
        this.create(realm);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findAll(final int transform) {
        return this.findAll(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r");
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findBySiteId(final int transform, final java.lang.Integer siteId) {
        return this.findBySiteId(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r where r.site.siteId = ?", siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByOwner(final int transform, final java.lang.String owner) {
        return this.findByOwner(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r where r.owner.userId = ?", owner);
    }

    public java.lang.Object findByOwnerAndName(final int transform, final java.lang.String owner, final java.lang.String realmName) {
        return this.findByOwnerAndName(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r where r.owner.userId = ? and r.realmName = ?", owner, realmName);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByOwnerAndSite(final int transform, final java.lang.String owner, final java.lang.Integer siteId) {
        return this.findByOwnerAndSite(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r where r.owner.userId = ? and r.site.siteId = ?", owner, siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByLoginPage(final int transform, final java.lang.String loginPageId) {
        return this.findByLoginPage(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwHbm as r where r.loginPageId = ?", loginPageId);
    }
}
