package net.sf.xpontus.view.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.*;

/**
 * <code>JFileChooser</code> provides a simple mechanism for the user to
 * choose a file.
 * For information about using <code>JFileChooser</code>, see
 * <a
 * href="http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html">How to Use File Choosers</a>,
 * a section in <em>The Java Tutorial</em>.
 *
 * <p>
 *
 * The following code pops up a file chooser for the user's home directory that
 * sees only .jpg and .gif images:
 * <pre>
 *    JFileChooser chooser = new JFileChooser();
 *    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
 *    // under the demo/jfc directory in the JDK.
 *    ExampleFileFilter filter = new ExampleFileFilter();
 *    filter.addExtension("jpg");
 *    filter.addExtension("gif");
 *    filter.setDescription("JPG & GIF Images");
 *    chooser.setFileFilter(filter);
 *    int returnVal = chooser.showOpenDialog(parent);
 *    if(returnVal == JFileChooser.APPROVE_OPTION) {
 *       System.out.println("You chose to open this file: " +
 *            chooser.getSelectedFile().getName());
 *    }
 * </pre>
 *
 * @beaninfo
 *   attribute: isContainer false
 * description: A component which allows for the interactive selection of a file.
 *
 * @version 1.106 06/28/04
 * @author Jeff Dinkins
 *
 */
public class JFontChooser extends JComponent {

    /**
     * Type value indicating that the <code>JFileChooser</code> supports an
     * "Open" file operation.
     */
    public static final int OPEN_DIALOG = 0;

    /**
     * Return value if cancel is chosen.
     */
    public static final int CANCEL_OPTION = 1;

    /**
     * Return value if approve (yes, ok) is chosen.
     */
    public static final int APPROVE_OPTION = 0;

    /** Instruction to cancel the current selection. */
    public static final String CANCEL_SELECTION = "CancelSelection";

    /**
     * Instruction to approve the current selection
     * (same as pressing yes or ok).
     */
    public static final String APPROVE_SELECTION = "ApproveSelection";

    /** Identifies change in the text on the approve (yes, ok) button. */
    public static final String APPROVE_BUTTON_TEXT_CHANGED_PROPERTY = "ApproveButtonTextChangedProperty";

    /**
     * Identifies change in the tooltip text for the approve (yes, ok)
     * button.
     */
    public static final String APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY = "ApproveButtonToolTipTextChangedProperty";

    /** Identifies change in the mnemonic for the approve (yes, ok) button. */
    public static final String APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY = "ApproveButtonMnemonicChangedProperty";

    /** Instruction to display the control buttons. */
    public static final String CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY = "ControlButtonsAreShownChangedProperty";

    /** Identifies user's directory change. */
    public static final String FONT_CHANGED_PROPERTY = "fontChanged";

    /** Identifies change in user's single-file selection. */
    public static final String SELECTED_FILE_CHANGED_PROPERTY = "SelectedFileChangedProperty";

    /** Identifies change in user's multiple-file selection. */
    public static final String SELECTED_FILES_CHANGED_PROPERTY = "SelectedFilesChangedProperty";

    /** Enables multiple-file selections. */
    public static final String MULTI_SELECTION_ENABLED_CHANGED_PROPERTY = "MultiSelectionEnabledChangedProperty";

    /**
     * Says that a different accessory component is in use
     * (for example, to preview files).
     */
    public static final String ACCESSORY_CHANGED_PROPERTY = "AccessoryChangedProperty";

    /**
     * Identifies whether a the AcceptAllFileFilter is used or not.
     */
    public static final String ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY = "acceptAllFileFilterUsedChanged";

    /** Identifies a change in the dialog title. */
    public static final String DIALOG_TITLE_CHANGED_PROPERTY = "DialogTitleChangedProperty";

    /**
     * Identifies a change in the type of files displayed (files only,
     * directories only, or both files and directories).
     */
    public static final String DIALOG_TYPE_CHANGED_PROPERTY = "DialogTypeChangedProperty";

    /**
     * Identifies a change in the list of predefined file filters
     * the user can choose from.
     */
    public static final String CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY = "ChoosableFileFilterChangedProperty";

    private JComponent previewPanel;

    private String dialogTitle = null;

    private String approveButtonText = null;

    private String approveButtonToolTipText = null;

    private int approveButtonMnemonic = 0;

    private java.awt.Font selectedFont;

    private ActionListener actionListener = null;

    private Vector filters = new Vector(5);

    private JDialog dialog = null;

    private int dialogType = OPEN_DIALOG;

    private int returnValue = 0;

    private JComponent accessory = null;

    private boolean controlsShown = true;

    private PropertyChangeListener showFilesListener = null;

    private int fileSelectionMode = 0;

    private boolean multiSelectionEnabled = false;

    private boolean useAcceptAllFileFilter = true;

    private boolean dragEnabled = false;

    private FileFilter fileFilter = null;

    private FileSystemView fileSystemView = null;

    private File currentDirectory = null;

    private File selectedFile = null;

    private File[] selectedFiles;

    /**
     * Constructs a <code>JFileChooser</code> pointing to the user's
     * default directory. This default depends on the operating system.
     * It is typically the "My Documents" folder on Windows, and the
     * user's home directory on Unix.
     */
    public JFontChooser() {
        this((java.awt.Font) UIManager.get("TextArea.font"));
    }

    public JFontChooser(String fontName, int fontStyle, int fontSize) {
        this(new java.awt.Font(fontName, fontStyle, fontSize));
    }

    /**
     * Constructs a <code>JFileChooser</code> using the given path.
     * Passing in a <code>null</code>
     * string causes the file chooser to point to the user's default directory.
     * This default depends on the operating system. It is
     * typically the "My Documents" folder on Windows, and the user's
     * home directory on Unix.
     *
     * @param currentDirectoryPath  a <code>String</code> giving the path
     *                                to a file or directory
     */
    public JFontChooser(java.awt.Font selectedFont) {
        setSelectedFont(selectedFont);
    }

    public static Font showDialog(Component component, String title, java.awt.Font initialFont) throws HeadlessException {
        final JFontChooser pane = new JFontChooser((initialFont != null) ? initialFont : null);
        FontTracker ok = new FontTracker(pane);
        JDialog dialog = createDialog(component, title, true, pane, ok, null);
        dialog.setVisible(true);
        return ok.getFont();
    }

    public static JDialog createDialog(Component c, String title, boolean modal, JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) throws HeadlessException {
        Window window = null;
        final FontChooserDialog dialog = null;
        if (window instanceof Frame) {
        } else {
        }
        return dialog;
    }

    public java.awt.Font getSelectedFont() {
        return selectedFont;
    }

    public void setSelectedFont(java.awt.Font selectedFont) {
        this.selectedFont = selectedFont;
    }

    public void setSelectedFont(String fontName, int fontStyle, int fontSize) {
        setSelectedFont(new java.awt.Font(fontName, fontStyle, fontSize));
    }

    public void setPreviewPanel(javax.swing.JPanel preview) {
        if (previewPanel != preview) {
            JComponent oldPreview = previewPanel;
            previewPanel = preview;
            firePropertyChange(JColorChooser.PREVIEW_PANEL_PROPERTY, oldPreview, preview);
        }
    }

    public javax.swing.JComponent getPreviewPanel() {
        return this.previewPanel;
    }

    public void setFontNamesList(String[] fontNames) {
    }

    public void setFontSizeList(int[] fontSizes) {
    }

    class FontChooserDialog extends javax.swing.JDialog {

        private Font initialFont;

        private JFontChooser chooserPane;

        private JButton cancelButton;

        public FontChooserDialog(Dialog owner, String title, boolean modal, Component c, JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) throws HeadlessException {
            super(owner, title, modal);
            initFontChooserDialog(c, chooserPane, okListener, cancelListener);
        }

        public FontChooserDialog(Frame owner, String title, boolean modal, Component c, JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) throws HeadlessException {
            super(owner, title, modal);
            initFontChooserDialog(c, chooserPane, okListener, cancelListener);
        }

        protected void initFontChooserDialog(Component c, JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) {
        }
    }
}

class FontTracker implements ActionListener, ItemListener, java.io.Serializable {

    private JFontChooser chooser;

    private java.awt.Font font;

    public FontTracker(JFontChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        font = chooser.getSelectedFont();
    }

    public void itemStateChanged(ItemEvent e) {
    }

    public java.awt.Font getFont() {
        return this.font;
    }
}
