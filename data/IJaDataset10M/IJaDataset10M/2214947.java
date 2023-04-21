package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.html.HtmlHead;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;

/**
 * Renderer for meta data section of the document--a.k.a <head>.
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 */
public class HeadRenderer extends XhtmlRenderer {

    public HeadRenderer() {
        this(HtmlHead.TYPE);
    }

    protected HeadRenderer(FacesBean.Type type) {
        super(type);
    }

    @Override
    protected void findTypeConstants(FacesBean.Type type) {
        super.findTypeConstants(type);
        _titleKey = type.findKey("title");
    }

    @Override
    protected void encodeBegin(FacesContext context, RenderingContext arc, UIComponent comp, FacesBean bean) throws IOException {
        ResponseWriter rw = context.getResponseWriter();
        rw.startElement("head", comp);
        renderId(context, comp);
        String title = getTitle(bean);
        if (title != null) {
            rw.startElement("title", null);
            rw.writeText(title, null);
            rw.endElement("title");
        }
        _writeGeneratorTag(context);
        delegateRenderer(context, arc, comp, bean, _styleSheetRenderer);
    }

    @Override
    protected void encodeEnd(FacesContext context, RenderingContext arc, UIComponent comp, FacesBean bean) throws IOException {
        ResponseWriter rw = context.getResponseWriter();
        rw.endElement("head");
    }

    protected String getTitle(FacesBean bean) {
        return toString(bean.getProperty(_titleKey));
    }

    /**
   * Writes the META generator tag that identifies the technology
   * generating the page.
   */
    private static void _writeGeneratorTag(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("meta", null);
        writer.writeAttribute("name", "generator", null);
        writer.writeAttribute("content", "Apache MyFaces Trinidad", null);
        writer.endElement("meta");
    }

    private CoreRenderer _styleSheetRenderer = new StyleSheetRenderer() {

        @Override
        protected void renderId(FacesContext context, UIComponent component) {
        }
    };

    private PropertyKey _titleKey;
}
