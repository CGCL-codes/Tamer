package org.broadleafcommerce.core.pricing.service.workflow;

import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.SequenceProcessor;

public class CompositeActivity extends BaseActivity {

    private SequenceProcessor workflow;

    public ProcessContext execute(ProcessContext context) throws Exception {
        ProcessContext subContext = workflow.doActivities(((PricingContext) context).getSeedData());
        if (subContext.isStopped()) {
            context.stopProcess();
        }
        return context;
    }

    public SequenceProcessor getWorkflow() {
        return workflow;
    }

    public void setWorkflow(SequenceProcessor workflow) {
        this.workflow = workflow;
    }
}
