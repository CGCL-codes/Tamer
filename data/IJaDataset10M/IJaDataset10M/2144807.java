package uk.ac.lkl.migen.mockup.shapebuilder.ui.tool.shape;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.apache.log4j.Logger;
import uk.ac.lkl.common.ui.Painter;
import uk.ac.lkl.migen.mockup.shapebuilder.model.event.*;
import uk.ac.lkl.migen.mockup.shapebuilder.ui.*;
import uk.ac.lkl.migen.mockup.shapebuilder.ui.tool.AbstractTool;

public abstract class ShapeTool extends AbstractTool implements Painter {

    protected ShapePlotter shapePlotter;

    private String name = null;

    private boolean selected;

    static Logger logger = Logger.getLogger(ShapeTool.class);

    public ShapeTool(ShapePlotter shapePlotter, String buttonText) {
        super(shapePlotter.getModel(), new JRadioButton(buttonText));
        name = new String(buttonText);
        this.shapePlotter = shapePlotter;
        this.selected = false;
        shapePlotter.addPainter(this);
        getButton().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                processStateChanged();
            }
        });
        shapePlotter.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (selected) processMousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (selected) processMouseReleased(e);
            }

            public void mouseClicked(MouseEvent e) {
                if (selected) processMouseClicked(e);
            }
        });
        shapePlotter.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                if (selected) processMouseMoved(e);
            }

            public void mouseDragged(MouseEvent e) {
                if (selected) processMouseDragged(e);
            }
        });
        shapePlotter.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (selected) processKeyPressed(e);
            }
        });
        getModel().addShapeListener(new ShapeListener() {

            public void shapeAdded(ShapeEvent e) {
                processShapeAdded(e);
            }

            public void shapeRemoved(ShapeEvent e) {
                processShapeRemoved(e);
            }
        });
    }

    public ShapePlotter getShapePlotter() {
        return shapePlotter;
    }

    protected final void processActionPerformed() {
        initialiseTool();
        logger.info("Tool/mode '" + this.getName() + "' selected.");
    }

    protected final void processStateChanged() {
        selected = getButton().isSelected();
        if (selected) shapePlotter.repaint();
    }

    protected void initialiseTool() {
        shapePlotter.setCursor(getToolCursor());
    }

    protected void processMousePressed(MouseEvent e) {
    }

    protected void processMouseReleased(MouseEvent e) {
    }

    protected void processMouseClicked(MouseEvent e) {
    }

    protected void processMouseMoved(MouseEvent e) {
    }

    protected void processMouseDragged(MouseEvent e) {
    }

    protected void processKeyPressed(KeyEvent e) {
    }

    protected void processShapeAdded(ShapeEvent e) {
    }

    protected void processShapeRemoved(ShapeEvent e) {
    }

    protected Cursor getToolCursor() {
        return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    }

    public final void doPainting(Graphics2D g2) {
        if (selected) paintToolMarks(g2);
    }

    protected void paintToolMarks(Graphics2D g2) {
    }

    public String getName() {
        return name;
    }
}
