package org.pojosoft.lms.web.gwt.client.user;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.pojosoft.ria.gwt.client.meta.ActionColumnMeta;
import org.pojosoft.ria.gwt.client.meta.ActionMeta;
import org.pojosoft.ria.gwt.client.meta.CollectionSummaryDetailLayoutMeta;
import org.pojosoft.ria.gwt.client.meta.ColumnMeta;
import org.pojosoft.ria.gwt.client.service.UserMessageUtils;
import org.pojosoft.ria.gwt.client.ui.DefaultCollectionTreeTableCellRenderer;
import org.pojosoft.ria.gwt.client.ui.ErrorDialogBox;
import org.pojosoft.ria.gwt.client.ui.support.WidgetFactory;
import org.pojosoft.ria.gwt.client.ui.treetable.TreeTable;
import org.pojosoft.ria.gwt.client.ui.treetable.TreeTableUtils;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link org.pojosoft.ria.gwt.client.ui.treetable.TreeTableCellRenderer renderer} implementation for rendering a
 * TreeTable cell of a role-users TreeTable. For supporting a many-to-many remove of a role-user.
 *
 * @author POJO Software
 */
public class RoleUserTreeTableCellRenderer extends DefaultCollectionTreeTableCellRenderer {

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
        HorizontalPanel actionContainer = WidgetFactory.createHorizontalPanel(false, "1%");
        actionContainer.addStyleName("pojo-TreeTable-Cell");
        for (Iterator iterator = actionColumnMeta.actions.iterator(); iterator.hasNext(); ) {
            final ActionMeta actionMeta = (ActionMeta) iterator.next();
            if (actionMeta.name.equalsIgnoreCase("remove")) {
                String image = actionMeta.image != null ? actionMeta.image : "item.gif";
                RoleUserTreeTableCellRenderer.ActionImage actionLink = new RoleUserTreeTableCellRenderer.ActionImage("_images/" + image, actionMeta);
                actionLink.addStyleName("pojo-ImageLink");
                actionLink.setTitle(actionMeta.label);
                if (actionContainer.getWidgetCount() > 0) actionContainer.add(WidgetFactory.createHTML("&nbsp;&nbsp;"));
                actionContainer.add(actionLink);
            }
        }
        return actionContainer;
    }

    protected class ActionImage extends Image {

        ActionMeta actionMeta;

        public ActionImage(String string, ActionMeta actionMeta) {
            super(string);
            this.actionMeta = actionMeta;
        }

        public void onBrowserEvent(com.google.gwt.user.client.Event event) {
            switch(DOM.eventGetType(event)) {
                case com.google.gwt.user.client.Event.ONCLICK:
                    {
                        if (Window.confirm("Remove record with id " + rendererContext.getModelRow().getId() + "?")) {
                            DOM.eventCancelBubble(event, true);
                            onRemove(this, actionMeta);
                        }
                        break;
                    }
                default:
                    {
                        super.onBrowserEvent(event);
                    }
            }
        }
    }

    void onRemove(final Widget sender, ActionMeta actionMeta) {
        UserServiceAsync userService = org.pojosoft.lms.web.gwt.client.support.ServiceLocator.lookupUserService();
        userService.removeRoleFromUser(rendererContext.getModelRow().getId(), (String) rendererContext.get("moduleModelObjectId"), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                ErrorDialogBox.showError(UserMessageUtils.getMessage("error.RemoveModelObjectFailed", rendererContext.getModelRow().getModelObjectName(), rendererContext.getModelRow().getId()), throwable.getMessage());
            }

            public void onSuccess(Object object) {
                TreeTable treeTable = rendererContext.getTreeTable();
                TreeTableUtils.removeRowFromTreeTableAndSelectNextRow(treeTable, rendererContext.getModelRow());
            }
        });
    }
}
