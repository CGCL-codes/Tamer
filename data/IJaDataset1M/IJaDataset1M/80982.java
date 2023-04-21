package ch.kerbtier.malurus.components.defaultrenderers;

import ch.kerbtier.malurus.components.Button;
import ch.kerbtier.malurus.renderers.JavaComponentRenderer;

public class ButtonRenderer extends JavaComponentRenderer<Button> {

    @Override
    public void render() {
        getWriter().print("<div id=\"" + getSubject().getClientId() + "\" class=\"buiButton\">");
        getWriter().print("<a href=\"javascript:window.bui.load('" + getSubject().getClientId() + "')\">" + getSubject().getLabel() + "</a>");
        getWriter().print("</div>");
    }
}
