package GeneratorCode;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import com.west.view.GenerateView;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.addStandaloneView(GenerateView.ID, false, IPageLayout.RIGHT, 1.0f, editorArea);
        layout.getViewLayout(GenerateView.ID).setCloseable(false);
    }
}
