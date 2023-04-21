package ho.tool.injury;

import ho.core.gui.comp.panel.ImagePanel;
import ho.core.model.HOVerwaltung;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Dialog for Injury Calculator
 *
 * @author draghetto
 */
public class InjuryDialog extends JDialog implements WindowListener {

    private static final long serialVersionUID = 5194730460165995230L;

    /** TODO Missing Parameter Documentation */
    DoctorPanel doctorPanel;

    /** TODO Missing Parameter Documentation */
    UpdatePanel updatePanel;

    /** TODO Missing Parameter Documentation */
    UpdateTSIPanel tsiPanel;

    private InjuryDetailPanel detail = new InjuryDetailPanel();

    /**
     * Creates a new KeeperToolDialog object.
     *
     * @param owner the main HO Frame
     */
    public InjuryDialog(JFrame owner) {
        super(owner, false);
        setTitle(HOVerwaltung.instance().getLanguageString("InjuryCalculator"));
        initComponents();
        setSize(700, 300);
        reload();
        addWindowListener(this);
    }

    /**
     * Returns the Detail Panel for use of calculator
     *
     * @return the Detail Panel
     */
    public final InjuryDetailPanel getDetail() {
        return detail;
    }

    /**
     * Method that force a reload of the dialog
     */
    public final void reload() {
        detail.reload();
        doctorPanel.reset();
        updatePanel.reset();
        tsiPanel.reset();
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public void windowActivated(java.awt.event.WindowEvent windowEvent) {
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public final void windowClosed(java.awt.event.WindowEvent windowEvent) {
        setVisible(false);
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public final void windowClosing(java.awt.event.WindowEvent windowEvent) {
        setVisible(false);
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public void windowDeactivated(java.awt.event.WindowEvent windowEvent) {
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public void windowDeiconified(java.awt.event.WindowEvent windowEvent) {
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public void windowIconified(java.awt.event.WindowEvent windowEvent) {
    }

    /**
     * React to Window Event
     *
     * @param windowEvent event
     */
    public void windowOpened(java.awt.event.WindowEvent windowEvent) {
    }

    /**
     * Initialize the GUI components
     */
    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(detail, BorderLayout.CENTER);
        final JPanel p = new ImagePanel();
        p.setLayout(new GridLayout(3, 1));
        doctorPanel = new DoctorPanel(this);
        p.add(doctorPanel);
        updatePanel = new UpdatePanel(this);
        p.add(updatePanel);
        tsiPanel = new UpdateTSIPanel(this);
        p.add(tsiPanel);
        getContentPane().add(p, BorderLayout.SOUTH);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        Dimension screenSize = getParent().getSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(getParent().getX() + x, getParent().getY() + y);
    }
}
