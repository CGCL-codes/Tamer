package org.wings.plaf.css;

import org.wings.*;
import org.wings.session.ScriptManager;
import org.wings.header.SessionHeaders;
import org.wings.header.Header;
import org.wings.io.Device;
import java.io.IOException;
import org.wings.plaf.css.script.OnHeadersLoadedScript;

/**
 * CG for SPopup instances.
 *
 * @author Christian Schyma
 */
public final class PopupCG extends FormCG implements org.wings.plaf.PopupCG {

    private static final long serialVersionUID = 1L;

    private Header header;

    private static final SDimension DEFAULT_DIMENSION = new SDimension(400, 300);

    public PopupCG() {
        header = (Utils.createExternalizedJSHeaderFromProperty(Utils.JS_ETC_POPUP));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void installCG(SComponent component) {
        super.installCG(component);
        SessionHeaders.getInstance().registerHeader(header);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        SessionHeaders.getInstance().deregisterHeader(header);
    }

    private String getInitScript(SPopup popup) {
        StringBuilder code = new StringBuilder();
        String anchor = popup.isAnchored() ? popup.getAnchor().getName() : "";
        String corner = popup.isAnchored() ? popup.getCorner() : "";
        String name = "popup_" + popup.getName();
        SDimension dim = popup.getPreferredSize();
        if (dim == null) {
            dim = DEFAULT_DIMENSION;
        }
        String heightUnit = dim.getHeightUnit();
        if (heightUnit != null && !heightUnit.equals("px")) {
            throw new IllegalStateException("Only 'px' is a valid unit, but height was specified as " + dim.getHeight());
        }
        String widthUnit = dim.getWidthUnit();
        if (widthUnit != null && !widthUnit.equals("px")) {
            throw new IllegalStateException("Only 'px' is a valid unit, but width was specified as " + dim.getWidth());
        }
        code.append("if (document.getElementById('" + popup.getName() + "yahoo') != null) { return; }\n");
        code.append(name).append(" = new wingS.Popup(").append("'").append(popup.getName()).append("', ").append(popup.getX() + ", ").append(popup.getY() + ", ").append(dim.getWidthInt()).append(", ").append(dim.getHeightInt()).append(", ").append("'").append(anchor).append("', ").append("'").append(corner).append("'").append(");");
        code.append(name).append(".show();");
        return code.toString();
    }

    @Override
    public void writeInternal(Device device, SComponent component) throws IOException {
        SPopup popup = (SPopup) component;
        device.print("<div id='outer_" + popup.getName() + "'>");
        super.writeInternal(device, popup);
        device.print("</div>");
        ScriptManager.getInstance().addScriptListener(new OnHeadersLoadedScript(getInitScript(popup)));
    }
}
