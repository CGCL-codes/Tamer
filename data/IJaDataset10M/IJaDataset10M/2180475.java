package org.informaticisenzafrontiere.openstaff;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(true);
        layout.addStandaloneView(AnagraficaView.ID, true, IPageLayout.LEFT, 0.25f, editorArea);
        layout.addPerspectiveShortcut("org.informaticisenzafrontiere.openstaff.projectperspectives");
        layout.addPerspectiveShortcut("org.informaticisenzafrontiere.openstaff.wagepacketperspective");
        layout.addPerspectiveShortcut("org.informaticisenzafrontiere.openstaff.accountperspective");
    }
}
