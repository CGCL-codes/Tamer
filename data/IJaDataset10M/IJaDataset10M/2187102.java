package com.genia.toolbox.basics.editor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.rmi.server.UID;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import com.genia.toolbox.basics.editor.AbstractController;
import com.genia.toolbox.basics.editor.exception.InitialisationException;
import com.genia.toolbox.basics.editor.gui.dialog.FileChoosers;
import com.genia.toolbox.basics.editor.gui.panel.AbstractSettings;
import com.genia.toolbox.basics.editor.gui.view.internal.AbstractDocumentDesktop;
import com.genia.toolbox.basics.editor.message.AbstractMessages;
import com.genia.toolbox.basics.editor.model.bean.InitialSettings;
import com.genia.toolbox.basics.editor.model.document.Document;
import com.genia.toolbox.basics.editor.model.document.Element;
import com.genia.toolbox.basics.exception.BundledException;

/**
 * The basic gui main frame.
 * 
 * @param <DD>
 *          The generic document desktop.
 * @param <DS>
 *          The generic settings panel.
 * @param <C>
 *          The generic controller.
 * @param <D>
 *          The generic document.
 * @param <E>
 *          The generic element.
 * @param <I>
 *          The generic initial settings.
 */
@SuppressWarnings("serial")
public abstract class AbstractGUI<DD extends AbstractDocumentDesktop<?, ?, D, E>, DS extends AbstractSettings<?, D, E>, C extends AbstractController<?, ?, D, E, I>, D extends Document<E>, E extends Element<?>, I extends InitialSettings> extends JFrame {

    /**
   * The text standard color.
   */
    public static final Color COLOR_TEXT = SystemColor.textText;

    /**
   * The text error color.
   */
    public static final Color COLOR_TEXT_ERROR = Color.RED;

    /**
   * The panel enabled color.
   */
    public static final Color COLOR_PANEL = new Color(216, 213, 196);

    /**
   * The panel disabled color.
   */
    public static final Color COLOR_PANEL_ERROR = SystemColor.control;

    /**
   * The valid border color.
   */
    public static final Color COLOR_BORDER = Color.GREEN;

    /**
   * The error border color.
   */
    public static final Color COLOR_BORDER_ERROR = Color.RED;

    /**
   * The border width.
   */
    public static final int BORDER_WIDTH = 3;

    /**
   * The tool bar separator width.
   */
    public static final int TOOLBAR_SEPARATOR_WIDTH = 20;

    /**
   * The controller.
   */
    private C controller = null;

    /**
   * The menu bar.
   */
    private AbstractMenuBar<?, C, D, E, I> menubar = null;

    /**
   * The tool bar.
   */
    private AbstractToolBar<?, C, D, E, I> toolbar = null;

    /**
   * The desktop panel.
   */
    private DD desktopPanel = null;

    /**
   * The settings panel.
   */
    private DS settingsPanel = null;

    /**
   * The splitpane.
   */
    private JSplitPane splitPane = null;

    /**
   * Constructor.
   */
    public AbstractGUI() {
        super();
    }

    /**
   * Initialize the frame.
   * 
   * @throws BundledException
   *           if there are other errors.
   */
    public void initialise() throws BundledException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            throw new InitialisationException();
        }
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                AbstractGUI.this.getController().notifyCloseApplication();
            }
        });
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                JFrame tmp = (JFrame) e.getSource();
                if (tmp.getWidth() < 800 || tmp.getHeight() < 600) {
                    tmp.setSize(800, 600);
                }
            }
        });
        this.setTitle(this.getController().notifyTranslation(AbstractMessages.BASIC_MAIN_FRAME_TITLE));
        URL url = this.getClass().getClassLoader().getResource("com/genia/toolbox/basics/editor/image/icon.gif");
        this.setIconImage(new ImageIcon(url).getImage());
        this.setMenubar(this.createMenuBar());
        this.setJMenuBar(this.getMenubar());
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        this.desktopPanel = this.createDesktopPanel();
        this.settingsPanel = this.createSettingsPanel();
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.desktopPanel, this.settingsPanel);
        this.splitPane.setOneTouchExpandable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int width = dim.getSize().width;
        this.splitPane.setDividerLocation(width - 30 * width / 100);
        this.setToolbar(this.createToolBar());
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 0, 0);
        this.getContentPane().add(this.getToolbar(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 5, 5, 5);
        this.getContentPane().add(this.splitPane, constraints);
        this.refresh();
    }

    /**
   * Refresh the tool bar and the menu.
   */
    public void refresh() {
        this.menubar.refresh();
        this.toolbar.refresh();
    }

    /**
   * Get the frame.
   * 
   * @return the frame.
   */
    public JFrame getFrame() {
        return this;
    }

    /**
   * Ask a question to the user.
   * 
   * @param title
   *          the title.
   * @param message
   *          the message.
   * @return the answer.
   */
    public boolean ask(String title, String message) {
        boolean answer = false;
        if (JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            answer = true;
        }
        return answer;
    }

    /**
   * Tell a message to the user.
   * 
   * @param title
   *          the title.
   * @param message
   *          the message.
   */
    public void tell(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
   * Warn the user.
   * 
   * @param title
   *          the title.
   * @param message
   *          the message.
   */
    public void warn(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * Get the menu bar.
   * 
   * @return the menu bar.
   */
    public AbstractMenuBar<?, C, D, E, I> getMenubar() {
        return this.menubar;
    }

    /**
   * Set the menu bar.
   * 
   * @param menubar
   *          the menu bar.
   */
    public void setMenubar(AbstractMenuBar<?, C, D, E, I> menubar) {
        this.menubar = menubar;
    }

    /**
   * Get the tool bar.
   * 
   * @return the tool bar.
   */
    public AbstractToolBar<?, C, D, E, I> getToolbar() {
        return this.toolbar;
    }

    /**
   * Set the tool bar.
   * 
   * @param toolbar
   *          the tool bar.
   */
    public void setToolbar(AbstractToolBar<?, C, D, E, I> toolbar) {
        this.toolbar = toolbar;
    }

    /**
   * Get the controller.
   * 
   * @return the controller.
   */
    public C getController() {
        return this.controller;
    }

    /**
   * Set the controller.
   * 
   * @param controller
   *          the controller.
   */
    public void setController(C controller) {
        this.controller = controller;
    }

    /**
   * Select an XML file for saving purpose.
   * 
   * @return an XML file.
   */
    public File saveXMLFile() {
        return FileChoosers.getInstance(this).saveToXMLFile();
    }

    /**
   * Select an XML file for opening purpose.
   * 
   * @return an XML file.
   */
    public File openXMLFile() {
        return FileChoosers.getInstance(this).openToXMLFile();
    }

    /**
   * Display a document.
   * 
   * @param document
   *          the document.
   */
    public void displayDocument(D document) {
        this.getDesktopPanel().deactivateAll();
        this.getDesktopPanel().display(document);
        this.getSettingsPanel().setDocument(document);
        this.refresh();
    }

    /**
   * Display the document for the first time.
   * 
   * @param document
   *          the document.
   */
    public void firstDisplayDocument(D document) {
        this.getDesktopPanel().firstDisplay(document);
        this.getSettingsPanel().setDocument(document);
        this.refresh();
    }

    /**
   * Hide a document.
   * 
   * @param documentUniqueID
   *          the document unique ID.
   */
    public void hideDocument(UID documentUniqueID) {
        this.getDesktopPanel().close(documentUniqueID);
    }

    /**
   * Get the document.
   * 
   * @param documentUniqueID
   *          the document unique ID.
   * @return the document.
   */
    @SuppressWarnings("unchecked")
    public D getDocument(UID documentUniqueID) {
        return (D) this.getDesktopPanel().getDocument(documentUniqueID);
    }

    /**
   * Get the selected document.
   * 
   * @return the selected document.
   */
    public D getSelectedDocument() {
        return this.getDesktopPanel().getSelectedDocument();
    }

    /**
   * Get all the documents unique IDs.
   * 
   * @return the documents unique IDs.
   */
    public List<UID> getAllDocumentsUniqueID() {
        return this.getDesktopPanel().getAllDocumentsUniqueID();
    }

    /**
   * Create the menubar.
   * 
   * @return the menubar.
   */
    public abstract AbstractMenuBar<?, C, D, E, I> createMenuBar();

    /**
   * Create the toolbar.
   * 
   * @return the toolbar.
   */
    public abstract AbstractToolBar<?, C, D, E, I> createToolBar();

    /**
   * Create the desktop panel.
   * 
   * @return the desktop panel.
   */
    public abstract DD createDesktopPanel();

    /**
   * Create the settings panel.
   * 
   * @return the settings panel.
   */
    public abstract DS createSettingsPanel();

    /**
   * Get the desktop panel.
   * 
   * @return the desktop panel.
   */
    public DD getDesktopPanel() {
        return this.desktopPanel;
    }

    /**
   * Get the settings panel.
   * 
   * @return the settings panel.
   */
    public DS getSettingsPanel() {
        return this.settingsPanel;
    }

    /**
   * Create a new document.
   * 
   * @return the initial settings.
   */
    public abstract I createElement();

    /**
   * Create a new sub-element.
   * 
   * @return the initial settings.
   */
    public abstract I createSubElement();

    /**
   * Notify that the document settings have to be saved.
   */
    public void notifyDocumentSettingsHasToBeSaved() {
        this.getDesktopPanel().updateTitle();
        this.refresh();
    }

    /**
   * Notify that the document settings have changed.
   * 
   * @param document
   *          the updated document.
   */
    public void notifyDocumentSettingsChanged(D document) {
        this.getDesktopPanel().updateDocument(document);
        this.refresh();
    }

    /**
   * Notify that the element settings have changed.
   * 
   * @param element
   *          the updated element.
   */
    public void notifyElementSettingsChanged(E element) {
        this.getDesktopPanel().updateElement(element);
        this.refresh();
    }

    /**
   * Display the current document settings.
   */
    public void displayCurrentDocumentSettings() {
        D document = this.getSelectedDocument();
        this.getSettingsPanel().setDocument(document);
        this.refresh();
    }

    /**
   * Display the current element settings.
   * 
   * @param element
   *          the element model.
   */
    public void displayElementSettings(E element) {
        this.getSettingsPanel().setElement(element);
        this.refresh();
    }

    /**
   * Display the sub-element settings.
   * 
   * @param element
   *          the sub-element model.
   */
    public void displaySubElement(E element) {
        this.getDesktopPanel().display(element);
        this.refresh();
    }

    /**
   * Get the updated settings from the selected element.
   * 
   * @return the element model.
   */
    public E getUpdatedElementFromSettings() {
        return this.getSettingsPanel().getElement();
    }

    /**
   * Add a sub element.
   * 
   * @param element
   *          the element model.
   */
    public void addSubElement(E element) {
        this.getDesktopPanel().addSubElement(element);
    }

    /**
   * Delete a sub element.
   * 
   * @param element
   *          the element model.
   */
    public void deleteSubElement(E element) {
        this.getDesktopPanel().deleteSubElement(element);
    }

    /**
   * Move up or right a sub element.
   * 
   * @param element
   *          the element model.
   */
    public void moveUpOrRightSubElement(E element) {
        this.getDesktopPanel().moveUpOrRightSubElement(element);
    }

    /**
   * Move down or left a sub element.
   * 
   * @param element
   *          the element model.
   */
    public void moveDownOrLeftSubElement(E element) {
        this.getDesktopPanel().moveDownOrLeftSubElement(element);
    }

    /**
   * Get the selected element.
   * 
   * @return the selected element.
   */
    public E getSelectedElement() {
        return this.getDesktopPanel().getSelectedElement();
    }

    /**
   * Set the selected element.
   * 
   * @param documentUniqueID
   *          the document unique ID.
   */
    public void setSelectedDocument(UID documentUniqueID) {
        this.getDesktopPanel().setSelectedDocument(documentUniqueID);
    }
}
