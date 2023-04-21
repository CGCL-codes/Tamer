package com.global360.sketchpadbpmn.graphic;

import java.awt.geom.GeneralPath;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.documents.xpdl.NameToken;
import com.global360.sketchpadbpmn.i18n.Messages;

public class BPMNCommunicationNodeGraphic extends BPMNGraphic {

    private static final long serialVersionUID = -3794196195374673403L;

    public BPMNCommunicationNodeGraphic(BpmnId id) {
        super(id);
        this.setProportionFixed(true);
        this.setProportion(1.0, Math.sqrt(3.0) / 2.0);
        this.setHeight(this.getHeight());
        updateShapes();
    }

    public BPMNCommunicationNodeGraphic(BPMNCommunicationNodeGraphic source) {
        super(source);
    }

    @Override
    public SketchGraphic clone() throws CloneNotSupportedException {
        return new BPMNCommunicationNodeGraphic(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        boolean result = false;
        if (other instanceof BPMNCommunicationNodeGraphic) {
            result = this.equals((BPMNCommunicationNodeGraphic) other);
        }
        return result;
    }

    public boolean equals(BPMNCommunicationNodeGraphic other) {
        if (this == other) return true;
        boolean result = false;
        if (other == null) return false;
        if (!super.equals(other)) return false;
        return result;
    }

    @Override
    public String getGraphicTypeName() {
        return Messages.getString("BPMNCommunicationNodeGraphic.Communication");
    }

    private double getBoundingSquareDimension() {
        return Math.min(this.getWidth(), this.getHeight());
    }

    private double getSideDimension() {
        double b = getBoundingSquareDimension();
        double side = (b / 2.0);
        return side;
    }

    @Override
    public void updateNameBounds() {
        double side = this.getSideDimension();
        double halfSide = side / 2.0;
        double u = (halfSide * Math.sqrt(3.0));
        this.setNameBounds(this.getX(), this.getY() + (u / 2.0), this.getWidth(), u);
    }

    @Override
    protected void updateShapes() {
        updateNameBounds();
        double side = this.getSideDimension();
        double halfSide = side / 2.0;
        double u = (halfSide * Math.sqrt(3.0));
        double x = this.getX();
        double y = this.getY();
        clearAllShapes();
        GeneralPath path = new GeneralPath(SketchGraphic.windingRule);
        path.moveTo(x + halfSide, y);
        path.lineTo(x + halfSide + side, y);
        path.lineTo(x + (2.0 * side), y + u);
        path.lineTo(x + halfSide + side, y + (2.0 * u));
        path.lineTo(x + halfSide, y + (2.0 * u));
        path.lineTo(x, y + u);
        path.closePath();
        this.addFillShape(path);
        this.addShape(path);
        updateConnectors();
    }
}
