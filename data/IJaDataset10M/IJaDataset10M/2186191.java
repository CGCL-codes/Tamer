package it.ancona.comune.ankonhippo.workflows.ankonhippo;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import it.ancona.comune.ankonhippo.workflows.ankonhippo.data.TaxonomyContainer;
import java.util.Map;
import nl.hippo.cms.workflows.shared.ConditionComponent;

public class AllPublishNowCondition extends ConditionComponent {

    public AllPublishNowCondition() {
        super();
    }

    public boolean passesConditionImpl(final Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        TaxonomyContainer taxonomyContainer = (TaxonomyContainer) transientVars.get(AHConstants.TAXONOMY_CONTAINER);
        return taxonomyContainer.allPublishNow();
    }
}
