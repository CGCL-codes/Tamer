package webOffline;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Do Viet Trung
 */
public class About implements MouseListener {

    public static boolean RIGHT_TO_LEFT = false;

    private JFrame frame;

    private boolean allowHideDlg = false;

    public static void addComponentsToPane(Container pane) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
        ImageIcon icon = new ImageIcon(WebOffline.imagesPath + "about.png");
        JLabel label = new JLabel(icon);
        pane.add(label, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        frame.setVisible(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void closeDiaglog() {
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createAndShowGUI(String title, boolean allowHideDlg) {
        this.allowHideDlg = allowHideDlg;
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(frame, ec.getMessage());
        }
        frame = new JFrame(title);
        frame.addMouseListener(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(470, 250));
        addComponentsToPane(frame.getContentPane());
        if (allowHideDlg) {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WebOffline.imagesPath + "infoIcon.png"));
        } else {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WebOffline.imagesPath + "mainIcon.png"));
        }
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
