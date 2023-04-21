package org.gwt.advanced.client.ui.widget.border;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a simple but very useful border that allows rendering single lines around
 * a custom widget.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public class SingleBorder extends AbstractBorder {

    /** number of shadow lines */
    private static final int SHADOW_COLORS = 3;

    /** div element that contains a custom widget (inside border) */
    private Element container;

    /** layout table */
    private Element layout;

    /**
     * Creates an instance of this class and sets the table element of the panel.
     */
    public SingleBorder() {
        this(true, true, true, true);
    }

    public SingleBorder(boolean top, boolean left, boolean right, boolean bottom) {
        super(DOM.createTable());
        this.layout = getElement();
        this.container = DOM.createTD();
        setStyleName("advanced-SingleBorder");
        linesVisibility.put(BorderLine.TOP, top);
        linesVisibility.put(BorderLine.LEFT, left);
        linesVisibility.put(BorderLine.RIGHT, right);
        linesVisibility.put(BorderLine.BOTTOM, bottom);
        render();
    }

    /** {@inheritDoc} */
    public Element getContentElement() {
        return getContainerElement();
    }

    /**
     * Gets a div element that replaces an original one.
     *
     * @return a div element to be used for containing a widget.
     */
    protected Element getContainerElement() {
        return container;
    }

    /** {@inheritDoc **/
    protected void render() {
        Widget content = super.getWidget();
        if (content != null) remove(content);
        DOM.setInnerText(layout, "");
        Element tBody = DOM.createTBody();
        Element contentTr = DOM.createTR();
        Element shadowTr = DOM.createTR();
        DOM.appendChild(layout, tBody);
        DOM.appendChild(tBody, contentTr);
        DOM.appendChild(contentTr, getContainerElement());
        DOM.setStyleAttribute(layout, "border", "0");
        DOM.setStyleAttribute(layout, "padding", "0");
        DOM.setStyleAttribute(layout, "borderCollapse", "collapse");
        DOM.setElementAttribute(layout, "cellSpacing", "0");
        DOM.setElementAttribute(layout, "cellPadding", "0");
        if (isShadowVisibile()) {
            Element bottomShadowTd = DOM.createTD();
            DOM.appendChild(tBody, shadowTr);
            DOM.appendChild(shadowTr, bottomShadowTd);
            DOM.setStyleAttribute(bottomShadowTd, "border", "0");
            DOM.setStyleAttribute(bottomShadowTd, "padding", "0");
            DOM.setElementAttribute(bottomShadowTd, "height", "3px");
            renderBottomShadow(bottomShadowTd);
        }
        DOM.setElementAttribute(container, "class", "single-container");
        DOM.setElementAttribute(container, "className", "single-container");
        if (!isVisible(BorderLine.TOP)) DOM.setStyleAttribute(getContainerElement(), "borderTopWidth", "0");
        if (!isVisible(BorderLine.BOTTOM)) DOM.setStyleAttribute(getContainerElement(), "borderBottomWidth", "0");
        if (!isVisible(BorderLine.LEFT)) DOM.setStyleAttribute(getContainerElement(), "borderLeftWidth", "0");
        if (!isVisible(BorderLine.RIGHT)) DOM.setStyleAttribute(getContainerElement(), "borderRightWidth", "0");
        if (content != null) super.setWidget(content);
    }

    /**
     * This method renders a bottom shadow.
     *
     * @param container is a shadow container element.
     */
    protected void renderBottomShadow(Element container) {
        if (isVisible(BorderLine.BOTTOM)) {
            for (int i = 0; i < SHADOW_COLORS; i++) {
                Element shadow = DOM.createDiv();
                DOM.setStyleAttribute(shadow, "height", "1px");
                DOM.appendChild(container, shadow);
                DOM.setElementAttribute(shadow, "class", "line shadow" + (i + 1));
                DOM.setElementAttribute(shadow, "className", "line shadow" + (i + 1));
                DOM.appendChild(shadow, DOM.createSpan());
            }
        }
    }
}
