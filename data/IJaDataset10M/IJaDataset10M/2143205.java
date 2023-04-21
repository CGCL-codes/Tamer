package org.pojosoft.lms.web.gwt.client.learningplan;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import org.pojosoft.lms.web.gwt.client.course.CourseServiceAsync;
import org.pojosoft.lms.web.gwt.client.support.ServiceLocator;
import org.pojosoft.ria.gwt.client.meta.ActionColumnMeta;
import org.pojosoft.ria.gwt.client.meta.ActionMeta;
import org.pojosoft.ria.gwt.client.meta.CollectionSummaryDetailLayoutMeta;
import org.pojosoft.ria.gwt.client.meta.ColumnMeta;
import org.pojosoft.ria.gwt.client.service.UserMessageUtils;
import org.pojosoft.ria.gwt.client.ui.AbstractCollectionTreeTableCellRenderer;
import org.pojosoft.ria.gwt.client.ui.ErrorDialogBox;
import org.pojosoft.ria.gwt.client.ui.support.WidgetFactory;
import org.pojosoft.ria.gwt.client.ui.treetable.ModelRow;
import org.pojosoft.ria.gwt.client.ui.treetable.TreeTable;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link org.pojosoft.ria.gwt.client.ui.treetable.TreeTableCellRenderer renderer} implementation for rendering the
 * cells of the User Registrations TreeTable in the MyLearning module.
 *
 * @author POJO Software
 */
public class MyLearningUserRegistrationTreeTableCellRenderer extends AbstractCollectionTreeTableCellRenderer {

    public static final String ENROLLED = "ENROLLED";

    protected String LABEL_UNREGISTER = "label.Unregister";

    protected String LABEL_REMOVE_FROM_WAITLIST = "label.RemoveFromWaitList";

    protected Widget createActionCellWidget() {
        CollectionSummaryDetailLayoutMeta layoutMeta = rendererContext.getLayoutMeta();
        List columnMetas = layoutMeta.summary.treeTable.columns;
        ActionColumnMeta actionColumnMeta = null;
        for (Iterator iterator = columnMetas.iterator(); iterator.hasNext(); ) {
            ColumnMeta columnMeta = (ColumnMeta) iterator.next();
            if (columnMeta instanceof ActionColumnMeta) {
                actionColumnMeta = (ActionColumnMeta) columnMeta;
                break;
            }
        }
        if (actionColumnMeta == null) return null;
        for (Iterator iterator = actionColumnMeta.actions.iterator(); iterator.hasNext(); ) {
            final ActionMeta actionMeta = (ActionMeta) iterator.next();
            if (actionMeta.name.equals("unregister")) {
                LABEL_UNREGISTER = actionMeta.label;
            } else if (actionMeta.name.equals("removeFromWaitList")) {
                LABEL_REMOVE_FROM_WAITLIST = actionMeta.label;
            }
        }
        HorizontalPanel actionContainer = WidgetFactory.createHorizontalPanel(false, "1%");
        actionContainer.addStyleName("pojo-TreeTable-Cell");
        final ModelRow row = rendererContext.getModelRow();
        final String registrationStatus = (String) row.getValue("status.id");
        String linkText = registrationStatus.equals(ENROLLED) ? LABEL_UNREGISTER : LABEL_REMOVE_FROM_WAITLIST;
        Hyperlink actionLink;
        actionLink = new NoBubblingOnClickEventHyperlink();
        actionLink.setHTML(linkText);
        actionLink.setTargetHistoryToken(linkText);
        actionLink.setStyleName("pojo-ActionLink");
        actionLink.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                String scheduleId = (String) row.getValue("schedule.id");
                String confirmationMsg = registrationStatus.equals(ENROLLED) ? "Unregister from schedule " + scheduleId + " ?" : "Remove from schedule " + scheduleId + "'s waitlist ?";
                if (!Window.confirm(confirmationMsg)) return;
                unregisterUser(widget, rendererContext.getTreeTable(), row);
            }
        });
        actionContainer.add(actionLink);
        return actionContainer;
    }

    class NoBubblingOnClickEventHyperlink extends Hyperlink {

        public void onBrowserEvent(Event event) {
            if ((DOM.eventGetType(event) == Event.ONCLICK)) {
                DOM.eventCancelBubble(event, true);
            }
            super.onBrowserEvent(event);
        }
    }

    String userId;

    protected void unregisterUser(final Widget sender, final TreeTable treeTable, final ModelRow scheduleRow) {
        if (userId == null) {
            getUserId(scheduleRow, new AsyncCallback() {

                public void onFailure(Throwable throwable) {
                    ErrorDialogBox.showError(UserMessageUtils.getMessage("error.MyLearningPlan.GetUserIdFromUserScheduleRegistrationFailed", scheduleRow.getId()), throwable.getMessage());
                }

                public void onSuccess(Object object) {
                    unregisterUser(sender, treeTable, scheduleRow, (String) object);
                }
            });
        } else {
            unregisterUser(sender, treeTable, scheduleRow, userId);
        }
    }

    protected void unregisterUser(final Widget sender, final TreeTable treeTable, final ModelRow registrationRow, final String userId) {
        CourseServiceAsync courseService = ServiceLocator.lookupCourseService();
        courseService.unregisterUser(userId, (String) registrationRow.getValue("schedule.id"), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                ErrorDialogBox.showError(UserMessageUtils.getMessage("error.MyLearningPlan.DeregistrationFailed", userId, (String) registrationRow.getValue("schedule.id")), throwable.getMessage());
            }

            public void onSuccess(Object object) {
                treeTable.removeRow(registrationRow);
            }
        });
    }

    protected void getUserId(ModelRow scheduleRow, final AsyncCallback callback) {
        if (userId != null) {
            callback.onSuccess(userId);
            return;
        }
        CourseServiceAsync courseService = ServiceLocator.lookupCourseService();
        courseService.getUserIdByRegistrationId(new Long(scheduleRow.getId()), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(Object object) {
                userId = (String) object;
                callback.onSuccess(object);
            }
        });
    }
}
