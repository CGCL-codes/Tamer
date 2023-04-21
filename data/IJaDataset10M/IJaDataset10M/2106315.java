package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.table;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXCollection;
import org.apache.myfaces.trinidad.component.UIXTable;
import org.apache.myfaces.trinidad.component.UIXTree;
import org.apache.myfaces.trinidad.component.core.data.CoreTable;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleSelectBooleanCheckboxRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlConstants;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlRenderer;

public class TableSelectOneRenderer extends XhtmlRenderer {

    public TableSelectOneRenderer(FacesBean.Type type) {
        super(type);
    }

    @Override
    protected void findTypeConstants(FacesBean.Type type) {
        super.findTypeConstants(type);
        _renderer = createCellRenderer(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void decode(FacesContext context, UIComponent component) {
        UIXCollection table = (UIXCollection) component;
        Object oldKey = table.getRowKey();
        try {
            table.setRowKey(null);
            String selectionParam = __getSelectionParameterName(context, table);
            Map<String, String> parameters = context.getExternalContext().getRequestParameterMap();
            _LOG.finest("Params:{0}", parameters);
            String selection = parameters.get(selectionParam);
            if (selection != null) {
                final RowKeySet state;
                if (table instanceof UIXTable) state = ((UIXTable) table).getSelectedRowKeys(); else state = ((UIXTree) table).getSelectedRowKeys();
                table.setClientRowKey(selection);
                if (!state.isContained()) {
                    RowKeySet unselected = state.clone();
                    state.clear();
                    state.add();
                    RowKeySet selected = state.clone();
                    FacesEvent event = new SelectionEvent(table, unselected, selected);
                    event.queue();
                }
            }
        } finally {
            table.setRowKey(oldKey);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
        TableRenderingContext tContext = TableRenderingContext.getCurrentInstance();
        if (tContext == null) {
            _LOG.severe("TABLESELECT_COMPONENT_MAY_ONLY_INSIDE_TABLE_AND_TREETABLE");
            return;
        }
        RenderStage stage = tContext.getRenderStage();
        switch(stage.getStage()) {
            case RenderStage.SUB_CONTROL_BAR_STAGE:
            case RenderStage.UPPER_CONTROL_BAR_STAGE:
            case RenderStage.LOWER_CONTROL_BAR_STAGE:
                break;
            case RenderStage.DATA_STAGE:
                renderCellContent(context, arc, tContext, component, bean);
                break;
            default:
                throw new AssertionError("bad renderStage:" + stage.getStage());
        }
    }

    protected boolean isSelectOne() {
        return true;
    }

    protected CoreRenderer createCellRenderer(FacesBean.Type type) {
        return new Radio(type);
    }

    protected void renderCellContent(FacesContext context, RenderingContext arc, TableRenderingContext tContext, UIComponent component, FacesBean bean) throws IOException {
        arc.setCurrentClientId(tContext.getTableId());
        delegateRenderer(context, arc, component, bean, _renderer);
        arc.setCurrentClientId(null);
    }

    /**
   * Get the name of the parameter for the selection;  package-private
   * for testing.
   */
    static String __getSelectionParameterName(FacesContext context, UIComponent table) {
        return (table.getClientId(context) + NamingContainer.SEPARATOR_CHAR + XhtmlConstants.SELECTED_KEY);
    }

    public static class Radio extends SimpleSelectBooleanCheckboxRenderer {

        public Radio(FacesBean.Type type) {
            super(type);
        }

        @Override
        protected String getCompositeId(String clientId) {
            return null;
        }

        /**
     * we do not want to render the simple span for the checkbox.
     */
        @Override
        protected boolean getRenderSimpleSpan(FacesBean bean) {
            return false;
        }

        /**
     * don't render a special content style class on the radio.
     */
        @Override
        protected String getContentStyleClass(FacesBean bean) {
            return null;
        }

        @Override
        protected void renderId(FacesContext context, UIComponent component) throws IOException {
            TableRenderingContext tContext = TableRenderingContext.getCurrentInstance();
            String param = (tContext.getTableId() + NamingContainer.SEPARATOR_CHAR + XhtmlConstants.SELECTED_KEY);
            ResponseWriter writer = context.getResponseWriter();
            writer.writeAttribute("name", param, null);
            if (getShortDesc(getFacesBean(component)) != null) writer.writeAttribute("id", getClientId(context, component), null);
        }

        @Override
        protected String getClientId(FacesContext context, UIComponent component) {
            return component.getContainerClientId(context);
        }

        @Override
        protected Object getSubmittedValue(FacesBean bean) {
            TableRenderingContext tContext = TableRenderingContext.getCurrentInstance();
            return tContext.getSelectedRowKeys().isContained() ? Boolean.TRUE : Boolean.FALSE;
        }

        @Override
        protected Object getType() {
            return "radio";
        }

        @Override
        protected Object getValueAttr(RenderingContext arc) {
            TableRenderingContext tContext = TableRenderingContext.getCurrentInstance();
            return ((UIXCollection) tContext.getCollectionComponent()).getClientRowKey();
        }

        @Override
        protected String getShortDesc(FacesBean bean) {
            String key = getDefaultShortDescKey();
            RenderingContext arc = RenderingContext.getCurrentInstance();
            return arc.getTranslatedString(key);
        }

        protected String getDefaultShortDescKey() {
            return "af_tableSelectOne.SELECT_COLUMN_HEADER";
        }

        @Override
        protected char getAccessKey(FacesBean bean) {
            return CHAR_UNDEFINED;
        }

        @Override
        protected boolean isImmediate(FacesBean bean) {
            TableRenderingContext tContext = TableRenderingContext.getCurrentInstance();
            return tContext.isImmediate();
        }

        @Override
        protected boolean getReadOnly(FacesContext context, FacesBean bean) {
            return false;
        }

        @Override
        protected boolean getDisabled(FacesBean bean) {
            return false;
        }

        /**
     * @todo Support?
     */
        @Override
        protected String getOnblur(FacesBean bean) {
            return null;
        }

        /**
     * @todo Support?
     */
        @Override
        protected String getOnfocus(FacesBean bean) {
            return null;
        }

        @Override
        protected String getOnchange(FacesBean bean) {
            return null;
        }

        protected String getOnselect(FacesBean bean) {
            return null;
        }

        @Override
        protected String getText(FacesBean bean) {
            return null;
        }
    }

    private CoreRenderer _renderer;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(TableSelectOneRenderer.class);
}
