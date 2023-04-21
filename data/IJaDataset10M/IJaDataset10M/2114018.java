package org.larozanam.arq.components.abstratos;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;

/**
 * creates a window based on jvascript <a href="http://prototype-window.xilinus.com/">window</a> library.
 *
 * @version $Id: Window.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class WindowPane extends AbstractWindowPane {

    @Environmental
    private RenderSupport renderSupport;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String url;

    private boolean hasBody = false;

    /**
     * Tapestry render phase method.
     * Called before component body is rendered.
     *
     * @param writer the markup writer
     */
    @BeforeRenderBody
    void beforeRenderBody(MarkupWriter writer) {
        writer.element("div", "id", getClientId() + "Content", "style", "display:none;");
    }

    /**
     * Tapestry render phase method.
     * Called after component body is rendered.
     * return false to render body again.
     *
     * @param writer the markup writer
     */
    @AfterRenderBody
    void afterRenderBody(MarkupWriter writer) {
        writer.end();
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    @AfterRender
    void afterRender(MarkupWriter writer) {
        JSONObject options = new JSONObject();
        options.put("className", getClassName());
        options.put("width", getWidth());
        options.put("height", getHeight());
        options.put("id", getClientId());
        options.put("title", getTitle());
        if (this.url != null && this.url.length() > 0) {
            options.put("url", url);
        } else {
            hasBody = true;
        }
        configure(options);
        renderSupport.addScript("%s = new Window(%s);", getClientId(), options);
        if (hasBody) renderSupport.addScript("%s.setContent('%sContent');", getClientId(), getClientId());
        if (isShow()) renderSupport.addScript("%s.show%s(%s);", getClientId(), isCenter() ? "Center" : "", isModal());
    }
}
