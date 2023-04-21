package org.osmius.dao.hibernate;

import org.osmius.dao.OsmEventTemplateDao;
import org.osmius.model.OsmEventTemplate;
import org.osmius.model.OsmEventTemplateId;
import java.util.List;
import java.util.Vector;

/**
 * @see org.osmius.dao.OsmEventTemplateDao
 */
public class OsmEventTemplateDaoHibernate extends BaseDaoHibernate implements OsmEventTemplateDao {

    public List getOsmEventTemplates(OsmEventTemplate osmEventTemplate) {
        List osmEventTemplates;
        if (osmEventTemplate == null) {
            osmEventTemplates = getHibernateTemplate().find("from OsmEventTemplate order by id.typInstance, id.idnTemplate");
        } else {
            StringBuffer queryString = new StringBuffer("from OsmEventTemplate  where ");
            String[] paramNames;
            Vector names = new Vector();
            Object[] paramValues;
            Vector values = new Vector();
            if (osmEventTemplate.getId() != null) {
                if (osmEventTemplate.getId().getTypInstance() != null) {
                    queryString.append(" id.typInstance = :typInstance ");
                    names.add("typeInstance");
                    values.add(osmEventTemplate.getId().getTypInstance());
                }
                if (osmEventTemplate.getId().getIdnTemplate() != null) {
                    if (names.size() > 0) {
                        queryString.append(" and ");
                    }
                    queryString.append(" id.idnTemplate = :idnTemplate ");
                    names.add("idnTemplate");
                    values.add(osmEventTemplate.getId().getIdnTemplate());
                }
            }
            queryString.append(" order by id.typInstance, id.idnTemplate");
            paramNames = new String[names.size()];
            names.toArray(paramNames);
            paramValues = new Object[values.size()];
            values.toArray(paramValues);
            osmEventTemplates = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, paramValues);
        }
        return osmEventTemplates;
    }

    public OsmEventTemplate getOsmEventTemplate(String typInstance, String idnTemplate) {
        OsmEventTemplate osmEventTemplate = (OsmEventTemplate) getHibernateTemplate().get(OsmEventTemplate.class, new OsmEventTemplateId(typInstance, idnTemplate));
        return osmEventTemplate;
    }

    public void saveOrUpdateOsmEventTemplate(OsmEventTemplate osmEventTemplate) {
        getHibernateTemplate().saveOrUpdate(osmEventTemplate);
    }

    public void removeOsmEventTemplate(String typInstance, String idnTemplate) {
        getHibernateTemplate().bulkUpdate("delete OsmTypeventsTemplate ott where ott.id.typInstance=? and ott.id.idnTemplate=?", new Object[] { typInstance, idnTemplate });
        getHibernateTemplate().bulkUpdate("delete OsmEventTemplate oet where oet.id.typInstance=? and oet.id.idnTemplate=?", new Object[] { typInstance, idnTemplate });
    }
}
