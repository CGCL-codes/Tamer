package org.apache.myfaces.custom.skin.renderkit.html.ext;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

public class HtmlCommandLinkSkinRenderer extends AdapterSkinRenderer {

    public HtmlCommandLinkSkinRenderer() {
        super("t", "commandLink");
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
