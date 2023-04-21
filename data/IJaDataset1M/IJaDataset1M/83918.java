package net.sf.breed.orbiter.eclipse;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Perspective containing only the Orbiter view.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 18 Feb 2009
 */
public class OrbiterPerspective implements IPerspectiveFactory {

    /**
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addStandaloneView(OrbiterView.ID, false, IPageLayout.LEFT, 1.0f, editorArea);
    }
}
