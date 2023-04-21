package org.apache.myfaces.custom.skin.renderkit.custom;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

public class HtmlCommandSortHeaderSkinRenderer extends AdapterSkinRenderer {

    public HtmlCommandSortHeaderSkinRenderer() {
        super("t", "commandSortHeader");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, SkinRenderingContext arc) throws IOException {
        _addStyleClass(context, component, arc);
    }
}
