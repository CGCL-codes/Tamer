package gestalt.candidates;

import java.util.Vector;
import gestalt.context.GLContext;
import gestalt.render.bin.AbstractBin;
import gestalt.render.plugin.Camera;
import gestalt.shape.AbstractDrawable;

/**
 * the windowmanager renders bins into the same view.
 */
public class WindowManager extends AbstractDrawable {

    private Vector<Camera> _myCamera;

    private Vector<AbstractBin> _myBin;

    public WindowManager() {
        _myCamera = new Vector<Camera>();
        _myBin = new Vector<AbstractBin>();
    }

    public void add(Camera theCamera, AbstractBin theBin) {
        if (theCamera != null && theBin != null) {
            _myCamera.add(theCamera);
            _myBin.add(theBin);
        }
    }

    public Camera camera(int theID) {
        return _myCamera.get(theID);
    }

    public AbstractBin bin(int theID) {
        return _myBin.get(theID);
    }

    public int size() {
        if (_myCamera.size() != _myBin.size()) {
            System.err.println("### WARNING @ WindowManager / data pssobly corrupted.");
        }
        return _myCamera.size();
    }

    public void draw(GLContext theRenderContext) {
        for (int i = 0; i < _myCamera.size(); i++) {
            final Camera myCamera = _myCamera.get(i);
            final AbstractBin myBin = _myBin.get(i);
            myCamera.draw(theRenderContext);
            myBin.draw(theRenderContext);
        }
    }
}
