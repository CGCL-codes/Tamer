package org.jogre.pointsTotal.client;

import java.awt.event.MouseEvent;
import org.jogre.pointsTotal.common.PointsTotalModel;
import org.jogre.client.JogreController;

/**
 * Controller for the selection component of the Points Total game.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
public class PointsTotalSelectionController extends JogreController {

    protected PointsTotalModel model;

    protected PointsTotalSelectionComponent selectionComponent;

    protected PointsTotalComponent component;

    /**
     * Default constructor for the pointsTotal controller which takes a
     * model and a view.
     *
     * @param model               PointsTotal model class.
     * @param selectionComponent  PointsTotal view class.
     * @param component           The board view component.
     * @param conn
     */
    public PointsTotalSelectionController(PointsTotalModel model, PointsTotalSelectionComponent selectionComponent, PointsTotalComponent component) {
        super(model, selectionComponent);
        this.model = model;
        this.selectionComponent = selectionComponent;
        this.component = component;
    }

    /**
     * Start method needed, as this is a JogreController, but this start()
     * routine does nothing, as the one in PointsTotalController does the
     * real start.
     */
    public void start() {
    }

    /**
	 * Handle mouse movement events
	 *
	 * @param  mEv        The mouse event
	 */
    public void mouseMoved(MouseEvent mEv) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            int space = selectionComponent.decodeSpace(mEv.getX(), mEv.getY());
            if ((space < 0) || model.isAvailToPlay(getSeatNum(), space)) {
                if (selectionComponent.setMouseSpace(space)) {
                    selectionComponent.repaint();
                }
            }
        }
    }

    /**
     * Implementation of the mouse pressed interface.
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent mEv) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            int space = selectionComponent.decodeSpace(mEv.getX(), mEv.getY());
            if ((space >= 0) && model.isAvailToPlay(getSeatNum(), space)) {
                if (selectionComponent.setSelectedSpace(space)) {
                    component.setCurrentPieceValue(space);
                    selectionComponent.repaint();
                    component.repaint();
                }
            }
        }
    }
}
