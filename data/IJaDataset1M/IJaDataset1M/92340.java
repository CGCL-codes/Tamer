package at.redcross.tacos.web.beans.bl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.entity.FilterRuleParam;
import at.redcross.tacos.dbal.helper.FilterRuleHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

/**
 * Synchronizes the available rules with the database.
 */
public class RuleDefinitionServlet extends HttpServlet {

    private static final long serialVersionUID = -874845960031052211L;

    /** log the changes */
    private static final Log logger = LogFactory.getLog(RuleDefinitionServlet.class);

    @Override
    public void init(ServletConfig arg0) throws ServletException {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            syncronizeRules(manager);
        } catch (Exception ex) {
            throw new ServletException("Failed to initializ rule registry", ex);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /**
     * Synchronizes the defined actions with the ones form the database
     */
    protected void syncronizeRules(EntityManager manager) {
        List<FilterRule> rules = FilterRuleHelper.list(manager);
        List<IFilterRule> definitions = FilterRuleRegistry.getInstance().getRules();
        Map<String, FilterRule> ruleMap = new HashMap<String, FilterRule>();
        for (FilterRule rule : rules) {
            ruleMap.put(rule.getName(), rule);
        }
        Map<String, IFilterRule> definitionMap = new HashMap<String, IFilterRule>();
        for (IFilterRule defintion : definitions) {
            definitionMap.put(defintion.getId(), defintion);
        }
        Set<String> ruleSet = new HashSet<String>(ruleMap.keySet());
        ruleSet.removeAll(definitionMap.keySet());
        for (String ruleId : ruleSet) {
            FilterRule rule = ruleMap.get(ruleId);
            manager.remove(rule);
            logger.info("Removing rule '" + rule + "'");
        }
        Set<String> definitionSet = new HashSet<String>(definitionMap.keySet());
        definitionSet.removeAll(ruleMap.keySet());
        for (String definitionId : definitionSet) {
            IFilterRule definition = definitionMap.get(definitionId);
            FilterRule rule = new FilterRule();
            rule.setName(definition.getId());
            rule.setDescription(definition.getDescription());
            rule.setDescriptionTemplate(definition.getDescription());
            for (FilterParamDefinition paramDef : definition.getParams()) {
                FilterRuleParam param = new FilterRuleParam();
                param.setName(paramDef.getId());
                param.setDescription(paramDef.getDescription());
                manager.persist(param);
                rule.getParams().add(param);
            }
            manager.persist(rule);
            logger.info("Adding rule '" + rule + "'");
        }
        EntityManagerHelper.commit(manager);
    }
}
