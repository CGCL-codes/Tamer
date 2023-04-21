package midnightmarsbrowser.editors;

import midnightmarsbrowser.dialogs.UnknownExceptionDialog;
import midnightmarsbrowser.imageviewer.SWTImageCanvas;
import midnightmarsbrowser.util.PDSIMG;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ImageEditor extends EditorPart {

    public static String ID = "midnightmarsbrowser.editors.ImageEditor";

    private Composite composite;

    private SWTImageCanvas imageCanvas;

    public ImageEditor() {
    }

    public void doSave(IProgressMonitor monitor) {
    }

    public void doSaveAs() {
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
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
            imageCanvas = new SWTImageCanvas(composite);
            PDSIMG img = PDSIMG.readIMGFile(((ImageEditorInput) getEditorInput()).getPathname(), 0, 2048);
            byte[] imageBytes = img.getImageByteArray();
            PaletteData palette = new PaletteData(0xFF, 0xFF, 0xFF);
            ImageData imageData = new ImageData(img.getLineSamples(), img.getLines(), 8, palette, 1, imageBytes);
            imageCanvas.setImageData(imageData);
        } catch (Throwable e) {
            UnknownExceptionDialog.openDialog(this.getSite().getShell(), e.toString(), e);
            e.printStackTrace();
        }
    }

    public void setFocus() {
        if (imageCanvas != null) {
            imageCanvas.setFocus();
        }
    }

    public void dispose() {
        if (imageCanvas != null) {
            imageCanvas.dispose();
        }
        super.dispose();
    }
}
