package net.sourceforge.ba_fo_ma.internal;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * 
 */
public class InterfacePerspective implements IPerspectiveFactory {

    /**
     * 
     */
    public InterfacePerspective() {
    }

    /**
     * 
     */
    public void createInitialLayout(IPageLayout pageLayout) {
        pageLayout.setEditorAreaVisible(true);
    }
}
