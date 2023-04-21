package org.pojosoft.ria.gwt.client;

import com.google.gwt.user.client.ui.Widget;
import org.pojosoft.ria.gwt.client.meta.ActionMeta;
import org.pojosoft.ria.gwt.client.meta.CollectionSummaryDetailLayoutMeta;
import org.pojosoft.ria.gwt.client.meta.RecordLayoutMeta;
import org.pojosoft.ria.gwt.client.ui.treetable.TreeTable;

/**
 * Represents a base event fired within {@link CollectionSummaryDetailLayoutMeta}'s rendered UI (widgets).
 *
 * @author POJO Software
 * @see RenderEvent
 * @see RenderEvent
 * @see AddNewModelObjectEvent
 */
public class CollectionLayoutEvent extends Event {

    public static final String RENDER = "RenderEvent";

    public static final String ADD_NEW = "AddNewEvent";

    public CollectionLayoutEvent() {
    }

    public CollectionLayoutEvent(Widget sender, String name) {
        super(sender, name);
    }

    /**
   * Event fired when the CollectionSummaryDetailLayoutMeta is to be rendered
   *
   * @author POJO Software
   */
    public static class RenderEvent extends CollectionLayoutEvent {

        protected Renderer renderer;

        protected RecordLayoutMeta layoutMeta;

        /**
     * @param renderer the renderer
     * @param layoutMeta the layout metadata that the renderer is using to render
     */
        public RenderEvent(Renderer renderer, RecordLayoutMeta layoutMeta) {
            super(null, RENDER);
            this.renderer = renderer;
            this.layoutMeta = layoutMeta;
        }

        public Renderer getRenderer() {
            return renderer;
        }

        public void setRenderer(Renderer renderer) {
            this.renderer = renderer;
        }

        public RecordLayoutMeta getLayoutMeta() {
            return layoutMeta;
        }

        public void setLayoutMeta(RecordLayoutMeta layoutMeta) {
            this.layoutMeta = layoutMeta;
        }
    }

    /**
   * Event fired when the "Add New..." link is clicked by the user within
   * {@link CollectionSummaryDetailLayoutMeta}'s rendered UI (widgets).
   *
   * @author POJO Software
   */
    public static class AddNewModelObjectEvent extends CollectionLayoutEvent {

        protected String moduleModelObjectId;

        protected ActionMeta actionMeta;

        protected CollectionSummaryDetailLayoutMeta parentLayoutMeta;

        protected CollectionSummaryDetailLayoutMeta layoutMeta;

        protected TreeTable treeTable;

        protected Renderer renderer;

        /**
     * @param sender the widget that fired the event
     * @param moduleModelObjectId name of the Model Object id defined for the module
     * @param actionMeta metadata defining the Add New action to be taken
     * @param parentLayoutMeta the parent layout metadata of the event's {@link CollectionSummaryDetailLayoutMeta}
     * @param layoutMeta the event's layout metadata
     * @param treeTable the reference to the TreeTable widget
     * @param renderer the renderer of the layoutMeta
     */
        public AddNewModelObjectEvent(Widget sender, String moduleModelObjectId, ActionMeta actionMeta, CollectionSummaryDetailLayoutMeta parentLayoutMeta, CollectionSummaryDetailLayoutMeta layoutMeta, TreeTable treeTable, Renderer renderer) {
            super(sender, ADD_NEW);
            this.moduleModelObjectId = moduleModelObjectId;
            this.actionMeta = actionMeta;
            this.parentLayoutMeta = parentLayoutMeta;
            this.layoutMeta = layoutMeta;
            this.treeTable = treeTable;
            this.renderer = renderer;
        }

        public String getModuleModelObjectId() {
            return moduleModelObjectId;
        }

        public void setModuleModelObjectId(String moduleModelObjectId) {
            this.moduleModelObjectId = moduleModelObjectId;
        }

        public ActionMeta getActionMeta() {
            return actionMeta;
        }

        public void setActionMeta(ActionMeta actionMeta) {
            this.actionMeta = actionMeta;
        }

        public CollectionSummaryDetailLayoutMeta getParentLayoutMeta() {
            return parentLayoutMeta;
        }

        public void setParentLayoutMeta(CollectionSummaryDetailLayoutMeta parentLayoutMeta) {
            this.parentLayoutMeta = parentLayoutMeta;
        }

        public CollectionSummaryDetailLayoutMeta getLayoutMeta() {
            return layoutMeta;
        }

        public void setLayoutMeta(CollectionSummaryDetailLayoutMeta layoutMeta) {
            this.layoutMeta = layoutMeta;
        }

        public TreeTable getTreeTable() {
            return treeTable;
        }

        public void setTreeTable(TreeTable treeTable) {
            this.treeTable = treeTable;
        }

        public Renderer getRenderer() {
            return renderer;
        }

        public void setRenderer(Renderer renderer) {
            this.renderer = renderer;
        }
    }
}
