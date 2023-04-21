package org.apache.myfaces.custom.skin.sandbox;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.trinidad.context.RenderingContext;

public class HtmlFilterTableSkinRenderer extends AdapterSkinRenderer {

    public HtmlFilterTableSkinRenderer() {
        super("s", "filterTable");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, RenderingContext arc) throws IOException {
        _addStyleClass(context, component, arc);
    }
}
