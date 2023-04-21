package org.jfonia.view.scribbles;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;
import org.jfonia.commands.AddScribbleCommand;
import org.jfonia.commands.RemoveScribbleCommand;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.constants.ViewConstants;
import org.jfonia.language.LanguageResource;
import org.jfonia.language.MessageConstants;
import org.jfonia.notation.Notation;
import org.jfonia.view.main.Project;
import org.jfonia.view.panels.BasicNodePanel;

/**
 *
 * @author Rik Bauwens
 */
public class ScribblePanel extends BasicNodePanel implements MouseInputListener, ComponentListener, IScribblesObserver {

    private Map<Scribble, ScribbleView> scribbleViews;

    private Scribbles scribbles;

    private boolean dragging;

    private ScribbleView draggedScribble;

    private Point draggedScribbleRelativePosition;

    private JLabel message;

    public ScribblePanel(MutableValueNode<Double> xNode, MutableValueNode<Double> yNode, MutableValueNode<Double> widthNode, MutableValueNode<Double> heightNode) {
        super(xNode, yNode, widthNode, heightNode);
        setVisible(false);
        scribbleViews = new LinkedHashMap<Scribble, ScribbleView>();
        dragging = false;
        message = new JLabel(LanguageResource.getInstance().getMessage(MessageConstants.CLICK_TO_ADD_SCRIBBLE));
        message.setOpaque(false);
        message.setVisible(true);
        message.setBounds(getWidth() / 2 - message.getPreferredSize().width / 2, getHeight() / 2 - message.getPreferredSize().height / 2, message.getPreferredSize().width, message.getPreferredSize().height);
        add(message);
        setBackground(ViewConstants.SCRIBBLE_PANEL_BACKGROUND_COLOR);
        addMouseListener(this);
        addComponentListener(this);
        setPreferredSize(new Dimension((int) ViewConstants.SCRIBBLE_PANEL_MIN_WIDTH, (int) ViewConstants.SCRIBBLE_PANEL_MIN_HEIGHT));
        repaint();
    }

    public void setScribbles(Scribbles scribbles) {
        if (this.scribbles != null) this.scribbles.removeObserver(this);
        clearScribbles();
        this.scribbles = scribbles;
        setVisible(false);
        if (scribbles == null) return;
        for (Scribble scribble : scribbles) addScribble(scribble);
        scribbles.addObserver(this);
        setVisible(true);
    }

    private void addScribble(Scribble scribble) {
        if (scribbleViews.containsKey(scribble)) return;
        ScribbleView scribbleView = new ScribbleView(scribble);
        scribbleViews.put(scribble, scribbleView);
        scribbleView.addMouseListener(this);
        scribbleView.addMouseMotionListener(this);
        add(scribbleView);
        if (!scribbles.isEmpty()) message.setVisible(false);
        repaint();
    }

    private void removeScribble(Scribble scribble) {
        if (!scribbleViews.containsKey(scribble)) return;
        ScribbleView scribbleView = scribbleViews.get(scribble);
        scribbleViews.remove(scribble);
        scribbleView.removeMouseListener(this);
        scribbleView.removeMouseMotionListener(this);
        remove(scribbleView);
        if (scribbles.isEmpty()) message.setVisible(true);
        repaint();
    }

    private void clearScribbles() {
        Iterator<Scribble> it = scribbleViews.keySet().iterator();
        while (it.hasNext()) {
            Scribble scribble = it.next();
            ScribbleView scribbleView = scribbleViews.get(scribble);
            scribbleView.removeMouseListener(this);
            scribbleView.removeMouseMotionListener(this);
            remove(scribbleView);
            it.remove();
        }
        message.setVisible(true);
        repaint();
    }

    public void scribbleAdded(Scribble scribble) {
        addScribble(scribble);
    }

    public void scribbleRemoved(Scribble scribble) {
        removeScribble(scribble);
    }

    private boolean hasScribbleFocus() {
        for (ScribbleView scribbleView : scribbleViews.values()) {
            if (scribbleView.hasFocus()) return true;
        }
        return false;
    }

    @Override
    public boolean hasFocus() {
        return hasScribbleFocus();
    }

    public void mouseClicked(MouseEvent e) {
        Notation.getInstance().clearSelectedElements();
        if (e.getSource() instanceof ScribblePanel) {
            if (e.getClickCount() == 2) {
                int x = e.getX();
                int y = e.getY();
                int width = ScribbleView.INITIAL_WIDTH;
                int height = ScribbleView.INITIAL_HEIGHT;
                x = Math.min(Math.max(0, x - width / 2), getWidth() - width);
                y = Math.min(Math.max(0, y - height / 2), getHeight() - height);
                Project.getInstance().getCurrentLeadSheet().execute(new AddScribbleCommand(scribbles, new Scribble(x, y)));
                repaint();
            }
            requestFocus();
        } else if (e.getSource() instanceof ScribbleView) {
            ScribbleView scribbleView = (ScribbleView) e.getSource();
            Rectangle2D mouseRect = new Rectangle2D.Double(e.getX(), e.getY(), ViewConstants.MOUSE_POINTER_REGION_WIDTH, ViewConstants.MOUSE_POINTER_REGION_HEIGHT);
            if (scribbleView.isClosed(mouseRect)) {
                Project.getInstance().getCurrentLeadSheet().execute(new RemoveScribbleCommand(scribbles, scribbleView.getScribble()));
                repaint();
                requestFocus();
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof ScribbleView) {
            draggedScribble = (ScribbleView) e.getSource();
            draggedScribbleRelativePosition = new Point(e.getPoint());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getSource() instanceof ScribbleView && dragging) {
            draggedScribble = null;
            draggedScribbleRelativePosition = null;
            dragging = false;
            requestFocus();
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getSource() instanceof ScribbleView && draggedScribble != null) {
            int x = (int) (draggedScribble.getX() + e.getX() - draggedScribbleRelativePosition.getX());
            int y = (int) (draggedScribble.getY() + e.getY() - draggedScribbleRelativePosition.getY());
            int width = draggedScribble.getWidth();
            int height = draggedScribble.getHeight();
            if (x < 0) x = 0; else if (x > getWidth() - width) x = getWidth() - width;
            if (y < 0) y = 0; else if (y > getHeight() - height) y = getHeight() - height;
            draggedScribble.setLocation(x, y);
            dragging = true;
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        message.setBounds(getWidth() / 2 - message.getPreferredSize().width / 2, getHeight() / 2 - message.getPreferredSize().height / 2, message.getPreferredSize().width, message.getPreferredSize().height);
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
}
