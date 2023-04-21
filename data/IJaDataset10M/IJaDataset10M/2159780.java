package org.apache.myfaces.custom.skin.renderkit.custom;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

public class HtmlInputTextHelpSkinRenderer extends AdapterSkinRenderer {

    public HtmlInputTextHelpSkinRenderer() {
        super("t", "inputTextHelp");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, SkinRenderingContext arc) throws IOException {
        _addStyleDisabledReadOnlyRequired(context, component, arc);
        String baseStyleClass = this.getBaseStyleName(component);
        String displayValueOnlyStyleClass = baseStyleClass + "::displayValueOnly";
        _renderStyleClass(component, context, arc, displayValueOnlyStyleClass, "displayValueOnlyStyleClass");
    }
}
