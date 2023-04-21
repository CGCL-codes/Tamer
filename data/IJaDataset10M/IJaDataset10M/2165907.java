package net.sourceforge.javautil.developer.web.library.jsf.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.component.UIColumn;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import net.sourceforge.javautil.developer.web.library.jsf.JSFUtil;

/**
 * This will allow easier definition and setup for {@link DataModelSwitcher}'s.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class UIDataModelSwitcher extends UIOutput {

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (!this.isRendered()) return;
        UIData data = JSFUtil.find(getChildren(), UIData.class);
        if (data != null) {
            DataModel model = (DataModel) data.getValueExpression("value").getValue(context.getELContext());
            if (model instanceof DataModelSwitcher) {
                DataModelSwitcher switcher = (DataModelSwitcher) model;
                String current = switcher.getCurrentModel();
                List<UIColumn> columns = null;
                columns = JSFUtil.findAll(data.getChildren(), UIColumn.class);
                int columnCount = 0;
                for (int u = 0; u < columns.size(); u++) {
                    UIColumn column = columns.get(u);
                    if (!column.isRendered()) continue;
                    Object viewSetting = JSFUtil.get(context, column, "views", null);
                    List<String> views = null;
                    if (viewSetting instanceof String) {
                        views = new ArrayList<String>(Arrays.asList(((String) viewSetting).split(",")));
                    } else if (viewSetting instanceof List) {
                        views = (List<String>) viewSetting;
                    }
                    if (views != null && !views.contains(current)) {
                        data.getChildren().remove(column);
                    } else columnCount++;
                }
                context.getExternalContext().getRequestMap().put("switchColumnCount", columnCount);
            }
        }
        context.getResponseWriter().startElement("span", this);
        context.getResponseWriter().writeAttribute("id", this.getClientId(context), "id");
        if (data != null && data.isRendered()) data.encodeAll(context);
        context.getResponseWriter().endElement("span");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
