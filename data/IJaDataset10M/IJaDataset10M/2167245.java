package fr.ign.cogit.geoxygene.appli.mode;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.apache.log4j.Logger;
import fr.ign.cogit.geoxygene.I18N;
import fr.ign.cogit.geoxygene.appli.LayerViewPanel;
import fr.ign.cogit.geoxygene.appli.MainFrame;
import fr.ign.cogit.geoxygene.appli.ProjectFrame;

/**
 * @author Julien Perret
 */
public class ModeSelector implements ContainerListener, KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {

    /**
   * Logger.
   */
    static final Logger LOGGER = Logger.getLogger(ModeSelector.class.getName());

    /**
   * List of modes.
   */
    private List<Mode> modes = new ArrayList<Mode>();

    /**
   * The current mode.
   */
    private Mode currentMode = null;

    public Mode getCurrentMode() {
        return this.currentMode;
    }

    /**
   * The toolbar.
   */
    private JToolBar toolBar = new JToolBar(I18N.getString("ModeSelector.ModeSelection"));

    /**
   * Get the toolbar.
   * @return the toolbar
   */
    public final JToolBar getToolBar() {
        return this.toolBar;
    }

    /**
   * Associated main frame.
   */
    private MainFrame mainFrame;

    /**
   * @return the main frame
   */
    public final MainFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
   * Set the main frame.
   * @param frame the new main frame
   */
    public final void setMainFrame(final MainFrame frame) {
        this.mainFrame = frame;
    }

    /**
   * Constructor.
   * @param theMainFrame associated main frame
   */
    public ModeSelector(final MainFrame theMainFrame) {
        this.setMainFrame(theMainFrame);
        this.getMainFrame().add(this.toolBar, BorderLayout.PAGE_START);
        this.modes.add(new ZoomMode(this.getMainFrame(), this));
        this.modes.add(new ZoomBoxMode(this.getMainFrame(), this));
        this.modes.add(new MoveMode(this.getMainFrame(), this));
        this.modes.add(new SelectionMode(this.getMainFrame(), this));
        this.toolBar.addSeparator();
        JButton zoomToFullExtentButton = new JButton(new ImageIcon(ModeSelector.class.getResource("/images/icons/16x16/zoomToFullExtent.png")));
        zoomToFullExtentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                ProjectFrame projectFrame = ModeSelector.this.getMainFrame().getSelectedProjectFrame();
                if (projectFrame != null) {
                    try {
                        projectFrame.getLayerViewPanel().getViewport().zoomToFullExtent();
                    } catch (NoninvertibleTransformException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        zoomToFullExtentButton.setToolTipText(I18N.getString("ModeSelector.zoomToFullExtent.ToolTip"));
        this.toolBar.add(zoomToFullExtentButton);
        this.toolBar.addSeparator();
        JButton refreshButton = new JButton(new ImageIcon(ModeSelector.class.getResource("/images/icons/16x16/refresh.png")));
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                ProjectFrame projectFrame = ModeSelector.this.getMainFrame().getSelectedProjectFrame();
                if (projectFrame != null) {
                    projectFrame.getLayerViewPanel().repaint();
                }
            }
        });
        refreshButton.setToolTipText(I18N.getString("ModeSelector.refresh.ToolTip"));
        this.toolBar.add(refreshButton);
        this.toolBar.addSeparator();
        final JToggleButton showGeometryToolsButton = new JToggleButton(new ImageIcon(ModeSelector.class.getResource("/images/icons/16x16/edit.png")));
        showGeometryToolsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                ProjectFrame projectFrame = ModeSelector.this.getMainFrame().getSelectedProjectFrame();
                if (projectFrame != null) {
                    if (!showGeometryToolsButton.isSelected()) {
                        projectFrame.setGeometryToolsVisible(false);
                    } else {
                        projectFrame.setGeometryToolsVisible(true);
                    }
                }
            }
        });
        showGeometryToolsButton.setToolTipText(I18N.getString("ModeSelector.showGeometryTools.ToolTip"));
        this.toolBar.add(showGeometryToolsButton);
        this.setCurrentMode(this.modes.get(0));
    }

    @Override
    public final void keyPressed(final KeyEvent e) {
        this.currentMode.keyPressed(e);
    }

    @Override
    public final void keyReleased(final KeyEvent e) {
        this.currentMode.keyReleased(e);
    }

    @Override
    public final void keyTyped(final KeyEvent e) {
        this.currentMode.keyTyped(e);
    }

    @Override
    public final void mouseClicked(final MouseEvent e) {
        this.currentMode.mouseClicked(e);
    }

    @Override
    public final void mouseEntered(final MouseEvent e) {
        this.currentMode.mouseEntered(e);
    }

    @Override
    public final void mouseExited(final MouseEvent e) {
        this.currentMode.mouseExited(e);
    }

    @Override
    public final void mousePressed(final MouseEvent e) {
        this.currentMode.mousePressed(e);
    }

    @Override
    public final void mouseReleased(final MouseEvent e) {
        this.currentMode.mouseReleased(e);
    }

    @Override
    public final void mouseWheelMoved(final MouseWheelEvent e) {
        this.currentMode.mouseWheelMoved(e);
    }

    @Override
    public final void mouseDragged(final MouseEvent e) {
        this.currentMode.mouseDragged(e);
    }

    @Override
    public final void mouseMoved(final MouseEvent e) {
        this.currentMode.mouseMoved(e);
    }

    public final List<Mode> getRegisteredModes() {
        return this.modes;
    }

    /**
   * Set the current mode.
   * @param mode the new current mode.
   */
    public final void setCurrentMode(final Mode mode) {
        if (this.currentMode != null) {
            this.currentMode.getButton().setEnabled(true);
        }
        this.currentMode = mode;
        this.currentMode.getButton().setEnabled(false);
        if (this.getMainFrame() != null && this.getMainFrame().getSelectedProjectFrame() != null) {
            LayerViewPanel layerViewPanel = this.getMainFrame().getSelectedProjectFrame().getLayerViewPanel();
            layerViewPanel.setCursor(this.currentMode.getCursor());
        }
        this.currentMode.activated();
    }

    @Override
    public final void componentAdded(final ContainerEvent e) {
        if (ProjectFrame.class.isAssignableFrom(e.getChild().getClass())) {
            this.addComponent(((ProjectFrame) e.getChild()).getLayerViewPanel());
        }
    }

    /**
   * Add a component.
   * @param component the newly added component
   */
    private void addComponent(final Component component) {
        if (component instanceof AbstractButton) {
            return;
        }
        component.addKeyListener(this);
        component.addMouseWheelListener(this);
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public final void componentRemoved(final ContainerEvent e) {
    }
}
