package net.sf.pdfizer;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.internal.progress.ProgressContentProvider;
import org.eclipse.ui.progress.IProgressConstants;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addNewWizardShortcut("net.sf.pdfizer.wizards.NewEntryWizard");
        layout.addNewWizardShortcut("net.sf.pdfizer.wizards.PdfDocumentWizard");
        layout.addNewWizardShortcut("net.sf.pdfizer.wizards.CreateNewMediaWizard");
        IFolderLayout catalogFolder = layout.createFolder("CatalogFolder", IPageLayout.LEFT, 0.3f, editorArea);
        catalogFolder.addView(CatalogTreeView.ID);
        catalogFolder.addView(AuthorView.ID);
        layout.getViewLayout(CatalogTreeView.ID).setCloseable(false);
        layout.getViewLayout(CatalogTreeView.ID).setMoveable(false);
        IFolderLayout propertyFolder = layout.createFolder("PropertyFolder", IPageLayout.BOTTOM, 0.7f, "CatalogFolder");
        propertyFolder.addView(IPageLayout.ID_PROP_SHEET);
        IFolderLayout searchViewsFolderLayout = layout.createFolder("searchView", IPageLayout.RIGHT, 0.7f, editorArea);
        searchViewsFolderLayout.addPlaceholder(TitleView.ID + ":*");
        searchViewsFolderLayout.addPlaceholder(SubjectOccurencesView.ID);
        layout.getViewLayout(SubjectOccurencesView.ID).setCloseable(true);
        IPlaceholderFolderLayout bottomFolder = layout.createPlaceholderFolder("property_bottom", IPageLayout.BOTTOM, 0.7f, "searchView");
        bottomFolder.addPlaceholder(DocumentQueueView.ID);
        layout.getViewLayout(DocumentQueueView.ID).setCloseable(false);
        layout.getViewLayout(DocumentQueueView.ID).setMoveable(false);
    }
}
