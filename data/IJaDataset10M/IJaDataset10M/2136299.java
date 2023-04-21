package cruise.umple.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class UmplePerspective implements IPerspectiveFactory {

    private IPageLayout factory;

    public UmplePerspective() {
        super();
    }

    public void createInitialLayout(IPageLayout factory) {
        this.factory = factory;
        addViews();
        addPerspectiveShortcuts();
    }

    private void addViews() {
        IFolderLayout bottom = factory.createFolder("bottomRight", IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
        bottom.addView("org.eclipse.ui.console.ConsoleView");
        bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
        IFolderLayout topLeft = factory.createFolder("topLeft", IPageLayout.LEFT, 0.30f, factory.getEditorArea());
        topLeft.addView("org.eclipse.ui.navigator.ProjectExplorer");
        topLeft.addView("org.eclipse.jdt.junit.ResultView");
        IFolderLayout topRight = factory.createFolder("topRight", IPageLayout.RIGHT, 0.50f, factory.getEditorArea());
        topRight.addView(IPageLayout.ID_OUTLINE);
    }

    private void addPerspectiveShortcuts() {
        factory.addPerspectiveShortcut("org.eclipse.jdt.junit.ResultView");
    }
}
