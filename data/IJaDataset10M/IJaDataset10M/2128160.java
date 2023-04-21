package views.widgets;

import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JMenuBar;

/**
 * Common buttons displayed on top of the widgets
 * 
 * @author root
 *
 */
public class WidgetMenu extends JMenuBar {

    private static final long serialVersionUID = -8238416227458765242L;

    private static final int NB_BUTTONS = 2;

    private static final int CLOSE = 0;

    private static final int INSPECT = 1;

    private static JButton[] menuButtons = new JButton[NB_BUTTONS];

    private static ActionListener[] menuListeners = new ActionListener[NB_BUTTONS];

    protected MOWidget parent;

    /**
	 * Constructor
	 */
    public WidgetMenu(MOWidget mow) {
        parent = mow;
        menuButtons[0] = new JButton("Close");
        menuButtons[1] = new JButton("Inspect");
        ActionListener basicListener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.out.println("clic sur " + ((JButton) arg0.getSource()).getText());
            }
        };
        ActionListener closeListener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (parent.getContainer() != null) parent.getContainer().removeWidget();
            }
        };
        ActionListener zoomListener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                parent.toggleZoom();
            }
        };
        ActionListener inspectListener = new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                parent.showInspector(parent);
            }
        };
        menuListeners[0] = closeListener;
        menuListeners[1] = inspectListener;
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (int i = 0; i < NB_BUTTONS; i++) {
            this.add(menuButtons[i]);
            menuButtons[i].addActionListener(menuListeners[i]);
            menuButtons[i].addActionListener(basicListener);
        }
    }

    /**
	 * Redefines the drawing method
	 * @param g The component's Graphics
	 */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        setOpaque(false);
        super.paintComponent(g2d);
        g2d.setPaint(oldPaint);
    }
}
