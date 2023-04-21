package org.apache.myfaces.custom.skin.renderkit.custom;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

public class HtmlCommandNavigation2SkinRenderer extends AdapterSkinRenderer {

    public HtmlCommandNavigation2SkinRenderer() {
        super("t", "commandNavigation2");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, SkinRenderingContext arc) throws IOException {
        String styleClass = null;
        String disabledStyleClass = null;
        String baseStyleClass = this.getBaseStyleName(component);
        styleClass = baseStyleClass + SkinConstants.STYLE_CLASS_SUFFIX;
        disabledStyleClass = baseStyleClass + SkinConstants.DISABLED_CLASS_SUFFIX;
        _renderStyleClass(component, context, arc, styleClass, "styleClass");
        _renderStyleClass(component, context, arc, disabledStyleClass, "disabledStyleClass");
    }
}
