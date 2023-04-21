package uk.ac.lkl.migen.system.expresser.ui.view.shape.block;

import java.awt.*;
import java.awt.geom.Area;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BasicShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.ui.ObjectSetCanvas;
import uk.ac.lkl.migen.system.expresser.ui.ObjectSetView;
import uk.ac.lkl.migen.system.expresser.ui.view.AbstractLocatedObjectView;

public class UnresourcedBasicShapeView extends AbstractLocatedObjectView {

    public UnresourcedBasicShapeView(ObjectSetCanvas view) {
        super(null, view);
    }

    public Area getStrictBoundingArea(BlockShape object) {
        return new Area(this.getViewBounds());
    }

    @Override
    public boolean strictlyContains(BlockShape object, int x, int y) {
        BasicShape shape = (BasicShape) object;
        IntegerValue xValue = shape.getAttributeValue(BlockShape.X);
        IntegerValue yValue = shape.getAttributeValue(BlockShape.Y);
        IntegerValue widthValue = shape.getAttributeValue(BlockShape.WIDTH);
        IntegerValue heightValue = shape.getAttributeValue(BlockShape.HEIGHT);
        int gridSize = getGridSize();
        int viewX = xValue.getInt() * gridSize;
        int viewY = yValue.getInt() * gridSize;
        int viewWidth = widthValue.getInt() * gridSize;
        int viewHeight = heightValue.getInt() * gridSize;
        Rectangle rectangle = new Rectangle(viewX, viewY, viewWidth, viewHeight);
        return rectangle.contains(x, y);
    }

    public void paintObject(ObjectSetView.ObjectPainter objectPainter, BlockShape object, Graphics2D g2) {
        BasicShape shape = (BasicShape) object;
        IntegerValue x = shape.getAttributeValue(BlockShape.X);
        IntegerValue y = shape.getAttributeValue(BlockShape.Y);
        IntegerValue width = shape.getAttributeValue(BlockShape.WIDTH);
        IntegerValue height = shape.getAttributeValue(BlockShape.HEIGHT);
        int gridSize = getGridSize();
        int viewX = x.getInt() * gridSize;
        int viewY = y.getInt() * gridSize;
        int viewWidth = width.getInt() * gridSize;
        int viewHeight = height.getInt() * gridSize;
        ModelColor modelColor = shape.getColor();
        g2.setColor(new Color(modelColor.getRed(), modelColor.getGreen(), modelColor.getBlue(), 255));
        g2.fillRect(viewX + 6, viewY + 6, viewWidth - 12, viewHeight - 12);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.GRAY);
        g2.drawRect(viewX + 6, viewY + 6, viewWidth - 12, viewHeight - 12);
    }

    protected void processViewChange() {
    }

    protected void processAttributeChange() {
    }
}
