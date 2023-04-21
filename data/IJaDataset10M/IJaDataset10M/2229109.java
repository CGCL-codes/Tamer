package de.cabanis.unific.ui.perspectives;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import de.cabanis.unific.ui.conflicts.ConflictResolvingView;

/**
 * @author Nicolas Cabanis
 */
public class ConflictResolvingPerspective implements IPerspectiveFactory {

    private Logger logger = Logger.getLogger(getClass());

    public static final String ID_PERSPECTIVE = ConflictResolvingPerspective.class.getName();

    /**
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.addView(ConflictResolvingView.ID_VIEW, IPageLayout.TOP, 1.0f, IPageLayout.ID_EDITOR_AREA);
        layout.addPerspectiveShortcut(ID_PERSPECTIVE);
        layout.addShowViewShortcut(ConflictResolvingView.ID_VIEW);
    }
}
