package pogvue.gui;

import pogvue.gui.event.ColumnSelectionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class ScalePanel extends JPanel implements MouseListener {

    public ScaleCanvas scaleCanvas;

    protected int offy;

    public int width;

    private final AlignViewport av;

    private final Controller controller;

    public ScalePanel(AlignViewport av, Controller c) {
        this.av = av;
        this.controller = c;
        componentInit();
        System.out.println("AV " + av);
        System.out.println("Loaded ScalePanel");
    }

    private void componentInit() {
        scaleCanvas = new ScaleCanvas(av, controller);
        setLayout(new BorderLayout());
        add("Center", scaleCanvas);
        addMouseListener(this);
        scaleCanvas.addMouseListener(this);
        controller.addListener(scaleCanvas);
    }

    public Dimension getMinimumSize() {
        return scaleCanvas.getMinimumSize();
    }

    public Dimension getPreferredSize() {
        return scaleCanvas.getPreferredSize();
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        int res = (int) (x / av.getCharWidth() + av.getStartRes());
        boolean found = false;
        System.out.println("Selected column = " + res);
        if (!av.getColumnSelection().contains(res)) {
            av.getColumnSelection().addElement(res);
        } else {
            av.getColumnSelection().removeElement(res);
        }
        controller.handleColumnSelectionEvent(new ColumnSelectionEvent(this, av.getColumnSelection()));
    }

    public void mouseReleased(MouseEvent evt) {
    }
}
