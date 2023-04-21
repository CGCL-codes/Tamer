package com.global360.sketchpadbpmn.Modes;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.WorkflowCanvas;
import com.global360.sketchpadbpmn.graphic.BPMNFlowGraphic;
import com.global360.sketchpadbpmn.graphic.lines.LabeledLineGraphic;
import com.global360.sketchpadbpmn.handle.ConnectorHandle;
import com.global360.sketchpadbpmn.handle.ConnectorLabelHandle;

/**
 * @author andya
 *
 */
public class DisplaceConnectorLabelMode extends EditConnectorMode {

    private Point2D.Double handleCenterAtStart = new Point2D.Double();

    private Point2D.Float nameOffsetAtStart = new Point2D.Float();

    private Point2D.Float nameOffsetCurrently = new Point2D.Float();

    /**
   * @param owner
   * @param canvas
   */
    public DisplaceConnectorLabelMode(SketchManager owner, WorkflowCanvas canvas) {
        super(owner, canvas, CanvasMode.DISPLACE_CONNECTOR_LABEL_MODE);
    }

    @Override
    public void setActiveHandle(ConnectorHandle activeHandle) {
        SketchpadMode.log.debug("DisplaceConnectorLabelMode.setActiveHandle: activeHandle center " + activeHandle.getCenter());
        this.handleCenterAtStart.setLocation(activeHandle.getCenter());
        LabeledLineGraphic line = (LabeledLineGraphic) activeHandle.getReferent();
        SketchpadMode.log.debug("DisplaceConnectorLabelMode.setActiveHandle: line name offset " + line.getTextBoxDisplacement());
        this.nameOffsetAtStart.setLocation(line.getTextBoxDisplacement());
        this.nameOffsetCurrently.setLocation(line.getTextBoxDisplacement());
        super.setActiveHandle(activeHandle);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        SketchpadMode.log.debug("DisplaceConnectorLabelMode.mouseDragged.");
        Point2D.Double location = this.canvas.mapPoint(e.getPoint());
        location = this.canvas.mapToGrid(location);
        this.currentLocation.setLocation(location);
        moveActiveHandle(e, location);
        this.canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            saveChanges();
            this.canvas.setMode(CanvasMode.SELECT_MODE);
        } catch (SketchpadException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void saveChanges() throws SketchpadException {
        BPMNFlowGraphic original = (BPMNFlowGraphic) this.canvas.getSelection().findOriginal(this.connector);
        this.canvas.getCommandManager().setFlowNameOffset(original, this.connector.getTextBoxDisplacement());
        this.canvas.getSelection().reselect();
        this.canvas.repaint();
    }

    @Override
    public void moveActiveHandle(MouseEvent event, Point2D.Double location) {
        SketchpadMode.log.debug("DisplaceConnectorLabelMode.moveActiveHandle: " + location);
        ConnectorLabelHandle handle = getActiveLabelHandle();
        handle.setCenter(location);
        LabeledLineGraphic line = (LabeledLineGraphic) activeHandle.getReferent();
        this.nameOffsetCurrently.x = (float) (location.x - this.handleCenterAtStart.x + this.nameOffsetAtStart.x);
        this.nameOffsetCurrently.y = (float) (location.y - this.handleCenterAtStart.y + this.nameOffsetAtStart.y);
        line.setTextBoxDisplacement(this.nameOffsetCurrently);
    }

    /**
   * @return
   */
    private ConnectorLabelHandle getActiveLabelHandle() {
        return (ConnectorLabelHandle) this.activeHandle;
    }
}
