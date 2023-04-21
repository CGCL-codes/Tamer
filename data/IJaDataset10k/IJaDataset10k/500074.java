package org.jumpmind.pos.activity;

import org.jumpmind.pos.IActivity;
import org.jumpmind.pos.ui.PromptRepsonse;
import org.jumpmind.pos.ui.PromptType;

@Activity
public class SaleActivity extends AbstractActivity {

    @Override
    public ActivityStatus enter(IActivity lastActivity) {
        return ActivityStatus.IDLE;
    }

    @Override
    public ActivityStatus done(IActivity nextActivity) {
        if (context.getActiveTransaction() != null) {
            if (PromptRepsonse.OK == application.prompt(PromptType.OK_CANCEL, "sale.screen.transaction.in.progress")) {
                context.setActiveTransaction(null);
            } else {
                return ActivityStatus.ABORT;
            }
        }
        return ActivityStatus.DONE;
    }
}
