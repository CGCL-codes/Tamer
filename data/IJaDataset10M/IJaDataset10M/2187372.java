package org.apache.myfaces.custom.skin.sandbox;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.RenderingContext;

public class HtmlGraphicImageDynamicSkinRenderer extends AdapterSkinRenderer {

    public HtmlGraphicImageDynamicSkinRenderer() {
        super("s", "graphicImageDynamic");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, RenderingContext arc) throws IOException {
        String baseStyleClass = getBaseStyleName(component);
        String styleClass = baseStyleClass + SkinConstants.STYLE_CLASS_SUFFIX;
        _renderStyleClass(component, context, arc, styleClass, "styleClass");
    }
}
