package mhhc.htmlcomponents.component;

import mhhc.htmlcomponents.ComponentContext;
import mhhc.htmlcomponents.ComponentVisitor;

/**
 * Renders a simple <div/> HTML element
 * This element can be used to decorate another element by providing an enclosing div element around the content.
 */
public abstract class Div extends AbstractComponent {

    protected Component content;

    public Div(Component content) {
        this.content = content;
    }

    public String renderHTML(ComponentContext ctx) {
        StringBuffer html = new StringBuffer("<div id='" + getId(ctx) + "'>");
        html.append((content != null ? content.renderHTML(ctx) : ""));
        html.append("</div>");
        return html.toString();
    }

    public void accept(ComponentVisitor visitor, ComponentContext ctx) {
        visitor.visit(this, ctx);
        content.accept(visitor, ctx);
    }
}
