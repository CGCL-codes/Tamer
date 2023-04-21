package es.iiia.sgi;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import es.iiia.sgi.views.DebugTree;
import es.iiia.sgi.views.RenderLineView;
import es.iiia.sgi.views.RuleView;
import es.iiia.sgi.views.ShapeView;
import es.iiia.sgi.views.SubshapeView;

public class Perspective implements IPerspectiveFactory {

    private IPageLayout layout;

    public static final String ID = "es.iiia.sgi.perspective";

    public static final String FOLDER_LEFT = ID + ".leftFolder";

    public static final String FOLDER_TOP = ID + ".topFolder";

    public static final String FOLDER_BOTTOM = ID + ".bottomFolder";

    public void createInitialLayout(IPageLayout layout) {
        this.layout = layout;
        String editorAreaId = layout.getEditorArea();
        this.layout.setEditorAreaVisible(true);
        this.layout.setFixed(false);
        this.layout.createPlaceholderFolder(FOLDER_LEFT, IPageLayout.LEFT, 0.25f, editorAreaId);
        this.layout.createPlaceholderFolder(FOLDER_TOP, IPageLayout.TOP, 0.6f, editorAreaId);
        this.layout.createPlaceholderFolder(FOLDER_BOTTOM, IPageLayout.BOTTOM, 0.10f, editorAreaId);
        IFolderLayout parts = layout.createFolder("bigParts", IPageLayout.LEFT, 0.3f, FOLDER_LEFT);
        parts.addView(ShapeView.ID);
        parts.addView(RuleView.ID);
        IFolderLayout tabs = layout.createFolder("bottomParts", IPageLayout.LEFT, 0.3f, FOLDER_BOTTOM);
        tabs.addView(DebugTree.ID);
        tabs.addView(SubshapeView.ID);
        tabs.addView(RenderLineView.ID);
        tabs.addPlaceholder(IPageLayout.ID_PROP_SHEET);
        String editorArea = layout.getEditorArea();
        layout.addStandaloneView(IPageLayout.ID_OUTLINE, true, IPageLayout.LEFT, 0.3f, editorArea);
    }

    public IPageLayout getLayout() {
        return layout;
    }
}
