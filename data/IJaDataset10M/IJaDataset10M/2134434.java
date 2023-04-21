package jhomenet.ui.panel;

import java.util.List;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.TransitionTarget;
import org.jdesktop.animation.transitions.Effect;
import org.jdesktop.animation.transitions.EffectsManager;
import org.jdesktop.animation.transitions.effects.FadeIn;
import jhomenet.ui.window.IWindowListener;
import jhomenet.ui.window.DefaultWindowEvent;
import jhomenet.ui.wrapper.WindowWrapper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

/**
 * A class that can be used to manage all of the panels that are added to a potential
 * window wrapper.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public final class PanelGroupManager implements IWindowListener {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(PanelGroupManager.class.getName());

    /**
	 * Default actions.
	 */
    protected final AbstractAction closeAction = new CloseWindowAction("Close");

    protected final AbstractAction cancelAction = new CloseWindowAction("Cancel");

    /**
	 * List of panels.
	 */
    private final List<CustomPanel> panelList = new ArrayList<CustomPanel>();

    /**
	 * List of available buttons.
	 */
    private final List<JButton> groupButtons = new ArrayList<JButton>();

    /**
	 * Reference to the window wrapper object.
	 */
    protected WindowWrapper windowWrapper;

    /**
	 * Flag whether to allow window resizing.
	 */
    private Boolean allowResized = false;

    /**
	 * 
	 */
    private Dimension getPreferredDimension;

    private int lastIndex = -1;

    private int currentIndex = -1;

    /**
	 * Constructor.
	 */
    public PanelGroupManager() {
        super();
    }

    public final void updatePanels() {
        for (CustomPanel p : this.panelList) p.repaint();
    }

    /**
	 * Add a panel to the panel manager.
	 * 
	 * @param panel The panel to add to the panel manager
	 */
    public final void addPanel(CustomPanel panel) {
        panel.setPanelGroupManager(this);
        panelList.add(panel);
    }

    /**
	 * Build the panel manager's main panel. Depending on the number of panels
	 * added to the panel manager using the <code>addPanel</code> method determines
	 * whether a single panel is shown or a tabbed panel.
	 * 
	 * @see #addPanel(CustomPanel)
	 * @return A <code>JComponent</code> that contains all of the added panels
	 */
    public final JComponent buildMainPanel() {
        logger.debug("Building panel manager components");
        if (panelList.size() == 0) {
            logger.debug("Panel model list size = 0...building blank panel");
            return new JXPanel();
        } else if (panelList.size() == 1) {
            logger.debug("Panel model list size = 1...building single panel");
            return panelList.get(0).buildPanel();
        } else {
            logger.debug("Panel model list size > 1...building tabbed pane");
            JTabbedPane tabbedPane = new JTabbedPane();
            for (CustomPanel panel : panelList) {
                tabbedPane.addTab(panel.getPanelName(), panel.buildPanel());
            }
            tabbedPane.setSelectedIndex(0);
            tabbedPane.updateUI();
            tabbedPane.repaint();
            return tabbedPane;
        }
    }

    private void initTransitionAnimation(final JTabbedPane tabbedPane) {
        final TransitionTarget target = new TransitionTarget() {

            @Override
            public void setupNextScreen() {
                tabbedPane.setSelectedIndex(currentIndex);
            }
        };
        final Animator animator = new Animator(500);
        final ScreenTransition transition = new ScreenTransition(tabbedPane, target, animator);
        tabbedPane.addChangeListener(new ChangeListener() {

            /**
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 */
            @Override
            public void stateChanged(ChangeEvent e) {
                lastIndex = currentIndex;
                currentIndex = tabbedPane.getSelectedIndex();
                if (lastIndex == -1) setupBackgroundAndEffect(tabbedPane);
                transition.start();
            }
        });
    }

    private void setupBackgroundAndEffect(JComponent c) {
        FadeIn fader = new FadeIn();
        EffectsManager.setEffect(c, fader, EffectsManager.TransitionType.CHANGING);
    }

    /**
	 * Build the desired button bar for the panel. If no buttons have been added to the
	 * panel manager, then a single "Close" button will be added by default. However, if
	 * one or more additional buttons have been added, then these buttons along with a
	 * "Cancel" button will also be added.
	 * <p>
	 * NOTE: The button bar created in this method will contain the necessary padding.
	 * 
	 * @return The button bar panel
	 */
    public final JComponent buildButtonPanel() {
        int numButtons = 0;
        if (groupButtons != null) numButtons = groupButtons.size();
        FormLayout panelLayout = new FormLayout("4dlu, fill:default:grow, 4dlu", "pref, 4dlu, pref, 4dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        builder.addSeparator("", cc.xy(2, 1));
        if (numButtons == 0) {
            builder.add(ButtonBarFactory.buildRightAlignedBar(new JButton(closeAction)), cc.xy(2, 3));
        } else {
            JButton[] tmp = new JButton[numButtons + 1];
            for (int i = 0; i < groupButtons.size(); i++) tmp[i] = groupButtons.get(i);
            tmp[groupButtons.size()] = new JButton(cancelAction);
            builder.add(ButtonBarFactory.buildRightAlignedBar(tmp), cc.xy(2, 3));
        }
        return builder.getPanel();
    }

    /**
	 * Get the list of buttons.
	 * 
	 * @return
	 */
    public final List<JButton> getGroupButtons() {
        return groupButtons;
    }

    /**
	 * Add a button to the button list.
	 * 
	 * @param button The button to add
	 * @return
	 */
    public final JButton addGroupButton(JButton button) {
        this.groupButtons.add(button);
        return button;
    }

    /**
	 * Add an action to the button list.
	 * 
	 * @param action The action to add
	 * @return
	 */
    public final JButton addGroupButtonAction(AbstractAction action) {
        JButton button = new JButton(action);
        return addGroupButton(button);
    }

    /**
	 * 
	 * @param buttonList
	 */
    public final List<JButton> addButtons(List<JButton> buttonList) {
        if (buttonList != null) {
            for (JButton button : buttonList) {
                addGroupButton(button);
            }
        }
        return buttonList;
    }

    /**
	 * Set the panel manager's window wrapper. 
	 * 
	 * @param windowWrapper The panel manager's window wrapper
	 */
    public void setWindowWrapper(WindowWrapper windowWrapper) {
        if (windowWrapper == null) throw new IllegalArgumentException("Window wrapper cannot be null!");
        this.windowWrapper = windowWrapper;
    }

    /**
	 * Get the referenced window wrapper. 
	 *  
	 * @return The referenced window wrapper
	 */
    public final WindowWrapper getWindowWrapper() {
        return this.windowWrapper;
    }

    /**
	 * 
	 * @param visible
	 */
    public void setVisible(boolean visible) {
        logger.debug("Setting " + panelList.size() + " visibility to " + visible);
        for (CustomPanel panelModel : panelList) {
            panelModel.setVisible(true);
        }
    }

    /**
	 * Send a window dispose request to the window wrapper. This is usually called
	 * by panel children requesting that the parent window close.
	 */
    public final void requestWindowDispose() {
        this.windowWrapper.requestWindowDispose();
    }

    /**
	 * This method is called when the parent window wrapper is actually closing.
	 * It gives child panels an opportunity to do any last minute clean up before
	 * the parent window is closed. Long running tasks should not be run from
	 * this method as this method is run on the Event Dispatch Thread (EDT) and
	 * could hang the window otherwise.
	 * 
	 * @see jhomenet.ui.window.IWindowListener#windowClosing(jhomenet.ui.window.DefaultWindowEvent)
	 */
    public void windowClosing(final DefaultWindowEvent event) {
        for (CustomPanel panel : panelList) {
            panel.parentWindowIsClosing(event);
        }
    }

    /**
	 * @return the allowResized
	 */
    public final Boolean getAllowResized() {
        return allowResized;
    }

    /**
	 * @param allowResized the allowResized to set
	 */
    public final void setAllowResized(Boolean allowResized) {
        this.allowResized = allowResized;
    }

    /**
	 * Close window action class.
	 */
    private final class CloseWindowAction extends AbstractAction {

        private CloseWindowAction(String text) {
            putValue(Action.NAME, text);
        }

        /**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent actionEvent) {
            requestWindowDispose();
        }
    }

    /**
	 * Dummy action class.
	 */
    class DummyAction extends AbstractAction {

        private DummyAction() {
            putValue(Action.NAME, "Dummy");
        }

        /**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent actionEvent) {
            logger.debug("Dummy action");
        }
    }
}
