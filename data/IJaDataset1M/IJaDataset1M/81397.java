package com.iver.cit.gvsig.project.documents.layout.contextmenu.gui;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.LayoutContext;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;

/**
 * Refresca todos los fframes del Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class RefreshLayoutMenuEntry extends AbstractLayoutContextMenuAction {

    public String getGroup() {
        return "layout";
    }

    public int getGroupOrder() {
        return 6;
    }

    public int getOrder() {
        return 3;
    }

    public String getText() {
        return PluginServices.getText(this, "refrescar");
    }

    public boolean isEnabled(LayoutContext layoutContext, IFFrame[] selectedFrames) {
        return true;
    }

    public boolean isVisible(LayoutContext layoutContext, IFFrame[] selectedFrames) {
        if (!(getLayout().getLayoutControl().getGeometryAdapter().getPoints().length > 0)) return true;
        return false;
    }

    public void execute(LayoutContext layoutContext, IFFrame[] selectedFrames) {
        layoutContext.fullRefresh();
    }
}
