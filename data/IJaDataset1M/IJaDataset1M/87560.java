package JavaOrc.ui;

import diagram.*;
import diagram.figures.*;
import diagram.tool.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import JavaOrc.diagram.CompositionItem;
import JavaOrc.diagram.CompositionLink;

/**
 * @class CardinalityTool
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 */
public class CardinalityTool extends AbstractTool {

    protected MouseHandler mouseHandler = new MouseHandler();

    protected Popup popup = new Popup();

    protected CompositionItem item;

    protected CompositionLink link;

    protected Diagram diagram;

    public void install(Diagram diagram) {
        diagram.addMouseListener(mouseHandler);
    }

    public void uninstall(Diagram diagram) {
        diagram.removeMouseListener(mouseHandler);
        reset();
    }

    /**
   * @class MouseHandler
   *
   */
    protected class MouseHandler extends MouseAdapter {

        /**
     * Called when the mouse is clicked.
     *
     * @param MouseEvent
     */
        public void mouseClicked(MouseEvent e) {
            if (!e.isConsumed() && SwingUtilities.isRightMouseButton(e)) {
                diagram = (Diagram) e.getSource();
                Point pt = e.getPoint();
                Figure figure = diagram.findFigure(pt);
                if (!(figure instanceof CompositionLink)) {
                    reset();
                    return;
                }
                DiagramModel model = diagram.getModel();
                if (model == null) {
                    reset();
                    return;
                }
                link = (CompositionLink) figure;
                if (link.pointFor(pt.x, pt.y, LinkShappingTool.CLICK_TOLERANCE * 2.0) != -1) {
                    reset();
                    return;
                }
                e.consume();
                fireToolStarted();
                item = (CompositionItem) model.getValue(link);
                if (item == null) {
                    item = new CompositionItem();
                    model.setValue(link, item);
                }
                startEditing(pt);
            }
        }
    }

    protected void startEditing(Point pt) {
        popup.show(pt);
    }

    protected void stopEditing(String n) {
        item.setCardinality(n);
        DiagramUI ui = (DiagramUI) diagram.getUI();
        ui.refreshFigure(link);
    }

    /**
   * @class Popup
   */
    protected class Popup extends JPopupMenu {

        protected JTextField text = new JTextField();

        protected int n;

        public Popup() {
            super("Cardinality");
            JLabel lbl = new JLabel("Cardinality");
            lbl.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            add(lbl);
            add(text);
            lbl.setFont(getFont().deriveFont(Font.PLAIN, getFont().getSize() - 1));
            setDefaultLightWeightPopupEnabled(true);
            Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(1, 1, 1, 1));
            border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), border);
            text.setBorder(border);
        }

        public void show(Point pt) {
            text.setText(item.getCardinality());
            super.show(diagram, pt.x, pt.y);
        }

        protected void firePopupMenuWillBecomeInvisible() {
            try {
                stopEditing(text.getText());
            } catch (Throwable t) {
            }
            fireToolFinished();
            reset();
        }
    }

    protected void reset() {
        diagram = null;
        item = null;
        link = null;
    }
}
