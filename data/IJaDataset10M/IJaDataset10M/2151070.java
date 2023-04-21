package net.coolcoders.showcase.web.vaadin.template;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * @author <a href="mailto:andreas@bambo.it">Andreas Baumgartner, andreas@bambo.it</a>
 *         Date: 24.10.2010
 *         Time: 14:33:51
 */
public class VerticalLayoutCentered extends VerticalLayout {

    private VerticalLayout contentLayout;

    public VerticalLayoutCentered(int width) {
        contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);
        contentLayout.setWidth(width, Sizeable.UNITS_PIXELS);
        this.addComponent(contentLayout);
        this.setComponentAlignment(contentLayout, Alignment.MIDDLE_CENTER);
    }
}
