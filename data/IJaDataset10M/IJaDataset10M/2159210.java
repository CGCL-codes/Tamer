package org.apache.myfaces.custom.skin.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.skin.Icon;
import org.apache.myfaces.trinidadinternal.skin.icon.ContextImageIcon;

public class HtmlGraphicImageSkinRenderer extends AdapterSkinRenderer {

    public HtmlGraphicImageSkinRenderer() {
        super("h", "graphicImage");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, RenderingContext arc) throws IOException {
        String baseStyleClass = getBaseStyleName(component);
        String styleClass = baseStyleClass + SkinConstants.STYLE_CLASS_SUFFIX;
        _renderStyleClass(component, context, arc, styleClass, "styleClass");
        if (component instanceof HtmlGraphicImage) {
            _setIconWithHeightAndWidth(context, (HtmlGraphicImage) component, arc);
        } else if (component instanceof UIGraphic) {
            _setIcon(context, (UIGraphic) component, arc);
        }
    }

    /**
     * This method get the value an if is the case replace the component value
     * with the path through icon mechanims of trinidad
     * 
     * @param context
     * @param component
     * @param arc
     * @param getProperty
     * @param setProperty
     */
    private void _setIcon(FacesContext context, UIGraphic component, RenderingContext arc) {
        String oldIcon = null;
        try {
            oldIcon = (String) component.getValue();
        } catch (ClassCastException e) {
        }
        if (oldIcon != null) {
            Icon icon = arc.getIcon(oldIcon);
            if (icon != null) {
                String value = null;
                if (icon instanceof ContextImageIcon) {
                    String baseContextPath = context.getExternalContext().getRequestContextPath() + '/';
                    value = (String) icon.getImageURI(context, arc);
                    if (value.startsWith(baseContextPath)) {
                        value = value.substring(baseContextPath.length() - 1);
                    }
                } else {
                    value = (String) icon.getImageURI(context, arc);
                }
                component.setValue(value);
            }
        }
    }

    /**
     * This method get the value an if is the case replace the component value
     * with the path through icon mechanims of trinidad
     * 
     * @param context
     * @param component
     * @param arc
     * @param getProperty
     * @param setProperty
     */
    private void _setIconWithHeightAndWidth(FacesContext context, HtmlGraphicImage component, RenderingContext arc) {
        String oldIcon = null;
        try {
            oldIcon = (String) component.getValue();
        } catch (ClassCastException e) {
        }
        if (oldIcon != null) {
            Icon icon = arc.getIcon(oldIcon);
            if (icon != null) {
                String value = null;
                if (icon instanceof ContextImageIcon) {
                    String baseContextPath = context.getExternalContext().getRequestContextPath() + '/';
                    value = (String) icon.getImageURI(context, arc);
                    if (value.startsWith(baseContextPath)) {
                        value = value.substring(baseContextPath.length() - 1);
                    }
                } else {
                    value = (String) icon.getImageURI(context, arc);
                }
                component.setValue(value);
                Integer height = icon.getImageHeight(arc);
                if (height != null) {
                    if (component.getHeight() == null) {
                        component.setHeight(height.toString());
                    } else if (component.getHeight() == "") {
                        component.setHeight(height.toString());
                    }
                }
                Integer width = icon.getImageWidth(arc);
                if (width != null) {
                    if (component.getWidth() == null) {
                        component.setWidth("" + width);
                    } else if (component.getWidth() == "") {
                        component.setWidth("" + width);
                    }
                }
            }
        }
    }
}
