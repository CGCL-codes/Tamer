package org.bug4j.gwt.user.client.bugs;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.bug4j.gwt.user.client.BugModel;
import org.bug4j.gwt.user.client.data.Filter;
import org.bug4j.gwt.user.client.event.BugListChanged;
import org.bug4j.gwt.user.client.graphs.TopGraphView;

/**
 * The tabbed panel with bugs and graphs
 */
public class AllBugsView extends TabLayoutPanel {

    public AllBugsView(final BugModel bugModel, Filter filter) {
        super(2, Style.Unit.EM);
        final BugView bugView = new BugView(bugModel, filter);
        final TopGraphView topGraphView = new TopGraphView(bugModel);
        final Widget topGraphViewWidget = topGraphView.createWidget();
        add(bugView, "Bugs");
        add(topGraphViewWidget, "Charts");
        addSelectionHandler(new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent<Integer> integerSelectionEvent) {
                bugModel.getEventBus().fireEvent(new BugListChanged());
            }
        });
        if (getUserAgent().contains("msie")) {
            selectTab(1);
            final Scheduler scheduler = Scheduler.get();
            scheduler.scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    selectTab(0);
                }
            });
        }
    }

    private static native String getUserAgent();
}
