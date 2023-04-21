package midnightmarsbrowser.editors;

import midnightmarsbrowser.application.Application;
import midnightmarsbrowser.application.UpdateViewerListener;
import midnightmarsbrowser.imageviewer.SWTImageCanvas;
import midnightmarsbrowser.model.ImageEntry;
import midnightmarsbrowser.model.TimeIntervalList;
import midnightmarsbrowser.model.TimeInterval;
import midnightmarsbrowser.model.ViewerSettings;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class UpdateViewerEditor extends MMBEditorBase implements UpdateViewerListener {

    public static String ID = "midnightmarsbrowser.editors.UpdateViewerEditor";

    private static UpdateViewerEditor instance = null;

    private Composite composite;

    private ImageCanvas imageCanvas;

    public static void openUpdateViewer(IWorkbenchWindow window) {
        if (instance == null) {
            IWorkbenchPage page = window.getActivePage();
            MMBEditorInput input = new MMBEditorInput(null);
            try {
                page.openEditor(input, UpdateViewerEditor.ID);
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeUpdateViewer() {
        if (instance != null) {
            instance.getEditorSite().getPage().closeEditor(instance, false);
        }
    }

    public UpdateViewerEditor() {
    }

    public void doSave(IProgressMonitor monitor) {
    }

    public void doSaveAs() {
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        if (input instanceof MMBEditorInput) {
            Application.getUpdateViewerService().addListener(this);
        } else {
            throw new PartInitException("input was not MMBEditorInput");
        }
        instance = this;
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isSaveAsAllowed() {
        return false;
    }

    public void createPartControl(Composite parent) {
        try {
            composite = new Canvas(parent, SWT.NONE);
            composite.setLayout(new FillLayout());
            imageCanvas = new ImageCanvas(composite);
            setPartName("Update Viewer");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setFocus() {
        imageCanvas.setFocus();
    }

    public void dispose() {
        if (instance == this) {
            instance = null;
        }
        if (imageCanvas != null) {
            imageCanvas.dispose();
        }
        Application.getUpdateViewerService().removeListener(this);
        super.dispose();
    }

    public ViewerSettings getViewerSettings() {
        return null;
    }

    public void setViewerSettings(ViewerSettings settings, boolean recomputeImageList) {
    }

    public TimeIntervalList getTimeIntervalList() {
        return null;
    }

    public ImageEntry getSelectedImage() {
        return null;
    }

    public void setSelectedImage(ImageEntry entry, boolean updateViewer) {
    }

    public void setSelectedImage(ImageEntry entry) {
    }

    public void setSelectedTimeInterval(TimeInterval entry) {
    }

    public TimeInterval getSelectedTimeInterval() {
        return null;
    }

    public void newUpdateImage() {
        ImageData imageData = Application.getUpdateViewerService().getImageData();
        imageCanvas.setImageData(imageData);
    }

    public void setImageEnabled(ImageEntry imageListEntry, boolean enabled) {
    }

    public void reloadImage(ImageEntry imageListEntry) {
    }
}
