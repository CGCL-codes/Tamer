package org.sss.presentation.faces.custom.button;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

/**
 * Button的HTML页面处理类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 169 $ $Date: 2008-04-10 00:00:32 -0400 (Thu, 10 Apr 2008) $
 */
public class ButtonRenderer extends Renderer {

    static final Log log = LogFactory.getLog(ButtonRenderer.class);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Button button = (Button) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.BUTTON_ELEM, button);
        writer.writeAttribute(HTML.ID_ATTR, button.getClientId(context), null);
        if (button.getStyle() != null) writer.writeAttribute(HTML.STYLE_ATTR, button.getStyle(), null);
        if (button.getTitle() != null) writer.writeAttribute(HTML.TITLE_ATTR, button.getTitle(), null);
        if (button.getStyleClass() != null) writer.writeAttribute(HTML.CLASS_ATTR, button.getStyleClass(), null);
        writer.writeText(button.getValue(), null);
        writer.endElement(HTML.BUTTON_ELEM);
    }
}
