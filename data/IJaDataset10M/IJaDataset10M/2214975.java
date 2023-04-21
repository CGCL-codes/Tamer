package org.plazmaforge.framework.client.swing.dialogs;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import org.plazmaforge.framework.client.swing.gui.GUIUtilities;
import org.plazmaforge.framework.client.swing.gui.StatusBar;
import org.plazmaforge.framework.resources.LibraryResources;
import org.plazmaforge.framework.resources.Resources;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Oleh Hapon
 * Date: 13.11.2003
 * Time: 8:54:27
 */
public class OptionDialog extends JDialog {

    protected JPanel dialogPanel;

    protected JPanel buttonPanel;

    protected JButton okButton;

    protected JButton cancelButton;

    protected StatusBar statusBar;

    private int option = JOptionPane.CANCEL_OPTION;

    private boolean pack = true;

    private boolean center = true;

    private boolean cancelOnEsc = true;

    protected Resources resources;

    private boolean isInit = false;

    public OptionDialog() throws HeadlessException {
    }

    public OptionDialog(Frame owner) throws HeadlessException {
        super(owner);
    }

    public OptionDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public OptionDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public OptionDialog(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public OptionDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public OptionDialog(Dialog owner) throws HeadlessException {
        super(owner);
    }

    public OptionDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public OptionDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public OptionDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public OptionDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
    }

    protected void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        dialogPanel = createDialogPanel();
        buttonPanel = createButtonPanel();
        statusBar = createStatusBar();
        JPanel panel = new JPanel(new BorderLayout());
        if (dialogPanel != null) {
            panel.add(dialogPanel, BorderLayout.CENTER);
        }
        if (buttonPanel != null) {
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
        c.add(panel, BorderLayout.CENTER);
        if (statusBar != null) {
            c.add(statusBar, BorderLayout.SOUTH);
        }
        cancelOnEsc = true;
        registerKeyESC();
        setResizable(false);
        setSize(400, 400);
    }

    protected void doOkAction() {
        close();
    }

    protected void doCancelAction() {
        close();
    }

    protected void close() {
        int operation = getDefaultCloseOperation();
        if (operation == DISPOSE_ON_CLOSE) dispose(); else if (operation == HIDE_ON_CLOSE) hide();
    }

    public int showDialog() {
        boolean isModal = isModal();
        setModal(true);
        show();
        setModal(isModal);
        return option;
    }

    public final void init() {
        if (!isInit) {
            initComponents();
            isInit = true;
        }
    }

    public void show() {
        init();
        if (isPack()) {
            pack();
        }
        if (isCenter()) {
            setLocationRelativeTo(getOwner());
        }
        option = JOptionPane.CANCEL_OPTION;
        super.show();
    }

    public int getOption() {
        return option;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    protected void registerKeyESC() {
        getRootPane().registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (cancelOnEsc) {
                    fireCancelAction();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public boolean isCancelOnEsc() {
        return cancelOnEsc;
    }

    public void setCancelOnEsc(boolean cancelOnEsc) {
        this.cancelOnEsc = cancelOnEsc;
    }

    protected JPanel createDialogPanel() {
        JPanel p = new JPanel();
        Border border = createDialogPanelBorder();
        if (border != null) {
            p.setBorder(border);
        }
        return p;
    }

    protected Border createDialogPanelBorder() {
        return createRaisedBorder();
    }

    protected JButton createOkButton() {
        return new JButton(getResourceString("message.ok"));
    }

    protected JButton createCancelButton() {
        return new JButton(getResourceString("message.cancel"));
    }

    protected JPanel createButtonPanel() {
        JPanel p = new JPanel();
        p.setBorder(createButtonPanelBorder());
        okButton = createOkButton();
        cancelButton = createCancelButton();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireOkAction();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireCancelAction();
            }
        });
        p.add(okButton);
        p.add(cancelButton);
        GUIUtilities.setJButtonSizesTheSame(new JButton[] { okButton, cancelButton });
        return p;
    }

    protected Border createButtonPanelBorder() {
        return createRaisedBorder();
    }

    protected StatusBar createStatusBar() {
        return new StatusBar();
    }

    protected Border createRaisedBorder() {
        return BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEmptyBorder(0, 4, 0, 4));
    }

    public Resources getResources() {
        if (resources == null) {
            resources = LibraryResources.getInstance();
        }
        return resources;
    }

    public String getResourceString(String key) {
        return getResources().getString(key);
    }

    public String getString(String key) {
        return getResources().getString(this.getClass().getName() + '.' + key);
    }

    protected void fireOkAction() {
        option = JOptionPane.OK_OPTION;
        doOkAction();
    }

    protected void fireCancelAction() {
        option = JOptionPane.CANCEL_OPTION;
        doCancelAction();
    }
}
