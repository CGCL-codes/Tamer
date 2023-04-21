package org.apache.myfaces.custom.skin.renderkit.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFRenderer;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

@JSFRenderer(renderKitId = "SkinRenderKit", family = "javax.faces.SelectOne", type = "javax.faces.Menu")
public class HtmlSelectOneMenuSkinRenderer extends HtmlSelectOneOrManySkinRenderer {

    public HtmlSelectOneMenuSkinRenderer() {
        super("h", "selectOneMenu");
    }
}
