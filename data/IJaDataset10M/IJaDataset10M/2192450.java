package midnightmarsbrowser.actions;

import midnightmarsbrowser.editors.PanoramaCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

public class PreviousMovieEndpointAction extends BasePanoramaAction {

    public static final String ID = "midnightmarsbrowser.actions.PreviousMovieEndpointAction";

    public PreviousMovieEndpointAction(IWorkbenchWindow window) {
        super(window);
        setId(ID);
        setText("Previous Movie Endpoint");
        setAccelerator(',' + SWT.SHIFT + SWT.CTRL);
    }

    public void run() {
        PanoramaCanvas panoramaCanvas = currentViewer.getPanoramaCanvas();
        if (panoramaCanvas != null) {
            panoramaCanvas.previousMovieEndpoint();
        }
    }
}
