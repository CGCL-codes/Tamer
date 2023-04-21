package org.osmius.dao.hibernate;

import org.osmius.dao.OsmRulesInstancesDao;
import org.osmius.model.OsmRulesInstances;
import org.osmius.model.OsmRulesInstancesId;
import java.util.List;

public class OsmRulesInstancesDaoHibernate extends BaseDaoHibernate implements OsmRulesInstancesDao {

    public void removeByService(String service) {
        getHibernateTemplate().bulkUpdate("delete OsmRulesInstances ri where ri.id.idnRule in (select sr.id.idnRule from OsmServiceRules sr where sr.id.idnService=?)", service);
    }

    public void saveOsmRulesInstances(int ruleID, String[] instances) {
        if (!(instances == null || instances.length == 0)) {
            for (int i = 0; i < instances.length; i++) {
                OsmRulesInstancesId id = new OsmRulesInstancesId(ruleID, instances[i]);
                getHibernateTemplate().save(new OsmRulesInstances(id));
            }
        }
    }

    public void removeInstanceByService(String idnService, String instance) {
        List rules = getHibernateTemplate().find("select ri.id.idnRule from OsmRulesInstances ri where ri.id.idnRule in (select sr.id.idnRule from OsmServiceRules sr where sr.id.idnService=?) and ri.id.idnInstance=?", new Object[] { idnService, instance });
        getHibernateTemplate().bulkUpdate("delete OsmRulesInstances ri where ri.id.idnRule in (select sr.id.idnRule from OsmServiceRules sr where sr.id.idnService=?) and ri.id.idnInstance=?", new Object[] { idnService, instance });
        if (!(rules == null || rules.size() == 0)) {
            long cont = 0;
            for (int i = 0; i < rules.size(); i++) {
                Integer rule = (Integer) rules.get(i);
                List data = getHibernateTemplate().find("select count(*) from OsmRulesInstances ri where ri.id.idnRule=?", rule);
                cont = (Long) data.get(0);
                if (cont == 0) {
                    getHibernateTemplate().bulkUpdate("delete OsmServiceRules sr where sr.id.idnService=? and sr.id.idnRule=?", new Object[] { idnService, rule });
                    getHibernateTemplate().bulkUpdate("delete OsmRules r where r.idnRule=?", rule);
                }
            }
        }
    }

    public void removeInstance(String instance) {
        List rules = getHibernateTemplate().find("select ri.id.idnRule from OsmRulesInstances ri where ri.id.idnInstance=?", instance);
        getHibernateTemplate().bulkUpdate("delete OsmRulesInstances ri where ri.id.idnInstance=?", instance);
        if (!(rules == null || rules.size() == 0)) {
            long cont = 0;
            for (int i = 0; i < rules.size(); i++) {
                Integer rule = (Integer) rules.get(i);
                List data = getHibernateTemplate().find("select count(*) from OsmRulesInstances ri where ri.id.idnRule=?", rule);
                cont = (Long) data.get(0);
                if (cont == 0) {
                    getHibernateTemplate().bulkUpdate("delete OsmServiceRules sr where sr.id.idnRule=?", rule);
                    getHibernateTemplate().bulkUpdate("delete OsmRules r where r.idnRule=?", rule);
                }
            }
        }
    }
}
