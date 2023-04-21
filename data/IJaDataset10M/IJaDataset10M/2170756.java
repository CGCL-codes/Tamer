package de.beas.explicanto.client.rcp.pageeditor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import de.bea.services.vidya.client.datastructures.CAttentionComponent;
import de.beas.explicanto.client.rcp.components.html.HtmlEditor;

public class PageComponentAttention extends PageComponentText {

    public PageComponentAttention(Composite parent, PageRegion region, CAttentionComponent component) {
        super(parent, region, component);
    }

    protected CAttentionComponent getAttentionComponent() {
        return (CAttentionComponent) getComponent();
    }

    protected Color getActiveBorderColor() {
        return ColorConstants.red;
    }

    protected Color getInactiveBorderColor() {
        return ColorConstants.orange;
    }

    protected int getComonentType() {
        return HtmlEditor.COMPONENT_TYPE_ATTENTION;
    }
}
