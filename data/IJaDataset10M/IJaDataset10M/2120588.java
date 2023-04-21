package com.gr.staffpm.widget.jqgrid.component.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequestCycle;
import com.gr.staffpm.widget.jqgrid.component.Grid;

/**
 * @author Graham Rhodes
 *
 */
public abstract class OnSelectRowAjaxEvent<B extends Serializable> extends AbstractAjaxGridAwareEvent<B> {

    private static final long serialVersionUID = 1L;

    /**
     * @param event
     */
    public OnSelectRowAjaxEvent() {
        super(GridEvent.onSelectRow);
    }

    @Override
    public final void onEvent(AjaxRequestTarget target) {
        String param = WebRequestCycle.get().getRequest().getParameter("row");
        if (!StringUtils.isEmpty(param) && param.length() > 3) {
            Integer row = Integer.parseInt(param.substring(3));
            Grid<B> grid = getGrid();
            String status = WebRequestCycle.get().getRequest().getParameter("status");
            IModel<B> model = grid.getDataPanel().getRowModels().get(row);
            onSelectRow(target, row, model, Boolean.parseBoolean(status));
        }
    }

    @Override
    public String statement(String callBackURL) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionSignature());
        sb.append("if(id){\n");
        sb.append("var url = '");
        sb.append(callBackURL);
        sb.append("&gridEvent=" + getGridEvent());
        Map<String, String> params = new HashMap<String, String>();
        encodeAdditionalParams(params);
        Iterator<String> keys = params.keySet().iterator();
        sb.append("';\n");
        while (keys.hasNext()) {
            sb.append("url += ");
            String key = keys.next();
            String value = params.get(key);
            sb.append("'&" + key + "=' + " + value + ";\n");
        }
        sb.append(";");
        sb.append("wicketAjaxGet(url)");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    protected String getFunctionSignature() {
        return "function(id, status){\n";
    }

    @Override
    protected void encodeAdditionalParams(Map<String, String> params) {
        params.put("row", "id");
        params.put("status", "status");
    }

    /**
     * 
     * Override this method to do something when a row is selected.
     * 
     * @param target The {@link AjaxRequestTarget}.
     * @param row The row (starts at 0).
     * @param rowModel The associated row model.
     */
    protected abstract void onSelectRow(AjaxRequestTarget target, int row, IModel<B> rowModel, boolean status);
}
