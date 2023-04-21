package sto.orz.rptp.dao;

import sto.orz.dao.hibernate.BaseDaoHibernate;
import sto.orz.rptp.model.Definition;

public class DefinitionDao extends BaseDaoHibernate {

    public void saveDefinition(final Definition definition) {
        if (log.isDebugEnabled()) {
            log.debug("definition's id: " + definition.getId());
        }
        getHibernateTemplate().saveOrUpdate(definition);
        getHibernateTemplate().flush();
    }

    public java.util.List getDefinitionIndex() {
        return getHibernateTemplate().find("select id, name from Definition d order by upper(d.name)");
    }

    public Definition getDefinition(String definitionId) {
        return (Definition) getHibernateTemplate().get(Definition.class, definitionId);
    }

    public void removeDefinition(String definitionId) {
        getHibernateTemplate().delete(getDefinition(definitionId));
    }
}
