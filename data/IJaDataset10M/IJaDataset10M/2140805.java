package tr.view.project.chooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.project.Project;

/**
 * Custom dialog box to choose a project.
 */
public class ProjectChooserDialog extends JDialog implements FocusListener, ActionListener {

    private static final Class CLASS = ProjectChooserDialog.class;

    private static final String TITLE = NbBundle.getMessage(CLASS, "dialog.title");

    private ProjectsPanel panel;

    private JButton okayButton;

    private JButton cancelButton;

    private boolean okay;

    private boolean cancel;

    private static class FocusablePanel extends JPanel {

        /**
         * Constructs a new <code>FocusablePanel</code> with the given layout
         * manager.
         * @param layout The layout manager
         */
        public FocusablePanel(LayoutManager layout) {
            super(layout);
        }

        /**
         * Always returns <code>true</code>, since
         * <code>FocusablePanel</code> can receive the focus.
         * @return <code>true</code>
         */
        @Override
        public boolean isFocusable() {
            return true;
        }
    }

    private void construct() {
        ActionListener cancelListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancel = true;
                setVisible(false);
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        okayButton = new JButton(NbBundle.getMessage(ProjectChooserDialog.class, "dialog.okay"));
        okayButton.addActionListener(this);
        getRootPane().setDefaultButton(okayButton);
        cancelButton = new JButton(NbBundle.getMessage(ProjectChooserDialog.class, "dialog.cancel"));
        cancelButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel();
        if (Utilities.isWindows()) {
            buttonsPanel.add(okayButton);
            buttonsPanel.add(cancelButton);
        } else {
            buttonsPanel.add(cancelButton);
            buttonsPanel.add(okayButton);
        }
        panel = new ProjectsPanel();
        panel.addActionListener(this);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        JPanel fillBottomPanel = new JPanel();
        fillBottomPanel.setPreferredSize(new Dimension(0, 32));
        JPanel fillWestPanel = new JPanel();
        fillWestPanel.setPreferredSize(new Dimension(32, 0));
        JPanel fillEastPanel = new JPanel();
        fillEastPanel.setPreferredSize(new Dimension(32, 0));
        Container dialog = getContentPane();
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(fillBottomPanel, BorderLayout.SOUTH);
        dialog.add(fillWestPanel, BorderLayout.WEST);
        dialog.add(fillEastPanel, BorderLayout.EAST);
        pack();
        setResizable(true);
    }

    /**
     * Called when the "Ok" button is pressed. Just sets a flag and hides the
     * dialog box.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okayButton || e.getSource() == panel) {
            okay = true;
        } else if (e.getSource() == cancelButton) {
            cancel = true;
        }
        setVisible(false);
    }

    /**
     * Called when the calendar gains the focus. Just re-sets the selected day
     * so that it is redrawn with the border that indicate focus.
     */
    public void focusGained(FocusEvent e) {
    }

    /**
     * Called when the calendar loses the focus. Just re-sets the selected day
     * so that it is redrawn without the border that indicate focus.
     */
    public void focusLost(FocusEvent e) {
    }

    public ProjectChooserDialog(Frame owner) {
        this(owner, TITLE);
    }

    public ProjectChooserDialog(Frame owner, String title) {
        super(owner, title, true);
        construct();
        initialise();
    }

    public ProjectChooserDialog(Dialog owner) {
        this(owner, TITLE);
    }

    public ProjectChooserDialog(Dialog owner, String title) {
        super(owner, title, true);
        construct();
        initialise();
    }

    private void initialise() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /**
     * Selects a project.
     */
    public Project select(Project project, Component component) {
        okay = false;
        cancel = false;
        if (component != null) {
            setLocationRelativeTo(component);
        }
        panel.refreshModel();
        panel.setSelected(project);
        setVisible(true);
        if (!okay) {
            return null;
        }
        return panel.getSelected();
    }

    /**
     * Determines whether the cancel button was activated.
     * @return true iff the cancel button was activated.
     */
    public boolean cancelled() {
        return cancel;
    }

    /**
     * Determines whether the ok button was activated.
     * @return true iff the ok button was activated.
     */
    public boolean okayed() {
        return okay;
    }
}
