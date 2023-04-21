package com.ingenico.insider.services.impl;

import java.awt.event.InputEvent;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import com.ingenico.insider.nodes.PMark;
import com.ingenico.insider.util.Constants;
import com.ingenico.piccolo.event.PSelectionEventHandler;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PInputEventFilter;

public final class SelectionLayerSupplier {

    private static final Logger _logger = Logger.getLogger(SelectionLayerSupplier.class.getCanonicalName());

    /**
	 * singleton instance
	 */
    private static SelectionLayerSupplier instance;

    /**
	 * Public constant keywords
	 */
    public static final String SELECTION_KEY = "selection";

    public static final String SELECTION_BASE = Constants.INSIDER_BASE + Constants.SEPARATOR + SELECTION_KEY;

    public static final String SELECTION_BACKGROUND_PATH = SELECTION_BASE + Constants.SEPARATOR + Constants.BACKGROUND_KEY;

    public static final String MARK_KEY = "mark";

    public static final String MARK_BASE = Constants.INSIDER_BASE + Constants.SEPARATOR + MARK_KEY;

    public static final String SELECTION_MARK_PATH = MARK_BASE + Constants.SEPARATOR + Constants.BACKGROUND_KEY;

    public static final String FLOATING_SELECTION_KEY = "floating_selection";

    /**
	 * The dedicated selection layer under the canvas
	 */
    private final PLayer selectionLayer;

    /**
	 * The PMark that is currently getting created
	 */
    protected final PMark rectangle;

    /**
	 * The data model that maintains the marks list
	 */
    private final DefaultListModel marksModel;

    private final PSelectionEventHandler selectionEventHandler;

    /**
	 * private constructor of final class to prevent external instantiation
	 */
    private SelectionLayerSupplier() {
        final PCanvas canvas = CanvasSupplier.getInstance().getCanvas();
        marksModel = new DefaultListModel();
        selectionLayer = new PLayer();
        canvas.getCamera().addLayer(0, selectionLayer);
        rectangle = new PMark(LocalisationSupplier.getInstance().localize(FLOATING_SELECTION_KEY));
        selectionLayer.addChild(rectangle);
        selectionEventHandler = new PSelectionEventHandler(rectangle, selectionLayer);
        rectangle.setPaint(UserPreferencesSupplier.getInstance().getColor(SELECTION_BACKGROUND_PATH));
        PInputEventFilter efDrag = new PInputEventFilter();
        efDrag.setAndMask(InputEvent.BUTTON1_MASK | InputEvent.SHIFT_MASK);
        selectionEventHandler.setEventFilter(efDrag);
        canvas.addInputEventListener(selectionEventHandler);
    }

    /**
	 * retrieve the singleton instance
	 *
	 * @return the SelectionLayerSupplier singleton
	 */
    public static SelectionLayerSupplier getInstance() {
        if (instance == null) {
            _logger.info(SelectionLayerSupplier.class.getName() + " instance does not exist... Creating instance.");
            instance = new SelectionLayerSupplier();
        }
        return instance;
    }

    public void clearSelection() {
        selectionEventHandler.clearSelection();
    }

    public DefaultListModel getModel() {
        return marksModel;
    }

    public PMark getSelectionMark() {
        return rectangle;
    }

    public void addMark(PMark mark) {
        selectionLayer.addChild(mark);
        marksModel.addElement(mark);
    }

    public void removeMark(PMark mark) {
        selectionLayer.removeChild(mark);
        marksModel.removeElement(mark);
    }
}
