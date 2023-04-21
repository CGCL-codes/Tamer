package de.juwimm.cms.safeguard.model;

import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: RealmJdbcHbmImpl.java 16 2009-02-17 06:08:37Z skulawik $
 */
public class RealmJdbcHbmImpl extends RealmJdbcHbm {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = 8995923002238698309L;

    /**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#getRealmJdbcValue()
	 */
    @Override
    public RealmJdbcValue getRealmJdbcValue() {
        RealmJdbcValue value = new RealmJdbcValue();
        value.setJndiName(this.getJndiName());
        value.setJdbcRealmId(this.getJdbcRealmId());
        value.setStatementRolePerUser(this.getStatementRolePerUser());
        value.setStatementUser(this.getStatementUser());
        value.setLoginPageId(this.getLoginPageId());
        value.setRealmName(this.getRealmName());
        return value;
    }

    /**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#setRealmJdbcValue(de.juwimm.cms.safeguard.vo.RealmJdbcValue)
	 */
    @Override
    public void setRealmJdbcValue(RealmJdbcValue realmJdbcValue) {
        this.setJndiName(realmJdbcValue.getJndiName());
        this.setRealmName(realmJdbcValue.getRealmName());
        this.setStatementRolePerUser(realmJdbcValue.getStatementRolePerUser());
        this.setStatementUser(realmJdbcValue.getStatementUser());
        this.setLoginPageId(realmJdbcValue.getLoginPageId());
    }

    /**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#toXml()
	 */
    @Override
    public String toXml() {
        ToXmlHelper helper = new ToXmlHelper();
        StringBuffer sb = new StringBuffer("<realmJdbc>");
        sb.append(helper.getXMLNode("jdbcRealmId", this.getJdbcRealmId().toString()));
        sb.append("<realmName><![CDATA[").append(this.getRealmName()).append("]]></realmName>\n");
        if (this.getLoginPageId() != null && this.getLoginPageId().length() > 0) sb.append(helper.getXMLNode("loginPageId", this.getLoginPageId()));
        sb.append(helper.getXMLNode("jndiName", this.getJndiName()));
        sb.append("<statementUser><![CDATA[").append(this.getStatementUser()).append("]]></statementUser>\n");
        sb.append("<statementRolePerUser><![CDATA[").append(this.getStatementRolePerUser()).append("]]></statementRolePerUser>\n");
        sb.append("</realmJdbc>");
        return sb.toString();
    }
}
