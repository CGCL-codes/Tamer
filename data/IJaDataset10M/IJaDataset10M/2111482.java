package org.apache.myfaces.custom.skin.sandbox;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.column.HtmlColumn;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.RenderingContext;

public class HtmlAutoUpdateDataTableSkinRenderer extends AdapterSkinRenderer {

    public HtmlAutoUpdateDataTableSkinRenderer() {
        super("s", "autoUpdateDataTable");
    }

    /**
     * Apply the following css class style attributes:
     * 
     * bodyStyleClass footerstyleClass headerstyleClass rowGroupStyleClass
     * rowStyleClass styleClass
     * 
     * @param context
     * @param component
     * @param arc
     * @throws IOException
     */
    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, RenderingContext arc) throws IOException {
        String bodyStyleClass = null;
        String footerStyleClass = null;
        String headerStyleClass = null;
        String rowStyleClass = null;
        String rowClasses = null;
        String styleClass = null;
        String baseStyleClass = this.getBaseStyleName(component);
        bodyStyleClass = baseStyleClass + "::body";
        footerStyleClass = baseStyleClass + "::footer";
        headerStyleClass = baseStyleClass + "::header";
        rowStyleClass = baseStyleClass + "::rowStyle";
        rowClasses = baseStyleClass + "::row";
        styleClass = baseStyleClass + SkinConstants.STYLE_CLASS_SUFFIX;
        _renderStyleClass(component, context, arc, bodyStyleClass, "bodyStyleClass");
        _renderStyleClass(component, context, arc, footerStyleClass, "footerClass");
        _renderStyleClass(component, context, arc, headerStyleClass, "headerClass");
        _renderStyleClass(component, context, arc, rowStyleClass, "rowStyleClass");
        _renderStyleClass(component, context, arc, styleClass, "styleClass");
        Map m = component.getAttributes();
        String oldRowClasses = (String) m.get("rowClasses");
        List<String> list = parseStyleClassListComma(oldRowClasses);
        if (list == null) {
            _renderStyleClass(component, context, arc, rowClasses, "rowClasses");
        } else {
            String def = arc.getStyleClass(rowClasses);
            if (def.startsWith("s_")) {
            } else {
                String[] l1 = new String[list.size()];
                int length = 0;
                for (int i = 0; i < l1.length; i++) {
                    if (!list.get(i).contains(def)) {
                        l1[i] = list.get(i) + " " + def;
                        length += l1[i].length() + 1;
                    } else {
                        l1[i] = list.get(i);
                        length += l1[i].length() + 1;
                    }
                }
                StringBuilder builder = new StringBuilder(length);
                for (int i = 0; i < l1.length; i++) {
                    if (l1[i] != null) {
                        if (builder.length() != 0) builder.append(',');
                        builder.append(l1[i]);
                    }
                }
                component.getAttributes().put("rowClasses", builder.toString());
            }
        }
        for (Iterator iter = component.getChildren().iterator(); iter.hasNext(); ) {
            UIComponent child = (UIComponent) iter.next();
            if (HtmlColumn.class.isAssignableFrom(child.getClass())) {
                HtmlColumn col = (HtmlColumn) child;
                if (child instanceof org.apache.myfaces.custom.crosstable.HtmlColumns) {
                    baseStyleClass = "t|columns";
                } else {
                    baseStyleClass = "t|column";
                }
                styleClass = baseStyleClass + SkinConstants.STYLE_CLASS_SUFFIX;
                String footerstyleClass = baseStyleClass + "::footer";
                String headerstyleClass = baseStyleClass + "::header";
                _renderStyleClass(child, context, arc, styleClass, "styleClass");
                _renderStyleClass(child, context, arc, footerstyleClass, "footerstyleClass");
                _renderStyleClass(child, context, arc, headerstyleClass, "headerstyleClass");
            }
        }
    }
}
