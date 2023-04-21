package com.ingenico.insider.services.impl;

import java.awt.event.InputEvent;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import com.ingenico.insider.util.Constants;
import com.ingenico.piccolo.event.PCurveDragEventHandler;
import com.ingenico.piccolo.event.PXYZoomEventHandler;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolox.swing.PScrollPane;

public final class CanvasSupplier {

    private static final Logger _logger = Logger.getLogger(CanvasSupplier.class.getCanonicalName());

    /**
	 * Public constant keywords
	 */
    public static final String CANVAS_KEY = "canvas";

    public static final String CANVAS_BASE = Constants.INSIDER_BASE + Constants.SEPARATOR + "canvas";

    public static final String RENDER_QUALITY_BASE = CANVAS_BASE + Constants.SEPARATOR + "quality";

    public static final String DEFAULT_PATH = RENDER_QUALITY_BASE + Constants.SEPARATOR + "default";

    public static final String ANIMATING_PATH = RENDER_QUALITY_BASE + Constants.SEPARATOR + "animating";

    public static final String INTERACTING_PATH = RENDER_QUALITY_BASE + Constants.SEPARATOR + "interacting";

    public static final String BGCOLOR_PATH = CANVAS_BASE + Constants.SEPARATOR + Constants.BACKGROUND_KEY;

    /**
	 * singleton instance
	 */
    private static CanvasSupplier instance;

    /**
	 * The Piccolo canvas
	 */
    private final PCanvas canvas;

    /**
	 * A ScrollPane that contains the canvas and allows to manipulate it
	 */
    private final PScrollPane scrollPane;

    private void initCanvas() {
        String resourceString = null;
        resourceString = UserPreferencesSupplier.getInstance().getString(DEFAULT_PATH);
        canvas.setDefaultRenderQuality(CanvasSupplierTools.stringToPPaintContextQuality(resourceString));
        resourceString = UserPreferencesSupplier.getInstance().getString(ANIMATING_PATH);
        canvas.setAnimatingRenderQuality(CanvasSupplierTools.stringToPPaintContextQuality(resourceString));
        resourceString = UserPreferencesSupplier.getInstance().getString(INTERACTING_PATH);
        canvas.setInteractingRenderQuality(CanvasSupplierTools.stringToPPaintContextQuality(resourceString));
        PXYZoomEventHandler ehXYZoom = new PXYZoomEventHandler();
        PInputEventFilter efXYZoom = new PInputEventFilter();
        efXYZoom.setAndMask(InputEvent.BUTTON3_MASK);
        ehXYZoom.setEventFilter(efXYZoom);
        canvas.setZoomEventHandler(ehXYZoom);
        PCurveDragEventHandler ceh = new PCurveDragEventHandler();
        PInputEventFilter efDrag = new PInputEventFilter();
        efDrag.setAndMask(InputEvent.BUTTON1_MASK | InputEvent.CTRL_MASK);
        ceh.setEventFilter(efDrag);
        canvas.addInputEventListener(ceh);
        PInputEventFilter efPan = new PInputEventFilter();
        efPan.setAndMask(InputEvent.BUTTON1_MASK);
        efPan.setNotMask(InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        canvas.getPanEventHandler().setEventFilter(efPan);
        canvas.setBackground(UserPreferencesSupplier.getInstance().getColor(BGCOLOR_PATH));
    }

    private void initScrollPane() {
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    /**
	 * private constructor of final class to prevent external instantiation
	 */
    private CanvasSupplier() {
        canvas = new PCanvas();
        initCanvas();
        scrollPane = new PScrollPane(canvas);
        initScrollPane();
        _logger.fine("Registering canvas properties to the dock framework");
        DockableViewsSupplier.getInstance().add(scrollPane, CANVAS_KEY);
    }

    public PCanvas getCanvas() {
        return canvas;
    }

    public JScrollPane getJComponent() {
        return scrollPane;
    }

    /**
	 * retrieve the singleton instance
	 *
	 * @return the CanvasSupplier singleton
	 */
    public static CanvasSupplier getInstance() {
        if (instance == null) {
            _logger.info(CanvasSupplier.class.getName() + " instance does not exist... Creating instance.");
            instance = new CanvasSupplier();
        }
        return instance;
    }
}
