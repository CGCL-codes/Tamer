package net.sourceforge.configured.rules.rulebase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.configured.annotation.ConfiguredByRules;
import net.sourceforge.configured.utils.ConfiguredAnnotationUtils;
import net.sourceforge.configured.utils.context.AutowiringContext;
import net.sourceforge.configured.utils.visit.ConfigurationVisitor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulebaseManagerImpl implements RulebaseManager {

    protected Logger logger = LoggerFactory.getLogger(RulebaseManagerImpl.class);

    protected Map<String, DependencyResolutionRulebase> rulebaseMap = new HashMap<String, DependencyResolutionRulebase>();

    @Override
    public DependencyResolutionRulebase getRulebase(AutowiringContext autowiringContext, Object parentObject, String fieldName, Class<?> fieldType) {
        String rulebaseToUse = null;
        ConfiguredByRules annotation = null;
        if (logger.isDebugEnabled()) {
            logger.debug("finding rulebase for property {}({}) at path \"{}\"", new Object[] { fieldName, fieldType, autowiringContext.getPropertyPathDescription() });
        }
        if (!autowiringContext.isEmpty()) {
            annotation = ConfiguredAnnotationUtils.getConfiguredByRulesAnnotation(parentObject.getClass(), fieldName);
            if (logger.isDebugEnabled() && annotation != null && isValidName(annotation.rulebase())) {
                logger.debug("found rulebase \"{}\" on annotation of field \"{}\"", annotation.rulebase(), fieldName);
            }
        }
        if (annotation == null) {
            annotation = ConfiguredAnnotationUtils.getConfiguredByRulesAnnotation(fieldType);
            if (logger.isDebugEnabled() && annotation != null && isValidName(annotation.rulebase())) {
                logger.debug("found rulebase \"{}\" on class annotation of \"{}\"", annotation.rulebase(), parentObject == null ? "ROOT_OBJECT" : parentObject.getClass());
            }
        }
        if (annotation != null) {
            rulebaseToUse = annotation.rulebase();
        }
        if (logger.isDebugEnabled() && !isValidName(rulebaseToUse)) {
            logger.debug("did not find a rulebase annotation for the supplied property \"{}\" or its class: {} at \"{}\"", new Object[] { fieldName, fieldType, autowiringContext.getPropertyPathDescription() });
        }
        try {
            boolean notReachedRoot = !autowiringContext.isEmpty();
            boolean searchedContext = false;
            while (!isValidName(rulebaseToUse) && notReachedRoot) {
                searchedContext = true;
                rulebaseToUse = autowiringContext.getRulebaseName();
                notReachedRoot = autowiringContext.moveToParent();
            }
            if (logger.isDebugEnabled() && searchedContext && isValidName(rulebaseToUse)) {
                logger.debug("searching autowiring context, found rulebase \"{}\" for ancestor \"{}\"", rulebaseToUse, autowiringContext.getPropertyPathDescription());
            }
        } finally {
            autowiringContext.resetDepth();
        }
        if (!isValidName(rulebaseToUse)) {
            rulebaseToUse = ConfiguredByRules.DEFAULT_RULEBASE;
        }
        DependencyResolutionRulebase ret = rulebaseMap.get(rulebaseToUse);
        if (ret == null) {
            throw new RuntimeException("No rulebase: " + rulebaseToUse + " could be found");
        }
        return ret;
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
        for (DependencyResolutionRulebase rulebase : rulebaseMap.values()) {
            rulebase.accept(visitor);
        }
    }

    protected boolean isValidName(String rulebaseName) {
        return !StringUtils.isBlank(rulebaseName);
    }

    public Collection<DependencyResolutionRulebase> getRulebases() {
        return rulebaseMap.values();
    }

    public void setRulebases(Collection<DependencyResolutionRulebase> rulebases) {
        rulebaseMap.clear();
        for (DependencyResolutionRulebase rulebase : rulebases) {
            rulebaseMap.put(rulebase.getRulebaseIdentifier(), rulebase);
        }
    }
}
