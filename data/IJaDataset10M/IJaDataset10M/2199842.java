package com.markatta.hund.wicket.pages;

import com.google.inject.Inject;
import com.markatta.hund.core.WorkItem;
import com.markatta.hund.core.WorkManager;
import com.markatta.hund.wicket.DefaultPage;
import java.util.List;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

/**
 *
 * @author johan
 */
public class WorkQueue extends DefaultPage {

    @Inject
    private transient WorkManager workManager;

    private class WorkItemView extends DataView {

        WorkItemView(String label, List items) {
            super(label, new ListDataProvider(items));
        }

        protected void populateItem(Item item) {
            final WorkItem workItem = (WorkItem) item.getModelObject();
            item.add(new Label("label", new Model(workItem.getLabel())));
        }
    }

    ;

    public WorkQueue() {
        super();
        List<WorkItem> executing = workManager.getExecuting();
        DataView executingView = new WorkItemView("executing", executing);
        executingView.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND));
        add(executingView);
        List<WorkItem> items = workManager.getWorkQueue();
        DataView workItemView = new WorkItemView("scheduled", items);
        workItemView.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND));
        add(workItemView);
    }
}
