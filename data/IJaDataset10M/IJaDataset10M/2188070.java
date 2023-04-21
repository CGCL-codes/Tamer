package uk.ac.lkl.migen.system.expresser.model.shape.block;

import java.util.*;
import uk.ac.lkl.common.util.BoundingBox;
import uk.ac.lkl.common.util.expression.ExpressionList;
import uk.ac.lkl.common.util.expression.operation.IntegerAdditionOperation;
import uk.ac.lkl.common.util.value.*;
import uk.ac.lkl.migen.system.expresser.io.*;
import uk.ac.lkl.migen.system.expresser.model.*;
import uk.ac.lkl.migen.system.expresser.model.event.*;

/**
 * A shape that consists of other shapes.
 * 
 * This is an abstraction over groups and patterns.
 * 
 * @author $Author: toontalk@gmail.com $
 * @version $Revision: 11313 $
 * @version $Date: 2012-02-14 06:08:09 -0500 (Tue, 14 Feb 2012) $
 * 
 */
public abstract class ContainerShape extends BlockShape {

    private ArrayList<BlockShape> shapes;

    protected boolean callUpdateBounds = true;

    private AttributeChangeListener<BlockShape> shapeListener = new AttributeChangeListener<BlockShape>() {

        @Override
        public void attributesChanged(AttributeChangeEvent<BlockShape> e) {
            if (callUpdateBounds) {
                updateBounds();
            }
        }
    };

    private SimpleValueSource<IntegerValue> xDriver;

    private SimpleValueSource<IntegerValue> yDriver;

    private SimpleValueSource<IntegerValue> widthDriver;

    private SimpleValueSource<IntegerValue> heightDriver;

    public ContainerShape() {
        shapes = new ArrayList<BlockShape>();
    }

    @Override
    public int getBasicShapeCount() {
        int total = 0;
        for (BlockShape shape : shapes) {
            total += shape.getBasicShapeCount();
        }
        return total;
    }

    protected void addShape(BlockShape shape) {
        shapes.add(shape);
        shape.addAttributeChangeListener(weakListener(shapeListener, shape));
        if (callUpdateBounds) {
            updateBounds();
        }
        shape.setSuperShape(this);
        setCorrectExpressionsUpToDate(false);
    }

    public void addShapes(Iterable<BlockShape> shapesToAdd) {
        callUpdateBounds = false;
        for (BlockShape shape : shapesToAdd) {
            addShape(shape);
        }
        callUpdateBounds = true;
        updateBounds();
    }

    protected void removeAllShapes() {
        for (int i = shapes.size() - 1; i >= 0; i--) removeShape(i);
    }

    protected void removeShapes(Iterable<BlockShape> shapesToRemove) {
        boolean currentCallUpdateBounds = callUpdateBounds;
        callUpdateBounds = false;
        for (BlockShape shape : shapesToRemove) removeShape(shape);
        updateBounds();
        callUpdateBounds = currentCallUpdateBounds;
    }

    protected void removeShape(int index) {
        removeShape(shapes.get(index));
    }

    public void removeShape(BlockShape shape) {
        boolean removed = shapes.remove(shape);
        if (!removed) return;
        shape.removeAttributeChangeListener(shapeListener);
        if (callUpdateBounds) updateBounds();
        shape.setSuperShape(null);
        setCorrectExpressionsUpToDate(false);
    }

    public List<BlockShape> getShapes() {
        if (shapes == null) return null;
        return Collections.unmodifiableList(shapes);
    }

    public BlockShape getShape(int index) {
        if (shapes.size() > index) {
            return shapes.get(index);
        } else {
            return null;
        }
    }

    public int getShapeCount() {
        return shapes.size();
    }

    public Iterator<BlockShape> iterator() {
        return shapes.iterator();
    }

    @Override
    public Attribute<IntegerValue> createXAttribute() {
        xDriver = new SimpleValueSource<IntegerValue>(new IntegerValue(0));
        return new Attribute<IntegerValue>(X, new DrivenValueSource<IntegerValue>(xDriver) {

            @Override
            public boolean isValueSettable() {
                return true;
            }

            @Override
            public boolean setValue(IntegerValue value) {
                updateShapesXLocation(getValue(), value);
                return true;
            }
        });
    }

    protected void updateShapesXLocation(IntegerValue currentValue, IntegerValue newValue) {
        IntegerValue difference = newValue.subtract(currentValue);
        if (difference.getInt() == 0) return;
        callUpdateBounds = false;
        for (BlockShape shape : getShapes()) {
            IntegerValue currentX = shape.getAttributeValue(X);
            IntegerValue newX = currentX.add(difference);
            shape.setXValue(newX);
        }
        updateBounds();
        callUpdateBounds = true;
    }

    @Override
    public Attribute<IntegerValue> createYAttribute() {
        yDriver = new SimpleValueSource<IntegerValue>(new IntegerValue(0));
        return new Attribute<IntegerValue>(Y, new DrivenValueSource<IntegerValue>(yDriver) {

            @Override
            public boolean isValueSettable() {
                return true;
            }

            @Override
            public boolean setValue(IntegerValue value) {
                updateShapesYLocation(getValue(), value);
                return true;
            }
        });
    }

    protected void updateShapesYLocation(IntegerValue currentValue, IntegerValue newValue) {
        IntegerValue difference = newValue.subtract(currentValue);
        if (difference.getInt() == 0) return;
        callUpdateBounds = false;
        for (BlockShape shape : getShapes()) {
            IntegerValue currentY = shape.getAttributeValue(Y);
            IntegerValue newY = currentY.add(difference);
            shape.setYValue(newY);
        }
        updateBounds();
        callUpdateBounds = true;
    }

    @Override
    public Attribute<IntegerValue> createWidthAttribute() {
        widthDriver = new SimpleValueSource<IntegerValue>(new IntegerValue(0));
        return new Attribute<IntegerValue>(WIDTH, new DrivenValueSource<IntegerValue>(widthDriver));
    }

    @Override
    public Attribute<IntegerValue> createHeightAttribute() {
        heightDriver = new SimpleValueSource<IntegerValue>(new IntegerValue(0));
        return new Attribute<IntegerValue>(HEIGHT, new DrivenValueSource<IntegerValue>(heightDriver));
    }

    public void updateBounds() {
        ChainingAttributeChangeSupport.ChangeSet changeSet = new ChainingAttributeChangeSupport.ChangeSet() {

            @Override
            public void execute() {
                updateBoundsInternal();
            }
        };
        setAttributeValues(changeSet);
    }

    protected void updateBoundsInternal() {
        IntegerValue minX = null;
        IntegerValue maxX = null;
        IntegerValue minY = null;
        IntegerValue maxY = null;
        for (BlockShape shape : getShapes()) {
            IntegerValue x1 = shape.getXValue();
            IntegerValue y1 = shape.getYValue();
            IntegerValue width = shape.getWidthValue();
            IntegerValue height = shape.getHeightValue();
            IntegerValue x2 = x1.add(width);
            IntegerValue y2 = y1.add(height);
            if (minX == null || x1.isLessThan(minX)) minX = x1;
            if (minY == null || y1.isLessThan(minY)) minY = y1;
            if (maxX == null || x2.isGreaterThan(maxX)) maxX = x2;
            if (maxY == null || y2.isGreaterThan(maxY)) maxY = y2;
        }
        IntegerValue width;
        IntegerValue height;
        if (minX != null && maxX != null) width = maxX.subtract(minX); else width = new IntegerValue(0);
        if (minY != null && maxY != null) height = maxY.subtract(minY); else height = new IntegerValue(0);
        if (minX != null) xDriver.setValue(minX);
        if (minY != null) yDriver.setValue(minY);
        widthDriver.setValue(width);
        heightDriver.setValue(height);
        processBoundsUpdate();
    }

    protected void processBoundsUpdate() {
    }

    /**
     * Returns the bounding box for this container shape in the model, 
     * or null if the shape has no width or no height or both (which
     * can happen with PatternShapes).
     * 
     * @return the bounding box for this container shape in the model.
     */
    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox result = null;
        for (BlockShape shape : getShapes()) {
            BoundingBox boundingBox = shape.getBoundingBox();
            if (result == null) {
                result = boundingBox;
            } else {
                result.extendToInclude(boundingBox);
            }
        }
        return result;
    }

    /**
     * Returns a list of all the tiles in this shape, as defined
     * by their (x,y) position and their colour.
     * 
     * @return a list of all the tiles in this shape.
     */
    @Override
    public TileList getTiles() {
        TileList result = new TileList();
        for (Iterator<BlockShape> itr = shapes.iterator(); itr.hasNext(); ) {
            BlockShape shape = itr.next();
            if (shape instanceof BasicShape) {
                BasicShape basicShape = (BasicShape) shape;
                int x = shape.getX();
                int y = shape.getY();
                ModelColor color = basicShape.getColor();
                result.add(new Tile(x, y, color));
            } else {
                TileList tileList = shape.getTiles();
                result.addAll(tileList);
            }
        }
        return result;
    }

    @Override
    public void generateCorrectColorExpressions() {
        for (ColorResourceAttributeHandle colorResourceAttributeHandle : getAllColorResourceAttributeHandles()) {
            ModelColor handleColor = colorResourceAttributeHandle.getColor();
            CorrectColorExpressionAttributeHandle handle = CorrectColorExpressionAttributeHandle.getHandle(handleColor);
            ExpressionList<IntegerValue> operands = new ExpressionList<IntegerValue>();
            for (BlockShape shape : getShapes()) {
                Attribute<IntegerValue> attribute = shape.getAttribute(handle);
                if (attribute == null) {
                    shape.generateCorrectColorExpressions();
                    attribute = shape.getAttribute(handle);
                }
                if (attribute != null) {
                    if (attribute.getValueSource() == null) {
                        shape.generateCorrectColorExpressions();
                    }
                    if (!shape.isCorrectExpressionsUpToDate()) {
                        shape.generateCorrectColorExpressions();
                    }
                    ObjectExpression<IntegerValue> objectExpression = shape.getObjectExpression(attribute);
                    operands.add(objectExpression);
                }
            }
            IntegerAdditionOperation integerAdditionOperation = new IntegerAdditionOperation(operands);
            ExpressionValueSource<IntegerValue> expressionValueSource = new ExpressionValueSource<IntegerValue>(integerAdditionOperation);
            if (!attributeExists(handle)) {
                addAttribute(handle);
            }
            setValueSource(handle, expressionValueSource);
        }
        setCorrectExpressionsUpToDate(true);
    }

    @Override
    protected boolean allLocalColorAllocationsCorrect(CorrectColorExpressionAttributeHandle handle) {
        return subShapesLocalColorAllocationsCorrect(handle);
    }

    @Override
    protected boolean subShapesLocalColorAllocationsCorrect(CorrectColorExpressionAttributeHandle handle) {
        for (BlockShape shape : getShapes()) {
            if (!shape.allLocalColorAllocationsCorrect(handle)) {
                return false;
            }
        }
        return true;
    }

    protected SimpleValueSource<IntegerValue> getxDriver() {
        return xDriver;
    }

    protected SimpleValueSource<IntegerValue> getyDriver() {
        return yDriver;
    }

    protected SimpleValueSource<IntegerValue> getWidthDriver() {
        return widthDriver;
    }

    protected SimpleValueSource<IntegerValue> getHeightDriver() {
        return heightDriver;
    }

    @Override
    public void moveBy(IntegerValue deltaX, IntegerValue deltaY) {
        super.moveBy(deltaX, deltaY);
        for (BlockShape shape : getShapes()) {
            shape.moveBy(deltaX, deltaY);
        }
    }

    @Override
    public boolean isCompletelyPositive() {
        if (!isPositive()) {
            return false;
        } else {
            for (BlockShape shape : getShapes()) {
                if (!shape.isCompletelyPositive()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public BlockShape getSubShapeWithId(String expressionId) {
        List<BlockShape> subShapes = getShapes();
        for (BlockShape subShape : subShapes) {
            if (expressionId.equals(subShape.getUniqueId())) {
                return subShape;
            }
            BlockShape subShapeWithId = subShape.getSubShapeWithId(expressionId);
            if (subShapeWithId != null) {
                return subShapeWithId;
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        int size = shapes.size();
        for (int i = size - 1; i >= 0; i--) {
            BlockShape shape = shapes.get(i);
            removeShape(shape);
            shape.removeAll();
        }
        super.removeAll();
    }
}
