package org.columba.core.gui.profiles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.CheckBoxWithMnemonic;
import org.columba.core.gui.base.SingleSideEtchedBorder;
import org.columba.core.help.HelpManager;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.core.xml.XmlElement;

/**
 * Profile chooser dialog.
 * <p>
 * User can choose a profile from a list. Add a new profile or edit and existing
 * profiles's properties.
 * <p>
 * Additionally, the user can choose to hide this dialog on next startup.
 *
 * @author fdietz
 */
public class ProfileChooserDialog extends JDialog implements ActionListener, ListSelectionListener, MouseListener {

    private static final String RESOURCE_PATH = "org.columba.core.i18n.dialog";

    protected JButton okButton;

    protected JButton cancelButton;

    protected JButton helpButton;

    protected JButton addButton;

    protected JButton editButton;

    private DefaultListModel model;

    protected JList list;

    protected String selection;

    protected JLabel nameLabel;

    protected JCheckBox checkBox;

    public ProfileChooserDialog() throws HeadlessException {
        super((JFrame) null, GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "chooser.title"), true);
        initComponents();
        layoutComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel topBorderPanel = new JPanel();
        topBorderPanel.setLayout(new BorderLayout());
        topBorderPanel.add(topPanel);
        topPanel.add(nameLabel);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(Box.createHorizontalGlue());
        Component glue = Box.createVerticalGlue();
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBagLayout.setConstraints(glue, c);
        gridBagLayout = new GridBagLayout();
        c = new GridBagConstraints();
        JPanel eastPanel = new JPanel(gridBagLayout);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBagLayout.setConstraints(addButton, c);
        eastPanel.add(addButton);
        Component strut1 = Box.createRigidArea(new Dimension(30, 5));
        gridBagLayout.setConstraints(strut1, c);
        eastPanel.add(strut1);
        gridBagLayout.setConstraints(editButton, c);
        eastPanel.add(editButton);
        glue = Box.createVerticalGlue();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        gridBagLayout.setConstraints(glue, c);
        eastPanel.add(glue);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        scrollPane.getViewport().setBackground(Color.white);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(checkBox, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 6, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(helpButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    protected void initComponents() {
        addButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "add"));
        addButton.setActionCommand("ADD");
        addButton.addActionListener(this);
        addButton.setEnabled(false);
        editButton = new ButtonWithMnemonic(GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "edit"));
        editButton.setActionCommand("EDIT");
        editButton.addActionListener(this);
        editButton.setEnabled(false);
        nameLabel = new JLabel(GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "label"));
        checkBox = new CheckBoxWithMnemonic(GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "dont_ask"));
        okButton = new ButtonWithMnemonic(GlobalResourceLoader.getString("", "", "ok"));
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        cancelButton = new ButtonWithMnemonic(GlobalResourceLoader.getString("", "", "cancel"));
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        helpButton = new ButtonWithMnemonic(GlobalResourceLoader.getString("", "", "help"));
        HelpManager.getInstance().enableHelpOnButton(helpButton, "extending_columba_2");
        HelpManager.getInstance().enableHelpKey(getRootPane(), "extending_columba_2");
        XmlElement profiles = ProfileManager.getInstance().getProfiles();
        model = new DefaultListModel();
        model.addElement("Default");
        for (int i = 0; i < profiles.count(); i++) {
            XmlElement p = profiles.getElement(i);
            String name = p.getAttribute("name");
            model.addElement(name);
        }
        list = new JList(model);
        list.addListSelectionListener(this);
        list.addMouseListener(this);
        String selected = ProfileManager.getInstance().getSelectedProfile();
        if (selected != null) {
            list.setSelectedValue(selected, true);
        }
        getRootPane().setDefaultButton(okButton);
        getRootPane().registerKeyboardAction(this, "CANCEL", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("OK")) {
            setVisible(false);
        } else if (action.equals("CANCEL")) {
            System.exit(0);
        } else if (action.equals("ADD")) {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setFileHidingEnabled(false);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File location = fc.getSelectedFile();
                Profile p = new Profile(location.getName(), location);
                ProfileManager.getInstance().addProfile(p);
                model.addElement(p.getName());
                list.setSelectedValue(p.getName(), true);
            }
        } else if (action.equals("EDIT")) {
            String inputValue = JOptionPane.showInputDialog(GlobalResourceLoader.getString(RESOURCE_PATH, "profiles", "enter_name"), selection);
            if (inputValue == null) {
                return;
            }
            ProfileManager.getInstance().renameProfile(selection, inputValue);
            model.setElementAt(inputValue, model.indexOf(selection));
            selection = inputValue;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        boolean enabled = !list.isSelectionEmpty();
        addButton.setEnabled(enabled);
        okButton.setEnabled(enabled);
        selection = (String) list.getSelectedValue();
        if ((selection != null) && (!selection.equals("Default"))) {
            editButton.setEnabled(true);
        } else {
            editButton.setEnabled(false);
        }
    }

    /**
     * @return The selection.
     */
    public String getSelection() {
        return selection;
    }

    public boolean isDontAskedSelected() {
        return checkBox.isSelected();
    }

    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getClickCount() >= 2) {
            actionPerformed(new ActionEvent(list, 0, "OK"));
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
}
