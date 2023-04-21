package com.nullfish.app.jfd2.command.embed.attribute;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.resource.JFDResource;
import com.nullfish.lib.ui.OneKeyButton;
import com.nullfish.lib.ui.OneKeyRadioButton;
import com.nullfish.lib.ui.TagPanel;
import com.nullfish.lib.ui.UIUtilities;
import com.nullfish.lib.ui.permissionpanel.PermissionPanel;
import com.nullfish.lib.ui.tristate_checkbox.TristateState;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.permission.FileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

public class AttributeDialog extends JDialog {

    private JFD jfd;

    private PermissionPanel permissionPanel = new PermissionPanel();

    private TagPanel tagPanel;

    private JButton okButton = new OneKeyButton(JFDResource.LABELS.getString("ok"), KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));

    private JButton cancelButton = new OneKeyButton(JFDResource.LABELS.getString("cancel"), KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));

    private JPanel mainPanel = new JPanel(new GridBagLayout());

    private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    private JFormattedTextField timestampText = new JFormattedTextField(new SimpleDateFormat("yyyyMMdd HH:mm:ss"));

    private OneKeyRadioButton permissionRadio = new OneKeyRadioButton();

    private OneKeyRadioButton timestampRadio = new OneKeyRadioButton();

    private OneKeyRadioButton tagRadio = new OneKeyRadioButton();

    private ButtonGroup buttonGroup = new ButtonGroup();

    private JPanel permissionContainer = new JPanel(new BorderLayout());

    private JPanel timestampContainer = new JPanel(new BorderLayout());

    private JPanel tagContainer = new JPanel(new BorderLayout());

    private boolean multiFileMode = false;

    private boolean okPressed = false;

    public AttributeDialog(JFD jfd) {
        super((Window) UIUtilities.getTopLevelOwner((Container) jfd));
        setModal(true);
        this.jfd = jfd;
        tagPanel = new TagPanel(jfd);
        init();
    }

    public AttributeDialog(Dialog owner, JFD jfd) {
        super(owner, true);
        this.jfd = jfd;
        init();
    }

    private void init() {
        setTitle("jFD2 - Attribute");
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonGroup.add(permissionRadio);
        buttonGroup.add(timestampRadio);
        buttonGroup.add(tagRadio);
        permissionContainer.setBorder(BorderFactory.createTitledBorder(JFDResource.LABELS.getString("permission")));
        permissionContainer.add(permissionRadio, BorderLayout.WEST);
        permissionContainer.add(permissionPanel, BorderLayout.CENTER);
        timestampContainer.setBorder(BorderFactory.createTitledBorder(JFDResource.LABELS.getString("timestamp")));
        timestampContainer.add(timestampRadio, BorderLayout.WEST);
        timestampContainer.add(timestampText, BorderLayout.CENTER);
        tagContainer.setBorder(BorderFactory.createTitledBorder(JFDResource.LABELS.getString("tag")));
        tagContainer.add(tagRadio, BorderLayout.WEST);
        tagContainer.add(tagPanel, BorderLayout.CENTER);
        tagPanel.addComponentListener(new ComponentListener() {

            public void componentHidden(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentResized(ComponentEvent e) {
                pack();
            }

            public void componentShown(ComponentEvent e) {
            }
        });
        setContentPane(mainPanel);
        mainPanel.add(permissionContainer, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 10, 10));
        mainPanel.add(timestampContainer, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 10, 10));
        mainPanel.add(tagContainer, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 10, 10));
        mainPanel.add(buttonsPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        mainPanel.getActionMap().put("close", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        mainPanel.getActionMap().put("focus_next", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                FocusManager.getCurrentManager().focusNextComponent();
            }
        });
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "focus_next");
        permissionRadio.setText("(P)");
        permissionRadio.setMnemonic('p');
        timestampRadio.setText("(T)");
        timestampRadio.setMnemonic('t');
        tagRadio.setText("(G)");
        tagRadio.setMnemonic('g');
        permissionRadio.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateAbility();
            }
        });
        timestampRadio.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateAbility();
            }
        });
        tagRadio.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateAbility();
            }
        });
        permissionRadio.setSelected(true);
    }

    private void updateAbility() {
        permissionPanel.setEnabled(!multiFileMode || permissionRadio.isSelected());
        timestampText.setEnabled(!multiFileMode || timestampRadio.isSelected());
        tagPanel.setEnabled(!multiFileMode || tagRadio.isSelected());
    }

    public void init(VFile[] files) throws VFSException {
        List filesList = new ArrayList();
        for (int i = 0; i < files.length; i++) {
            filesList.add(files[i]);
        }
        permissionPanel.init(filesList);
        tagPanel.init(filesList);
        multiFileMode = files.length > 1;
        if (!multiFileMode) {
            permissionRadio.setEnabled(false);
            permissionRadio.setVisible(false);
            timestampRadio.setEnabled(false);
            timestampRadio.setVisible(false);
            tagRadio.setEnabled(false);
            tagRadio.setVisible(false);
        }
        timestampText.setValue(files[0].getTimestamp());
        updateAbility();
    }

    public Date getTimestamp() {
        return (Date) timestampText.getValue();
    }

    public TristateState getState(PermissionType type, FileAccess access) {
        return permissionPanel.getState(type, access);
    }

    public List getTags() {
        return tagPanel.getTags();
    }

    public TristateState getTagState(String tag) {
        return tagPanel.getState(tag);
    }

    public boolean isEditingPermission() {
        return permissionRadio.isSelected();
    }

    public boolean isEditingTimestamp() {
        return timestampRadio.isSelected();
    }

    public boolean isEditingTag() {
        return tagRadio.isSelected();
    }

    public boolean isMultiFileMode() {
        return multiFileMode;
    }

    public boolean isOkPressed() {
        return okPressed;
    }
}
