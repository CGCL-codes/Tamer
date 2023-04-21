package edu.udo.scaffoldhunter.view.dendrogram;

import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * This class defines the dragging behaviour of the cluster selection bar
 * 
 * @author Philipp Lewe
 * 
 */
public class ClusterSelectionDragEventHandler extends PDragEventHandler {

    private double height;

    private double actHeight;

    private ClusterSelectionBar bar;

    ClusterSelectionDragEventHandler(double height, ClusterSelectionBar bar) {
        super();
        this.height = height;
        this.bar = bar;
        actHeight = 0;
        PInputEventFilter ef = new PInputEventFilter();
        ef.setAcceptsMouseDragged(true);
        setEventFilter(ef);
    }

    /**
     * @param position
     */
    public void setPosition(double position) {
        actHeight = position;
    }

    /**
     * Moves the dragged node in proportion to the drag distance
     * 
     * @param event
     *            event representing the drag
     */
    @Override
    protected void drag(final PInputEvent event) {
        final PDimension d = event.getDeltaRelativeTo(super.getDraggedNode());
        super.getDraggedNode().localToParent(d);
        if (!(actHeight + d.getHeight() < -5 || actHeight + d.getHeight() > height)) {
            super.getDraggedNode().offset(0, d.getHeight());
            actHeight += d.getHeight();
        }
        bar.fireClusterSelectionBarDragActive();
    }

    /**
     * @return the selectionbar Position;
     */
    public double getSelectionbarPosition() {
        return actHeight;
    }

    @Override
    protected void dragActivityFirstStep(PInputEvent event) {
        bar.fireClusterSelectionBarDragStarted();
    }

    @Override
    protected void dragActivityFinalStep(PInputEvent aEvent) {
        bar.fireClusterSelectionBarDragReleased();
    }
}
