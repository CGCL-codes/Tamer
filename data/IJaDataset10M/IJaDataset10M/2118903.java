package org.libreplan.web.tree;

import static org.libreplan.web.I18nHelper._;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.libreplan.business.trees.ITreeNode;
import org.libreplan.web.orders.OrderElementTreeController;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Treeitem;

/**
 * macro component for order elements tree and similar pages<br />
 *
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 * @author Lorenzo Tilve Álvaro <ltilve@igalia.com>
 */
public abstract class TreeComponent extends HtmlMacroComponent {

    private static final String CONTROLLER_NAME = "treeController";

    public abstract static class Column {

        private String label;

        private String cssClass;

        private String tooltip;

        public Column(String label, String cssClass) {
            this(label, cssClass, null);
        }

        public Column(String label, String cssClass, String tooltip) {
            this.label = label;
            if (!StringUtils.isEmpty(tooltip)) {
                this.tooltip = tooltip;
                cssClass += " help-tooltip";
            }
            this.cssClass = cssClass;
        }

        public String getLabel() {
            return label;
        }

        public String getCssClass() {
            return cssClass;
        }

        public String getTooltip() {
            return tooltip;
        }

        public String getHflex() {
            return cssClass.equals("name") ? "1" : "min";
        }

        public abstract <T extends ITreeNode<T>> void doCell(TreeController<T>.Renderer<T> renderer, Treeitem item, T currentElement);
    }

    protected final Column codeColumn = new Column(_("Code"), "code") {

        @Override
        public <T extends ITreeNode<T>> void doCell(TreeController<T>.Renderer<T> renderer, Treeitem item, T currentElement) {
            renderer.addCodeCell(currentElement);
        }
    };

    protected final Column nameAndDescriptionColumn = new Column(_("Name"), "name") {

        @Override
        public <T extends ITreeNode<T>> void doCell(TreeController<T>.Renderer<T> renderer, Treeitem item, T currentElement) {
            renderer.addDescriptionCell(currentElement);
        }
    };

    protected final Column operationsColumn = new Column(_("Op."), "operations", _("Operations")) {

        @Override
        public <T extends ITreeNode<T>> void doCell(TreeController<T>.Renderer<T> renderer, Treeitem item, T currentElement) {
            renderer.addOperationsCell(item, currentElement);
        }
    };

    protected final Column schedulingStateColumn = new Column(_("Scheduling state"), "scheduling_state", _("Complete, Partially or Not Scheduled. (Drag and drop to move tasks)")) {

        @Override
        public <T extends ITreeNode<T>> void doCell(TreeController<T>.Renderer<T> renderer, Treeitem item, T currentElement) {
            renderer.addSchedulingStateCell(currentElement);
        }
    };

    public abstract List<Column> getColumns();

    public void clear() {
        OrderElementTreeController controller = (OrderElementTreeController) getVariable(CONTROLLER_NAME, true);
        controller.clear();
    }

    public void useController(TreeController<?> controller) {
        doAfterComposeOnController(controller);
        controller.setColumns(getColumns());
        this.setVariable(CONTROLLER_NAME, controller, true);
    }

    public TreeController<?> getController() {
        return (TreeController<?>) getVariable(CONTROLLER_NAME, true);
    }

    private void doAfterComposeOnController(Composer controller) {
        try {
            controller.doAfterCompose(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddElementLabel() {
        return _("Add");
    }

    public boolean isCreateTemplateEnabled() {
        return true;
    }

    public boolean isCreateFromTemplateEnabled() {
        return false;
    }

    public String getRemoveElementLabel() {
        return _("Delete task");
    }
}
