package org.argouml.sequence2.diagram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.SelectionRerouteEdge;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * A custom select object to handle the special requirements of
 * rerouting, reshaping or dragging a message.
 * @author penyaskito
 */
class SelectionMessage extends SelectionRerouteEdge {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SelectionMessage.class);

    /**
     * The constructor
     * @param feme the fig.
     */
    public SelectionMessage(FigEdgeModelElement feme) {
        super(feme);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            ke.consume();
        } else {
            handleMovement();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        FigMessage message = (FigMessage) getContent();
        if (!message.isSelfMessage()) {
            super.mousePressed(me);
        }
    }

    @Override
    public void dragHandle(int x, int y, int w, int h, Handle handle) {
        FigMessage message = (FigMessage) getContent();
        if (message.isSelfMessage()) {
            message.translate(0, y - message.getY());
        } else {
            super.dragHandle(x, y, w, h, handle);
            handleMovement();
        }
    }

    private void handleMovement() {
        FigMessage figMessage = (FigMessage) getContent();
        FigClassifierRole source = (FigClassifierRole) figMessage.getSourceFigNode();
        FigClassifierRole dest = (FigClassifierRole) figMessage.getDestFigNode();
        if (figMessage.getFinalY() > source.getY() + source.getHeight() - 10) {
            final int newHeight = source.getHeight() + 10;
            final List<Fig> figs = getContent().getLayer().getContents();
            for (Fig workOnFig : figs) {
                if (workOnFig instanceof FigClassifierRole) {
                    workOnFig.setHeight(newHeight);
                }
            }
        }
        dest.positionHead(figMessage);
        source.createActivations();
        if (!figMessage.isSelfMessage()) {
            dest.createActivations();
        }
    }
}
