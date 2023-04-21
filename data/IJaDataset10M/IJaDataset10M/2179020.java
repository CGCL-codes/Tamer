package research.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import research.ui.views.FilmTypeView;
import research.ui.views.TestTypeView;

public class Types implements IPerspectiveFactory {

    public static final String ID = "research.ui.perspectives.Types";

    public void createInitialLayout(IPageLayout layout) {
        layout.setFixed(true);
        IFolderLayout fold = layout.createFolder("types", IPageLayout.LEFT, .3f, IPageLayout.ID_EDITOR_AREA);
        fold.addView(TestTypeView.ID);
        fold.addView(FilmTypeView.ID);
        InitialLayoutHelper.addShortcuts(layout);
    }
}
