package Core.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import Commands.ColoringCommand;
import Commands.CommandManager;
import Commands.DeleteCommand;
import Commands.FilledCommend;
import Commands.FlipCommand;
import Commands.LocateCommand;
import Commands.RotateCommand;
import Commands.WidthChangedCommend;
import Core.plugins.NullShape;
import Core.plugins.ShapeIF;
import UI.Application;

/**
 * Select tool that provides the implementations pf the dunction can be processed when the item is selected
 * @see ToolIF  implemented interface
 * @see ShapeIF
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class SelectTool extends AbstractTool {

    /**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
    private static SelectTool instance;

    /**
	 * @uml.property  name="nothingState"
	 * @uml.associationEnd  
	 */
    private static NoSelectedItems nothingState;

    /**
	 * @uml.property  name="selectedState"
	 * @uml.associationEnd  
	 */
    private static SelectedItems selectedState;

    /**
	 * @uml.property  name="rotating"
	 * @uml.associationEnd  
	 */
    private static RotatingItems rotating;

    private Cursor moveCursor;

    ;

    /**
	 * @uml.property  name="selectedShapes"
	 */
    private ArrayList<ShapeIF> selectedShapes;

    private Rectangle shapeArea;

    /**
	 * @uml.property  name="rotatingShape"
	 * @uml.associationEnd  
	 */
    private ShapeIF rotatingShape;

    private boolean mouseDown;

    private Point start;

    private double finalTheta;

    /**
	 * @uml.property  name="currentState"
	 * @uml.associationEnd  
	 */
    private SelectState currentState;

    private Point initialSelection;

    private boolean insideShapeArea;

    private Cursor crossCursor;

    /**
	 * @return
	 * @uml.property  name="instance"
	 */
    public static synchronized SelectTool getInstance() {
        if (instance == null) return instance = new SelectTool();
        return instance;
    }

    private SelectTool() {
        element = NullShape.getInstance();
        moveCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_SIZEALL);
        crossCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_CROSS);
        selectedShapes = new ArrayList<ShapeIF>();
        nothingState = new NoSelectedItems();
        selectedState = new SelectedItems();
        rotating = new RotatingItems();
        currentState = nothingState;
        initialSelection = new Point(0, 0);
        mouseDown = false;
        start = new Point(0, 0);
    }

    public void drawShape(GC gc, Point pt) {
        currentState.drawShape(gc, pt);
    }

    public void mouseDown(MouseEvent e) {
        currentState.mouseDown(e);
    }

    public void mouseUp(MouseEvent e) {
        currentState.mouseUp(e);
    }

    public void mouseMove(MouseEvent e) {
        currentState.mouseMove(e);
    }

    public void selectAll() {
        selectedShapes.clear();
        Iterator<ShapeIF> iter = Application.getInstance().getShapes().iterator();
        ShapeIF current;
        if (iter.hasNext()) {
            current = iter.next();
            current.select();
            selectedShapes.add(current);
            shapeArea = current.getBounds();
        }
        while (iter.hasNext()) {
            current = iter.next();
            current.select();
            selectedShapes.add(current);
            shapeArea = shapeArea.union(current.getBounds());
        }
        currentState = selectedState;
        Application.getInstance().refresh();
    }

    public boolean changeColor(Color newColor) {
        if (currentState instanceof SelectState && selectedShapes.size() > 0) {
            ColoringCommand command = new ColoringCommand(newColor, selectedShapes);
            command.doIt();
            CommandManager.getInstance().registerCommand(command);
            return true;
        }
        return false;
    }

    public boolean fill() {
        if (currentState instanceof SelectState && selectedShapes.size() > 0) {
            FilledCommend command = new FilledCommend(selectedShapes);
            command.doIt();
            CommandManager.getInstance().registerCommand(command);
            return true;
        }
        return false;
    }

    /**
	 * flip the selected shapes vertically or horizontally
	 * @param mode type of flipping
	 * @return boolean that indicates success or failure of the process
	 */
    public boolean flip(int mode) {
        if (currentState instanceof SelectState && selectedShapes.size() > 0) {
            FlipCommand command = new FlipCommand(selectedShapes, mode);
            command.doIt();
            CommandManager.getInstance().registerCommand(command);
            return true;
        }
        return false;
    }

    public boolean changeWidth(int width) {
        if (currentState instanceof SelectState && selectedShapes.size() > 0) {
            WidthChangedCommend command = new WidthChangedCommend(selectedShapes, width);
            command.doIt();
            CommandManager.getInstance().registerCommand(command);
            return true;
        }
        return false;
    }

    public boolean deleteSelected() {
        if (currentState instanceof SelectState && selectedShapes.size() > 0) {
            DeleteCommand command = new DeleteCommand(selectedShapes);
            command.doIt();
            CommandManager.getInstance().registerCommand(command);
            Application.getInstance().refresh();
            return true;
        }
        return false;
    }

    private abstract class SelectState {

        public abstract void mouseDown(MouseEvent e);

        public abstract void drawShape(GC gc, Point pt);

        public abstract void mouseUp(MouseEvent e);

        public abstract void mouseMove(MouseEvent e);
    }

    private class NoSelectedItems extends SelectState {

        public void mouseDown(MouseEvent e) {
            mouseDown = true;
            start.x = e.x;
            start.y = e.y;
            List<ShapeIF> shapes = Application.getInstance().getShapes();
            Iterator<ShapeIF> iter = shapes.iterator();
            ShapeIF current;
            while (iter.hasNext()) {
                current = iter.next();
                if (current.contains(start)) {
                    current.select();
                    selectedShapes.add(current);
                    currentState = selectedState;
                    initialSelection.x = e.x;
                    initialSelection.y = e.y;
                    shapeArea = current.getBounds();
                }
            }
        }

        public void mouseUp(MouseEvent e) {
            mouseDown = false;
            Rectangle rect = new Rectangle(Math.min(e.x, start.x), Math.min(e.y, start.y), Math.abs(start.x - e.x), Math.abs(start.y - e.y));
            List<ShapeIF> shapes = Application.getInstance().getShapes();
            Iterator<ShapeIF> iter = shapes.iterator();
            ShapeIF current;
            while (iter.hasNext()) {
                current = iter.next();
                shapeArea = current.getBounds();
                if (rect.contains(shapeArea.x, shapeArea.y) && rect.contains(shapeArea.x + shapeArea.width, shapeArea.y + shapeArea.height)) {
                    current.select();
                    selectedShapes.add(current);
                    initialSelection.x = e.x;
                    initialSelection.y = e.y;
                    currentState = selectedState;
                }
            }
            iter = selectedShapes.iterator();
            while (iter.hasNext()) {
                current = iter.next();
                shapeArea = shapeArea.union(current.getBounds());
            }
            Application.getInstance().refresh();
        }

        public void mouseMove(MouseEvent e) {
            if (mouseDown) Application.getInstance().refresh();
        }

        public void drawShape(GC gc, Point pt) {
            if (mouseDown) {
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                gc.setLineStyle(SWT.LINE_DASH);
                gc.drawRectangle(start.x, start.y, pt.x - start.x, pt.y - start.y);
                gc.setLineStyle(SWT.LINE_CUSTOM);
            }
        }
    }

    private class SelectedItems extends SelectState {

        public void mouseDown(MouseEvent e) {
            mouseDown = true;
            if (!shapeArea.contains(e.x, e.y)) {
                Iterator<ShapeIF> iter = selectedShapes.iterator();
                while (iter.hasNext()) iter.next().deselect();
                selectedShapes.clear();
                currentState = nothingState;
                nothingState.mouseDown(e);
            } else {
                initialSelection.x = e.x;
                initialSelection.y = e.y;
                boolean onVertex = false;
                Iterator<ShapeIF> iter = selectedShapes.iterator();
                ShapeIF currentShape;
                while (iter.hasNext()) {
                    currentShape = iter.next();
                    ArrayList<Point> points = currentShape.getPoints();
                    Iterator<Point> pointIter = points.iterator();
                    Point region;
                    while (pointIter.hasNext()) {
                        region = pointIter.next();
                        if (Math.abs(e.x - region.x) <= 5 && Math.abs(e.x - region.x) <= 5) {
                            rotatingShape = currentShape;
                            onVertex = true;
                        }
                    }
                }
                if (onVertex) {
                    currentState = rotating;
                    finalTheta = 0;
                }
                start.x = e.x;
                start.y = e.y;
            }
        }

        public void mouseUp(MouseEvent e) {
            if (mouseDown) {
                Iterator<ShapeIF> iter = selectedShapes.iterator();
                int shiftX = e.x - start.x;
                int shiftY = e.y - start.y;
                start.x = e.x;
                start.y = e.y;
                while (iter.hasNext()) iter.next().relocate(shiftX, shiftY);
                LocateCommand command = new LocateCommand(selectedShapes, e.x - initialSelection.x, e.y - initialSelection.y);
                CommandManager.getInstance().registerCommand(command);
                Application.getInstance().refresh();
            }
            mouseDown = false;
        }

        public void mouseMove(MouseEvent e) {
            if (mouseDown) {
                Iterator<ShapeIF> iter = selectedShapes.iterator();
                int shiftX = e.x - start.x;
                int shiftY = e.y - start.y;
                start.x = e.x;
                start.y = e.y;
                while (iter.hasNext()) iter.next().relocate(shiftX, shiftY);
                Application.getInstance().refresh();
            } else {
                if (shapeArea.contains(e.x, e.y)) insideShapeArea = true; else insideShapeArea = false;
            }
        }

        public void drawShape(GC gc, Point pt) {
            if (insideShapeArea) {
                Application.getInstance().setCursor(moveCursor);
                Application.getInstance().refresh();
            } else {
                Application.getInstance().setCursor(crossCursor);
                Application.getInstance().refresh();
            }
            gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
            gc.setLineStyle(SWT.LINE_DASH);
            Iterator<ShapeIF> iter = selectedShapes.iterator();
            Rectangle rectangle = iter.next().getBounds();
            while (iter.hasNext()) rectangle = rectangle.union(iter.next().getBounds());
            rectangle.x -= 5;
            rectangle.y -= 5;
            rectangle.width += 10;
            rectangle.height += 10;
            gc.setLineWidth(1);
            gc.drawRectangle(rectangle);
            gc.setLineStyle(SWT.LINE_CUSTOM);
        }
    }

    private class RotatingItems extends SelectState {

        @Override
        public void drawShape(GC gc, Point pt) {
        }

        @Override
        public void mouseDown(MouseEvent e) {
        }

        @Override
        public void mouseMove(MouseEvent e) {
            if (mouseDown) {
                Point center = rotatingShape.calculateCenter();
                int v1X = start.x - center.x, v1Y = start.y - center.y;
                int v2X = e.x - center.x;
                int v2Y = e.y - center.y;
                double cosTheta = (double) v1X * v2X - v1Y * v2Y;
                cosTheta /= Math.sqrt(v1X * v1X + v1Y * v1Y);
                cosTheta /= Math.sqrt(v2X * v2X + v2Y * v2Y);
                double theta;
                if (v1X * v2Y - v2X * v1Y > 0) theta = Math.acos(cosTheta); else theta = 360 - Math.acos(cosTheta);
                finalTheta += theta;
                Iterator<ShapeIF> iter = selectedShapes.iterator();
                while (iter.hasNext()) iter.next().rotate(theta);
                start.x = e.x;
                start.y = e.y;
            }
        }

        @Override
        public void mouseUp(MouseEvent e) {
            finalTheta %= Math.PI * 2;
            RotateCommand command = new RotateCommand(selectedShapes, finalTheta);
            CommandManager.getInstance().registerCommand(command);
            currentState = selectedState;
            finalTheta = 0;
        }
    }

    /**
	 * @return
	 * @uml.property  name="selectedShapes"
	 */
    public ArrayList<ShapeIF> getSelectedShapes() {
        return selectedShapes;
    }

    /**
	 * @param selectedShapes
	 * @uml.property  name="selectedShapes"
	 */
    public void setSelectedShapes(ArrayList<ShapeIF> selectedShapes) {
        this.selectedShapes = selectedShapes;
    }
}
