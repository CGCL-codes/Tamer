package de.sonivis.tool.view.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import ch.qos.logback.eclipse.views.LogbackView;
import de.sonivis.tool.view.textmining.TermClusteringView;
import de.sonivis.tool.view.textmining.TermTableView;
import de.sonivis.tool.view.views.VisualizationView;

/**
 * This class provides the text mining perspective
 * 
 * @author Janette
 * @version $Revision
 */
public class TextMiningPerspective implements IPerspectiveFactory {

    public static final String PERSPECTIVE_ID = "de.sonivis.tool.view.perspectives.TextMiningPerspective";

    @Override
    public final void createInitialLayout(final IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        final String editorArea = layout.getEditorArea();
        layout.addView(VisualizationView.ID_VIEW, IPageLayout.RIGHT, 0.45f, editorArea);
        final IFolderLayout controlFolder = layout.createFolder("controlFolder", IPageLayout.RIGHT, 0.55f, VisualizationView.ID_VIEW);
        controlFolder.addView(TermTableView.ID_VIEW);
        controlFolder.addPlaceholder(LogbackView.ID);
        controlFolder.addView(TermClusteringView.ID_VIEW);
    }
}
