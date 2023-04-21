package gumbo.ardor3d.awt;

import gumbo.ardor3d.app.GraphicInput;
import gumbo.ardor3d.app.GraphicManager;
import gumbo.ardor3d.app.GraphicModel;
import gumbo.ardor3d.app.GraphicRenderer;
import gumbo.ardor3d.app.GraphicType;
import gumbo.ardor3d.app.GraphicView;
import gumbo.core.util.AssertUtils;
import java.awt.Component;
import java.awt.Dimension;
import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.awt.AwtFocusWrapper;
import com.ardor3d.input.awt.AwtKeyboardWrapper;
import com.ardor3d.input.awt.AwtMouseManager;
import com.ardor3d.input.awt.AwtMouseWrapper;

/**
 * An abstract GraphicManager that creates GraphicViews compatible with AWT.
 * @author jonb
 */
public abstract class AwtGraphicManager extends GraphicManager {

    /**
	 * Creates an instance.
	 * @param appName Name used in status and error messages. Never empty.
	 * @param type The graphic system type. Never null.
	 */
    public AwtGraphicManager(String appName, GraphicType type) {
        super(appName, type);
    }

    protected abstract Component newNativeCanvas(CanvasRenderer renderer);

    /**
	 * Builds a new GraphicView for this GUI, with the specified input handler.
	 * Before use, the client must add the view's AWT canvas as a child to an
	 * AWT parent widget.
	 * @param model Shared exposed scene model. Never null.
	 * @param input Shared exposed input. Never null.
	 * @return Ceded graphic view. Never null.
	 */
    public GraphicView newGraphicView(GraphicModel model, GraphicInput input) {
        AssertUtils.assertNonNullArg(model);
        AssertUtils.assertNonNullArg(input);
        GraphicRenderer renderer = newGraphicRenderer(model);
        GraphicView view = new GraphicView(renderer, input);
        Component nativeCanvas = newNativeCanvas(renderer.getNativePeer());
        view.setGraphicCanvas(new AwtGraphicCanvas(view, (Canvas) nativeCanvas));
        addGraphicView(view);
        nativeCanvas.setSize(new Dimension(1, 1));
        AwtKeyboardWrapper keyboardWrapper = new AwtKeyboardWrapper(nativeCanvas);
        MouseManager mouseManager = new AwtMouseManager(nativeCanvas);
        AwtMouseWrapper mouseWrapper = new AwtMouseWrapper(nativeCanvas, mouseManager);
        AwtFocusWrapper focusWrapper = new AwtFocusWrapper(nativeCanvas);
        PhysicalLayer physicalLayer = new PhysicalLayer(keyboardWrapper, mouseWrapper, focusWrapper);
        input.getLogicalLayer().registerInput((Canvas) nativeCanvas, physicalLayer);
        return view;
    }
}
