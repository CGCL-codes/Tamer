package org.apptools.ui.swing.utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.apptools.PlatformTexts;

/**
 * A component for displaying several components behind each other. Similar 
 * to ListMultiComponentDialog but using a tabbed pane layout instead. 
 * 
 * @author Johan Stenberg
 */
public class TabMultiComponentDialog extends JDialog implements ActionListener {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    /**OK button*/
    protected JButton okButton;

    /**TabPane*/
    protected JTabbedPane tabPane;

    /**panel for buttons*/
    protected JPanel controlPane;

    /**panel for any icon supplied*/
    protected JPanel iconPane;

    /**
   * Create a new Dialog.
   * @param owner The owning frame
   * @param title The title string to display
   * @param comps The components to show in the tabbed pane
   * @param icon An icon (or null) to show beside the tab pane.
   * @throws HeadlessException
   */
    public TabMultiComponentDialog(Frame owner, String title, Component[] comps, Icon icon) throws HeadlessException {
        super(owner, title, true);
        iconPane = new JPanel(new BorderLayout());
        tabPane = new JTabbedPane();
        tabPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        okButton = new JButton(PlatformTexts.getString("Common.CAPTION_OK"));
        okButton.addActionListener(this);
        controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.LINE_AXIS));
        controlPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPane.add(Box.createHorizontalGlue());
        controlPane.add(okButton);
        getContentPane().add(controlPane, BorderLayout.PAGE_END);
        getContentPane().add(tabPane, BorderLayout.CENTER);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconPane.add(iconLabel);
            iconPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        getContentPane().add(iconPane, BorderLayout.LINE_START);
        setupComponents(comps);
        if (tabPane.getComponentCount() > 0) tabPane.setSelectedComponent(tabPane.getComponent(0));
        pack();
        setLocation(GUIUtils.suggestLocation(this, owner));
    }

    /**
   * Setup the components of this dialog. Called by the constructor.
   * @param comps The components to add to the tabbed pane
   */
    protected void setupComponents(Component[] comps) {
        if (comps != null) for (int i = 0; i < comps.length; i++) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
            p.add(comps[i]);
            tabPane.add(comps[i].getName(), p);
        }
    }

    /**Dispose dialog if OK pressed*/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            dispose();
        }
    }
}
