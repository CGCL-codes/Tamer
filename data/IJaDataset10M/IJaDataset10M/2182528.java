package org.jlense.uiworks.preference;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlense.uiworks.dialogs.IDialogConstants;
import org.jlense.uiworks.dialogs.IDialogSettings;
import org.jlense.uiworks.forms.IFormPage;
import org.jlense.uiworks.forms.IFormPageContainer;
import org.jlense.uiworks.internal.window.WorkbenchDialog;
import org.jlense.uiworks.resource.ImageRegistry;
import org.jlense.uiworks.resource.JFaceResources;
import org.jlense.uiworks.widget.BusyIndicator;
import org.jlense.uiworks.widget.ButtonBar;
import org.jlense.uiworks.widget.JLTreeCellRenderer;
import org.jlense.uiworks.widget.WidgetUtils;
import org.jlense.uiworks.workbench.IWorkbenchPart;
import org.jlense.uiworks.workbench.IWorkbenchWindow;
import org.jlense.util.Assert;
import org.jlense.util.SystemUtils;

/**
 * A preference dialog is a hierarchical presentation of preference
 * pages.  Each page is represented by a node in the tree shown
 * on the left hand side of the dialog; when a node is selected, the
 * corresponding page is shown on the right hand side.
 */
public class PreferenceDialog extends WorkbenchDialog implements IFormPageContainer {

    /**
     * Title area fields
     */
    public static final String PREF_DLG_TITLE_IMG = "preference_dialog_title_image";

    public static final String PREF_DLG_IMG_TITLE_ERROR = "preference_dialog_title_error_image";

    static {
        ImageRegistry reg = JFaceResources.getImageRegistry();
        reg.put(PREF_DLG_TITLE_IMG, SystemUtils.createImage(PreferenceDialog.class, "images/pref_dialog_title.gif"));
        reg.put(PREF_DLG_IMG_TITLE_ERROR, SystemUtils.createImage(PreferenceDialog.class, "images/title_error.gif"));
    }

    private static final Color ERROR_BACKGROUND_RGB = new Color(230, 226, 221);

    private int rc = JOptionPane.OK_OPTION;

    private JComponent titleArea;

    private JLabel messageLabel;

    private JLabel titleImage;

    private Color titleAreaColor;

    private String message;

    private Color normalMsgAreaBackground;

    private Color errorMsgAreaBackground;

    private Image errorMsgImage;

    /**
     * Preference store, initially <code>null</code> meaning none.
     *
     * @see #setPreferenceStore
     */
    private IPreferenceStore preferenceStore;

    /**
     * The current preference page, or <code>null</code> if
     * there is none.
     */
    private IFormPage currentPage;

    /**
     * The preference manager.
     */
    private PreferenceManager preferenceManager;

    /**
     * The main control for this dialog.
     */
    private JComponent body;

    /**
     * The JComponent in which a page is shown.
     */
    private JComponent pageContainer;

    /**
     * The minimum page size; 400 by 400 by default.
     *
     * @see #setMinimumPageSize
     */
    private Point minimumPageSize = new Point(400, 400);

    /**
     * The OK button.
     */
    private JButton okButton;

    /**
     * The Cancel button.
     */
    private JButton cancelButton;

    /**
     * The Help button; <code>null</code> if none.
     */
    private JButton helpButton = null;

    /**
     * Indicates whether help is available; <code>false</code> by default.'
     *
     * @see #setHelpAvailable
     */
    private boolean isHelpAvailable = false;

    /**
     * The tree control.
     */
    private JTree tree;

    /**
     * The current tree item.
     */
    private TreePath currentTreePath;

    /**
     * The window that owns this dialog
     */
    private IWorkbenchWindow _parentWindow;

    /**
     * Creates a new preference dialog under the control of the given preference 
     * manager.
     *
     * @param shell the parent shell
     * @param manager the preference manager
     */
    public PreferenceDialog(IWorkbenchWindow parentWindow, PreferenceManager manager) {
        super(parentWindow);
        _parentWindow = parentWindow;
        preferenceManager = manager;
        setTitle(JFaceResources.getString("PreferenceDialog.title"));
        setResizable(false);
        setModal(true);
        createContents();
        pack();
        Dimension oldSize = this.getSize();
        Dimension newSize = new Dimension(oldSize);
        if (oldSize.width < 200) newSize.width = 200;
        if (oldSize.height < 300) newSize.height = 300;
        if (!oldSize.equals(newSize)) setSize(newSize);
    }

    public int getReturnCode() {
        return rc;
    }

    public void setReturnCode(int returnCode) {
        rc = returnCode;
    }

    protected void cancelPressed() {
        Iterator nodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER).iterator();
        while (nodes.hasNext()) {
            IPreferenceNode node = (IPreferenceNode) nodes.next();
            if (node.getPage() != null) {
                if (!node.getPage().performCancel()) return;
            }
        }
        setReturnCode(JOptionPane.CANCEL_OPTION);
        setVisible(false);
    }

    protected JComponent createButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        okButton = new JButton(IDialogConstants.OK_LABEL);
        okButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okPressed();
            }
        });
        buttonBar.add(okButton);
        cancelButton = new JButton(IDialogConstants.CANCEL_LABEL);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelPressed();
            }
        });
        buttonBar.add(cancelButton);
        if (isHelpAvailable) {
            helpButton = new JButton(IDialogConstants.HELP_LABEL);
            helpButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    helpPressed();
                }
            });
            buttonBar.add(helpButton);
        }
        return buttonBar;
    }

    protected void createContents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.add(panel, WidgetUtils.createGridBagConstraints(GridBagConstraints.BOTH));
        JComponent dialogArea = createDialogArea();
        panel.add(dialogArea, WidgetUtils.createGridBagConstraints(0, 0, GridBagConstraints.BOTH));
        panel.add(new JSeparator(JSeparator.HORIZONTAL), WidgetUtils.createGridBagConstraints(0, 1, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0)));
        JComponent buttonBar = createButtonBar();
        panel.add(buttonBar, WidgetUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST));
        selectFirstItem();
    }

    protected JComponent createDialogArea() {
        JPanel dialogArea = new JPanel(new GridBagLayout());
        GridBagConstraints dialogGBC = new GridBagConstraints();
        dialogGBC.gridx = dialogGBC.gridy = 0;
        dialogGBC.weighty = 1.0;
        dialogGBC.fill = GridBagConstraints.VERTICAL;
        pageContainer = new JPanel(new GridBagLayout());
        pageContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JComponent tree = createTree();
        tree.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        dialogArea.add(tree, dialogGBC);
        JComponent page = new JPanel(new GridBagLayout());
        {
            GridBagConstraints pageGBC = new GridBagConstraints();
            pageGBC.gridx = pageGBC.gridy = 0;
            pageGBC.weightx = 1.0;
            pageGBC.fill = GridBagConstraints.HORIZONTAL;
            JComponent titleComposite = createTitleArea();
            page.add(titleComposite, pageGBC);
            pageGBC.gridy++;
            page.add(new JSeparator(JSeparator.HORIZONTAL), pageGBC);
            pageGBC.gridy++;
            pageGBC.weighty = pageGBC.weightx = 1.0;
            pageGBC.fill = GridBagConstraints.BOTH;
            pageGBC.anchor = GridBagConstraints.NORTHWEST;
            page.add(pageContainer, pageGBC);
        }
        dialogGBC.weighty = dialogGBC.weightx = 1.0;
        dialogGBC.fill = GridBagConstraints.BOTH;
        dialogGBC.insets = new Insets(0, 5, 0, 0);
        dialogGBC.gridx++;
        dialogArea.add(page, dialogGBC);
        return dialogArea;
    }

    /**
     * Creates the wizard's title area.
     *
     * @param parent the SWT parent for the title area composite
     * @return the created title area composite
     */
    private JComponent createTitleArea() {
        titleArea = new JPanel(new GridBagLayout());
        titleArea.setBackground(SystemColor.window);
        titleArea.setOpaque(true);
        JPanel spacer1 = new JPanel();
        spacer1.setOpaque(false);
        titleArea.add(spacer1, WidgetUtils.createGridBagConstraints(0, 0));
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalTextPosition(messageLabel.LEFT);
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD));
        titleArea.add(messageLabel, WidgetUtils.createGridBagConstraints(1, 0));
        JPanel spacer2 = new JPanel();
        spacer2.setOpaque(false);
        titleArea.add(spacer2, WidgetUtils.createGridBagConstraints(2, 0, GridBagConstraints.HORIZONTAL));
        Icon icon = JFaceResources.getIcon(PREF_DLG_TITLE_IMG);
        if (icon != null) titleArea.add(new JLabel(icon), WidgetUtils.createGridBagConstraints(3, 0));
        return titleArea;
    }

    /**
     * Creates a Tree/TreePath structure that reflects the page hierarchy.
     */
    private JTree createTree() {
        tree = new JTree();
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(final TreeSelectionEvent event) {
                BusyIndicator.showWhile(PreferenceDialog.this, new Runnable() {

                    public void run() {
                        PreferenceTreeNode treeNode = (PreferenceTreeNode) event.getPath().getLastPathComponent();
                        IPreferenceNode node = (IPreferenceNode) treeNode.getUserObject();
                        if (!isCurrentPageValid()) {
                            showPageFlippingAbortDialog();
                            selectCurrentPageAgain();
                        } else {
                            if (!showPage(node)) {
                                showPageFlippingAbortDialog();
                                selectCurrentPageAgain();
                            } else {
                                currentTreePath = event.getPath();
                            }
                        }
                    }
                });
            }
        });
        PreferenceTreeNode root = new PreferenceTreeNode(preferenceManager.getRoot());
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);
        tree.setRootVisible(false);
        tree.setCellRenderer(new PreferenceRenderer());
        tree.setShowsRootHandles(true);
        return tree;
    }

    /**
     * Returns the preference mananger used by this preference dialog.
     *
     * @return the preference mananger
     */
    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public IDialogSettings getDialogSettings() {
        return preferenceStore;
    }

    /**
     * Notifies that the user has pressed the Save button and no page has vetoed.
     * <p>
     * The default implementation of this framework method does nothing.
     * Subclasses should override to save the preferences.
     * </p>
     */
    protected void handleSave() {
    }

    /**
     * Notifies that the window's close button was pressed, 
     * the close menu was selected, or the ESCAPE key pressed.
     * <p>
     * The default implementation of this framework method
     * sets the window's return code to <code>CANCEL</code>
     * and closes the window using <code>close</code>.
     * Subclasses may extend or reimplement.
     * </p>
     */
    protected void handleShellCloseEvent() {
        cancelPressed();
    }

    /**
     * Notifies of the pressing of the Help button.
     * <p>
     * The default implementation of this framework method
     * calls <code>performHelp</code> on the currently active page.
     * </p>
     */
    protected void helpPressed() {
        if (currentPage != null) {
            currentPage.performHelp();
        }
    }

    /**
     * Returns whether the current page is valid.
     *
     * @return <code>false</code> if the current page is not valid, or
     *  or <code>true</code> if the current page is valid or there is
     *  no current page
    */
    protected boolean isCurrentPageValid() {
        if (currentPage == null) return true; else return currentPage.isValid();
    }

    /**
     * The preference dialog implementation of this <code>Dialog</code>
     * framework method sends <code>performOk</code> to all pages of the 
     * preference dialog, then calls <code>handleSave</code> on this dialog
     * to save any state, and then calls <code>hardClose</code> to close
     * this dialog.
     */
    protected void okPressed() {
        Iterator nodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER).iterator();
        while (nodes.hasNext()) {
            IPreferenceNode node = (IPreferenceNode) nodes.next();
            if (node.getPage() != null) {
                if (!node.getPage().performOk()) return;
            }
        }
        handleSave();
        hide();
    }

    /**
     * Selects the page determined by <code>currentTreePath</code> in
     * the page hierarchy.
     */
    private void selectCurrentPageAgain() {
        tree.setSelectionPath(currentTreePath);
        currentPage.setVisible(true);
    }

    /**
     * Selects the first item in the tree of preference pages.
     */
    protected void selectFirstItem() {
        if (tree != null) {
            TreePath path = tree.getPathForRow(0);
            tree.setSelectionPath(path);
        }
    }

    /**
     * Display the given error message. The currently displayed message
     * is saved and will be redisplayed when the error message is set
     * to <code>null</code>.
     *
     * @param errorMessage the errorMessage to display or <code>null</code>
     */
    public void setErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            if (messageLabel.getIcon() != null) {
                messageLabel.setBackground(normalMsgAreaBackground);
                messageLabel.setIcon(null);
                titleImage.setIcon(new ImageIcon(JFaceResources.getImage(PREF_DLG_TITLE_IMG)));
                titleArea.revalidate();
            }
            setMessage(message);
        } else {
            messageLabel.setText(errorMessage);
            if (messageLabel.getIcon() == null) {
                if (errorMsgAreaBackground == null) {
                    errorMsgAreaBackground = SystemColor.window;
                    errorMsgImage = JFaceResources.getImage(PREF_DLG_IMG_TITLE_ERROR);
                }
                normalMsgAreaBackground = messageLabel.getBackground();
                messageLabel.setBackground(errorMsgAreaBackground);
                messageLabel.setIcon(new ImageIcon(errorMsgImage));
                titleImage.setIcon(null);
                titleArea.revalidate();
            }
        }
    }

    /**
     * Sets whether a Help button is available for this dialog.
     * <p>
     * Clients must call this framework method before the dialog's control
     * has been created.
     * <p>
     *
     * @param b <code>true</code> to include a Help button, 
     *  and <code>false</code> to not include one (the default)
     */
    public void setHelpAvailable(boolean b) {
        isHelpAvailable = b;
    }

    /**
     * Set the message text. If the message line currently displays an error,
     * the message is stored and will be shown after a call to clearErrorMessage
     */
    public void setMessage(String newMessage) {
        message = newMessage;
        if (message == null) message = "";
        if (messageLabel.getIcon() == null) messageLabel.setText(message);
    }

    /**
     * Sets the minimum page size.
     *
     * @param minWidth the minimum page width
     * @param minHeight the minimum page height
     */
    public void setMinimumPageSize(int minWidth, int minHeight) {
        minimumPageSize.x = minWidth;
        minimumPageSize.y = minHeight;
    }

    /**
     * Sets the minimum page size.
     *
     * @param size the page size encoded as
     *   <code>new Point(width,height)</code>
     * @see #setMinimumPageSize(int,int)
     */
    public void setMinimumPageSize(Point size) {
        minimumPageSize.x = size.x;
        minimumPageSize.y = size.y;
    }

    /**
     * Sets the preference store for this preference dialog.
     *
     * @param store the preference store
     * @see #getPreferenceStore
     */
    public void setPreferenceStore(IPreferenceStore store) {
        Assert.isNotNull(store);
        preferenceStore = store;
    }

    /**
     * Shows the preference page corresponding to the given preference node.
     * Does nothing if that page is already current.
     *
     * @param node the preference node, or <code>null</code> if none
     * @return <code>true</code> if the page flip was successful, and
     * <code>false</code> is unsuccessful
     */
    protected boolean showPage(IPreferenceNode node) {
        if (node == null) return false;
        if (node.getPage() == null) node.createPage();
        if (node.getPage() == null) return false;
        IFormPage newPage = node.getPage();
        if (newPage == currentPage) return true;
        if (currentPage != null) {
            if (!currentPage.okToLeave()) return false;
        }
        ;
        currentPage = newPage;
        if (currentPage.getControl() == null) currentPage.createControl();
        pageContainer.removeAll();
        pageContainer.add(currentPage.getControl(), WidgetUtils.createGridBagConstraints(GridBagConstraints.BOTH));
        currentPage.setVisible(true);
        update();
        return true;
    }

    /**
     * Shows the "Page Flipping abort" dialog.
     */
    private void showPageFlippingAbortDialog() {
        JOptionPane.showInputDialog(this, JFaceResources.getString("AbortPageFlippingDialog.message"), JFaceResources.getString("AbortPageFlippingDialog.title"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Updates this dialog's controls to reflect the current page.
     */
    protected void update() {
        updateTitle();
        updateMessage();
        updateButtons();
        pageContainer.revalidate();
        pageContainer.repaint();
    }

    public void updateButtons() {
        okButton.setEnabled(isCurrentPageValid());
    }

    public void updateMessage() {
        String pageMessage = currentPage.getMessage();
        String pageErrorMessage = currentPage.getErrorMessage();
        if (pageMessage == null) {
            setMessage(currentPage.getTitle());
        } else {
            setMessage(pageMessage);
        }
        setErrorMessage(pageErrorMessage);
    }

    public void updateTitle() {
        updateMessage();
    }

    /**
     * @see org.jlense.uiworks.forms.IFormPageContainer#getWorkbenchPart()
     */
    public IWorkbenchPart getWorkbenchPart() {
        return getActivePage().getActivePart();
    }
}

class PreferenceTreeNode extends DefaultMutableTreeNode {

    IPreferenceNode _preferenceNode;

    public PreferenceTreeNode(IPreferenceNode preferenceNode) {
        super(preferenceNode);
        _preferenceNode = preferenceNode;
        IPreferenceNode[] children = preferenceNode.getSubNodes();
        if (children != null && 0 < children.length) {
            for (int counter = 0; counter < children.length; counter++) {
                PreferenceTreeNode child = new PreferenceTreeNode(children[counter]);
                add(child);
            }
        }
    }
}

class PreferenceRenderer extends JLTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        IPreferenceNode node = (IPreferenceNode) ((PreferenceTreeNode) value)._preferenceNode;
        setText(node.getLabelText());
        setIcon(node.getLabelIcon());
        return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }
}
