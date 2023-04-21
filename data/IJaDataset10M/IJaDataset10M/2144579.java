package thinwire.render.web;

import thinwire.ui.Component;
import thinwire.ui.DropDown;
import thinwire.ui.event.PropertyChangeEvent;

/**
 * @author Joshua J. Gertzen
 */
final class DropDownRenderer extends MaskEditorComponentRenderer {

    private static final String DROPDOWN_CLASS = "tw_DropDown";

    private static final String SET_EDIT_ALLOWED = "setEditAllowed";

    private static final String SET_COMPONENT = "setComponent";

    private static final int MIN_SIZE = 25;

    private ComponentRenderer ddcr;

    void render(WindowRenderer wr, Component c, ComponentRenderer container) {
        init(DROPDOWN_CLASS, wr, c, container);
        DropDown dd = (DropDown) c;
        addInitProperty(DropDown.PROPERTY_EDIT_ALLOWED, dd.isEditAllowed());
        super.render(wr, c, container);
        Component ddc = dd.getComponent();
        if (ddc.getWidth() == 0 || ddc.getWidth() < dd.getWidth()) ddc.setWidth(dd.getView().getOptimalWidth());
        if (ddc.getHeight() == 0 || ddc.getHeight() < MIN_SIZE) ddc.setHeight(dd.getView().getOptimalHeight());
        ddc.setVisible(false);
        ddcr = wr.ai.getRenderer(ddc);
        ddcr.setPropertyChangeIgnored(Component.PROPERTY_FOCUS, true);
        ddcr.setPropertyChangeIgnored(Component.PROPERTY_ENABLED, true);
        ddcr.setPropertyChangeIgnored(Component.PROPERTY_X, true);
        ddcr.setPropertyChangeIgnored(Component.PROPERTY_Y, true);
        ddcr.render(wr, ddc, this);
        postClientEvent(SET_COMPONENT, wr.ai.getComponentId(ddc));
    }

    void destroy() {
        super.destroy();
        ddcr.destroy();
        ddcr = null;
    }

    public void propertyChange(PropertyChangeEvent pce) {
        String name = pce.getPropertyName();
        if (isPropertyChangeIgnored(name)) return;
        Object newValue = pce.getNewValue();
        if (name.equals(DropDown.PROPERTY_EDIT_ALLOWED)) {
            postClientEvent(SET_EDIT_ALLOWED, newValue);
        } else if (name.equals(DropDown.PROPERTY_COMPONENT)) {
            if (ddcr != null) ddcr.destroy();
            DropDown dd = (DropDown) comp;
            Component ddc = dd.getComponent();
            if (ddc.getWidth() == 0 || ddc.getWidth() < dd.getWidth()) ddc.setWidth(dd.getView().getOptimalWidth());
            if (ddc.getHeight() == 0 || ddc.getHeight() < MIN_SIZE) ddc.setHeight(dd.getView().getOptimalHeight());
            ddc.setVisible(false);
            ddcr = wr.ai.getRenderer(ddc);
            ddcr.setPropertyChangeIgnored(Component.PROPERTY_FOCUS, true);
            ddcr.setPropertyChangeIgnored(Component.PROPERTY_ENABLED, true);
            ddcr.setPropertyChangeIgnored(Component.PROPERTY_X, true);
            ddcr.setPropertyChangeIgnored(Component.PROPERTY_Y, true);
            ddcr.render(wr, ddc, this);
            postClientEvent(SET_COMPONENT, wr.ai.getComponentId(ddc));
        } else {
            super.propertyChange(pce);
        }
    }
}
