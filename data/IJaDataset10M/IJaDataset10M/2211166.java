package org.windu2b.jcaddie.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import org.windu2b.jcaddie.model.*;
import org.windu2b.jcaddie.tools.*;
import org.windu2b.jcaddie.viewcontroller.CaddieView;

/**
 * @author windu.2b
 * 
 */
public class CaddiePane extends JRootPane implements CaddieView {

    public enum ActionType {

        NEW_CADDIE, CLOSE, OPEN, SAVE, SAVE_AS, EXIT, UNDO, REDO, CUT, COPY, PASTE, DELETE, SELECT_ALL, ADD_CADDIE_PRODUCT, MODIFY_CADDIE_PRODUCT, DELETE_CADDIE_PRODUCT, DELETE_RECENT_CADDIES, DELETE_SELECTION, PREFERENCES, HELP, ABOUT
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 7900354539282438119L;

    private ResourceBundle resource;

    private static final String JCADDIE_EXTENSION = ".xjc";

    private static final FileFilter JCADDIE_FILTER = new FileFilter() {

        public boolean accept(File file) {
            return file.isDirectory() || file.getName().toLowerCase().endsWith(JCADDIE_EXTENSION);
        }

        public String getDescription() {
            return "JCaddie";
        }
    };

    private static File currentDirectory;

    private JComponent focusedComponent, catalogView, productView, caddieView;

    private TransferHandler catalogTransferHandler, producTransferHandler, caddieTransferHandler;

    public CaddiePane(Caddie caddie, UserPreferences preferences, CaddieController controller) {
        resource = ResourceBundle.getBundle(CaddiePane.class.getName());
        createActions(controller);
        setJMenuBar(getCaddieMenuBar(caddie, controller));
        getContentPane().add(getToolBar(), BorderLayout.NORTH);
        getContentPane().add(getMainPane(controller));
        createTransfertHandlers(caddie, preferences, controller);
    }

    /**
	 * Dessine le principal JComponent de l'application: celui qui contient tous
	 * les autres Ce JComponent ne contient pas le menu ni la toolBar
	 * 
	 * @param controller
	 * 
	 * @return
	 */
    private JComponent getMainPane(CaddieController controller) {
        JPanel mainPane = new JPanel(new BorderLayout());
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getCatalogProductPane(controller), getCoursesPane(controller));
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(200);
        mainSplitPane.setResizeWeight(0);
        JPanel statusBarPane = getStatusBarPane(controller);
        mainPane.add(mainSplitPane, BorderLayout.CENTER);
        mainPane.add(statusBarPane, BorderLayout.SOUTH);
        return mainPane;
    }

    /**
	 * Dessine le JComponent de droite dans le JSplitPane Ce JComponent contient
	 * la liste de courses, et le tableau récapitulatif des produits
	 * 
	 * @param controller
	 * @return le JComponent contenant le panneau de droite
	 */
    private JComponent getCoursesPane(CaddieController controller) {
        JComponent coursesView = controller.getProductController().getView();
        JScrollPane scrollPane = new CaddieScrollPane(coursesView);
        JComponent totalPane = controller.getTotalController().getView();
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(scrollPane, BorderLayout.CENTER);
        mainPane.add(totalPane, BorderLayout.SOUTH);
        JPopupMenu coursesViewPopup = new JPopupMenu();
        coursesViewPopup.add(getPopupAction(ActionType.UNDO));
        coursesViewPopup.add(getPopupAction(ActionType.REDO));
        coursesViewPopup.addSeparator();
        coursesViewPopup.add(getPopupAction(ActionType.CUT));
        coursesViewPopup.add(getPopupAction(ActionType.COPY));
        coursesViewPopup.add(getPopupAction(ActionType.PASTE));
        coursesViewPopup.addSeparator();
        coursesViewPopup.add(getPopupAction(ActionType.DELETE));
        coursesViewPopup.add(getPopupAction(ActionType.SELECT_ALL));
        coursesViewPopup.addSeparator();
        coursesViewPopup.add(getPopupAction(ActionType.MODIFY_CADDIE_PRODUCT));
        coursesView.setComponentPopupMenu(coursesViewPopup);
        return mainPane;
    }

    /**
	 * Renvoie la barre de statuts de l'application
	 * 
	 * @param controller
	 * @return
	 */
    private JPanel getStatusBarPane(CaddieController controller) {
        JComponent statusBarView = controller.getStatusBarController().getView();
        JPanel statusBarPane = new JPanel();
        statusBarPane.add(statusBarView);
        return statusBarPane;
    }

    /**
	 * Dessine le JComponent de gauche dans l'application Ce JComponent contient
	 * l'arbre des produits
	 * 
	 * @param controller
	 * 
	 * @return le JComponent contenant l'arbre des produits
	 */
    private JComponent getCatalogProductPane(CaddieController controller) {
        catalogView = controller.getCatalogController().getView();
        JScrollPane catalogScrollPane = new CaddieScrollPane(catalogView);
        catalogView.addFocusListener(new FocusableViewListener(controller, catalogScrollPane));
        ((JViewport) catalogView.getParent()).addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent ev) {
                catalogView.requestFocusInWindow();
            }
        });
        JPopupMenu catalogViewPopup = new JPopupMenu();
        catalogViewPopup.add(getPopupAction(ActionType.UNDO));
        catalogViewPopup.add(getPopupAction(ActionType.REDO));
        catalogViewPopup.addSeparator();
        catalogViewPopup.add(getPopupAction(ActionType.CUT));
        catalogViewPopup.add(getPopupAction(ActionType.COPY));
        catalogViewPopup.add(getPopupAction(ActionType.PASTE));
        catalogViewPopup.addSeparator();
        catalogViewPopup.add(getPopupAction(ActionType.DELETE));
        catalogViewPopup.add(getPopupAction(ActionType.SELECT_ALL));
        catalogViewPopup.addSeparator();
        catalogViewPopup.add(getPopupAction(ActionType.MODIFY_CADDIE_PRODUCT));
        catalogView.setComponentPopupMenu(catalogViewPopup);
        ((JViewport) catalogView.getParent()).setComponentPopupMenu(catalogViewPopup);
        return catalogScrollPane;
    }

    /**
	 * Returns an action decorated for menu items.
	 */
    private Action getMenuAction(ActionType actionType) {
        return new ResourceAction.MenuAction(getActionMap().get(actionType));
    }

    /**
	 * Returns an action decorated for popup menu items.
	 */
    private Action getPopupAction(ActionType actionType) {
        return new ResourceAction.PopupAction(getActionMap().get(actionType));
    }

    private void createTransfertHandlers(Caddie caddie, UserPreferences preferences, CaddieController controller) {
        catalogTransferHandler = new CatalogTransferHandler(preferences.getCatalog());
        producTransferHandler = new ProductTransferHandler(caddie, controller);
        caddieTransferHandler = new CaddieTransferHandler(caddie, controller);
    }

    public void setTransferEnabled(boolean enabled) {
        if (enabled) {
            catalogView.setTransferHandler(catalogTransferHandler);
            productView.setTransferHandler(producTransferHandler);
            caddieView.setTransferHandler(caddieTransferHandler);
            ((JViewport) productView.getParent()).setTransferHandler(producTransferHandler);
        } else {
            catalogView.setTransferHandler(null);
            productView.setTransferHandler(null);
            caddieView.setTransferHandler(null);
            ((JViewport) productView.getParent()).setTransferHandler(null);
        }
    }

    public boolean isClipboardEmpty() {
        return !getToolkit().getSystemClipboard().isDataFlavorAvailable(CaddieTransferableList.CADDIE_FLAVOR);
    }

    /**
	 * Permet de créer toutes les ActionType nécessaires pour l'application. Il
	 * s'agit des actions qui sont accessibles soit par le menu, soit par la
	 * ToolBar, soit par clic-droit Met en place aussi les actions destinées au
	 * D&D
	 * 
	 * @param controller
	 */
    private void createActions(final CaddieController controller) {
        createAction(ActionType.NEW_CADDIE, controller, "newCaddie");
        createAction(ActionType.OPEN, controller, "open");
        createAction(ActionType.CLOSE, controller, "close");
        createAction(ActionType.SAVE, controller, "save");
        createAction(ActionType.SAVE_AS, controller, "saveAs");
        createAction(ActionType.EXIT, controller, "exit");
        createAction(ActionType.UNDO, controller, "undo");
        createAction(ActionType.REDO, controller, "redo");
        createClipboardAction(ActionType.CUT, TransferHandler.getCutAction());
        createClipboardAction(ActionType.COPY, TransferHandler.getCopyAction());
        createClipboardAction(ActionType.PASTE, TransferHandler.getPasteAction());
        createAction(ActionType.DELETE, controller, "delete");
        createAction(ActionType.SELECT_ALL, controller, "selectAll");
        createAction(ActionType.ADD_CADDIE_PRODUCT, controller, "addCaddieProduct");
        createAction(ActionType.DELETE_CADDIE_PRODUCT, controller, "deleteCaddieProduct");
        createAction(ActionType.MODIFY_CADDIE_PRODUCT, controller.getProductController(), "modifySelectedProduct");
        createAction(ActionType.DELETE_SELECTION, controller.getProductController(), "deleteSelection");
        createAction(ActionType.HELP, controller, "help");
        createAction(ActionType.PREFERENCES, controller, "preferences");
        createAction(ActionType.ABOUT, controller, "about");
    }

    private void createClipboardAction(ActionType actionType, final Action clipboardAction) {
        getActionMap().put(actionType, new ResourceAction(resource, actionType.toString()) {

            /**
					 * 
					 */
            private static final long serialVersionUID = 4622359781849184293L;

            public void actionPerformed(ActionEvent ev) {
                ev = new ActionEvent(focusedComponent, ActionEvent.ACTION_PERFORMED, null);
                clipboardAction.actionPerformed(ev);
            }
        });
    }

    /**
	 * Ajoute un ActionType, avec son contrôleur et la méthode qu'il appelle
	 * 
	 * @param action
	 *            l'ActionType qui est ajouté
	 * @param controller
	 *            le contrôleur utilisé par l'ActionType
	 * @param string
	 *            la méthode invoquée
	 * 
	 */
    private void createAction(ActionType action, Object controller, String method) {
        try {
            getActionMap().put(action, new ControllerAction(resource, action.toString(), controller, method));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
	 * Permet de générer la barre de menu, avec les menus principaux et leurs
	 * sous-menus respectifs Se base sur la <code>resource</code> pour le
	 * choix de la langue
	 * 
	 * @return JMenuBar La barre de menu de l'application
	 */
    private JMenuBar getCaddieMenuBar(final Caddie caddie, final CaddieController controller) {
        ActionMap actions = getActionMap();
        JMenu fileMenu = new JMenu(new ResourceAction(this.resource, "FILE_MENU", true));
        fileMenu.add(getMenuAction(ActionType.NEW_CADDIE));
        fileMenu.add(getMenuAction(ActionType.OPEN));
        final JMenu openRecentHomeMenu = new JMenu(new ResourceAction(this.resource, "OPEN_RECENT_CADDIE_MENU", true));
        openRecentHomeMenu.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent ev) {
                updateOpenRecentCaddieMenu(openRecentHomeMenu, controller);
            }

            public void menuCanceled(MenuEvent ev) {
            }

            public void menuDeselected(MenuEvent ev) {
            }
        });
        fileMenu.add(actions.get(ActionType.SAVE));
        fileMenu.add(actions.get(ActionType.SAVE_AS));
        if (!System.getProperty("os.name").startsWith("Mac OS X")) {
            fileMenu.addSeparator();
            fileMenu.add(actions.get(ActionType.PREFERENCES));
            fileMenu.addSeparator();
            fileMenu.add(actions.get(ActionType.EXIT));
        }
        JMenu editMenu = new JMenu(new ResourceAction(resource, "EDIT_MENU", true));
        editMenu.setEnabled(true);
        editMenu.add(actions.get(ActionType.UNDO));
        editMenu.add(actions.get(ActionType.REDO));
        editMenu.addSeparator();
        editMenu.add(actions.get(ActionType.CUT));
        editMenu.add(actions.get(ActionType.COPY));
        editMenu.add(actions.get(ActionType.PASTE));
        JMenu productMenu = new JMenu(new ResourceAction(resource, "PRODUCT_MENU"));
        productMenu.setEnabled(true);
        productMenu.add(actions.get(ActionType.ADD_CADDIE_PRODUCT));
        productMenu.add(actions.get(ActionType.DELETE_CADDIE_PRODUCT));
        JMenu helpMenu = null;
        if (!System.getProperty("os.name").startsWith("Mac OS X")) {
            helpMenu = new JMenu(new ResourceAction(resource, "HELP_MENU", true));
            helpMenu.setEnabled(true);
            helpMenu.add(actions.get(ActionType.HELP));
            helpMenu.add(actions.get(ActionType.ABOUT));
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(productMenu);
        if (helpMenu != null) menuBar.add(helpMenu);
        return menuBar;
    }

    /**
	 * Updates <code>openRecentHomeMenu</code> from current recent caddies in
	 * preferences.
	 */
    protected void updateOpenRecentCaddieMenu(JMenu openRecentHomeMenu, final CaddieController controller) {
        openRecentHomeMenu.removeAll();
        for (final String caddieName : controller.getRecentCaddies()) {
            openRecentHomeMenu.add(new AbstractAction(new File(caddieName).getName()) {

                /**
				 * 
				 */
                private static final long serialVersionUID = 3125615753174599524L;

                public void actionPerformed(ActionEvent e) {
                    controller.open(caddieName);
                }
            });
        }
        if (openRecentHomeMenu.getMenuComponentCount() > 0) {
            openRecentHomeMenu.addSeparator();
        }
        openRecentHomeMenu.add(getMenuAction(ActionType.DELETE_RECENT_CADDIES));
    }

    /**
	 * Création de la ToolBar, avec tous ses boutons Le focus a été retiré des
	 * boutons
	 * 
	 * @return
	 */
    private Component getToolBar() {
        JToolBar toolBar = new JToolBar();
        ActionMap actions = getActionMap();
        toolBar.add(actions.get(ActionType.NEW_CADDIE));
        toolBar.add(actions.get(ActionType.OPEN));
        toolBar.add(actions.get(ActionType.SAVE));
        toolBar.addSeparator();
        toolBar.add(actions.get(ActionType.ADD_CADDIE_PRODUCT));
        toolBar.add(actions.get(ActionType.DELETE_CADDIE_PRODUCT));
        toolBar.addSeparator();
        toolBar.add(actions.get(ActionType.UNDO));
        toolBar.add(actions.get(ActionType.REDO));
        for (int i = 0, n = toolBar.getComponentCount(); i < n; i++) toolBar.getComponentAtIndex(i).setFocusable(false);
        return toolBar;
    }

    public void setEnabled(ActionType actionType, boolean enabled) {
        getActionMap().get(actionType).setEnabled(enabled);
    }

    public void setUndoRedoName(String undoText, String redoText) {
        setNameAndShortDescription(ActionType.UNDO, undoText);
        setNameAndShortDescription(ActionType.REDO, redoText);
    }

    public void setNameAndShortDescription(ActionType actionType, String name) {
        Action action = getActionMap().get(actionType);
        if (name == null) name = (String) action.getValue(Action.DEFAULT);
        action.putValue(Action.NAME, name);
        action.putValue(Action.SHORT_DESCRIPTION, name);
    }

    /**
	 * Méthode invoquée lors de l'ouverture d'un fichier. Elle génère un
	 * FileChooser permettant de choisir le nom et l'emplacement du fichier
	 * 
	 * @return Le nom complet (chemin absolu) du fichier à ouvrir, tel que choisi par l'utilisateur.
	 */
    public String showOpenDialog() {
        if (System.getProperty("os.name").startsWith("Mac OS X")) return showFileDialog(false, null); else return showFileChooser(false, null);
    }

    /**
	 * Méthode invoquée lors de la première sauvegarde d'un fichier Elle génère
	 * un FileChooser permettant de choisir le nom et l'emplacement du fichier
	 * 
	 * @param name Le nom du caddie à sauvegarder
	 * @return Le nom complet (chemin absolu) du fichier dans lequel sauvegarder
	 */
    public String showSaveDialog(String name) {
        String file;
        if (System.getProperty("os.name").startsWith("Mac OS X")) file = showFileDialog(true, name); else file = showFileChooser(true, name);
        if (file != null && !file.toLowerCase().endsWith(JCADDIE_EXTENSION)) file += JCADDIE_EXTENSION;
        return file;
    }

    /**
	 * Méthode permettant d'afficher un Filechooser
	 * 
	 * @param save
	 * @param name
	 * @return
	 */
    private String showFileChooser(boolean save, String name) {
        JFileChooser fileChooser = new JFileChooser();
        if (save && name != null) fileChooser.setSelectedFile(new File(name));
        fileChooser.setFileFilter(JCADDIE_FILTER);
        if (currentDirectory != null) fileChooser.setCurrentDirectory(currentDirectory);
        int option;
        if (save) option = fileChooser.showSaveDialog(this); else option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentDirectory = fileChooser.getCurrentDirectory();
            return fileChooser.getSelectedFile().toString();
        } else return null;
    }

    /**
	 * Méthode permettant d'afficher le FileChooser propre à Mac OS X
	 * 
	 * @param save
	 * @param name
	 * @return
	 */
    private String showFileDialog(boolean save, String name) {
        FileDialog fileDialog = new FileDialog(JOptionPane.getFrameForComponent(this));
        if (save && name != null) fileDialog.setFile(new File(name).getName());
        fileDialog.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(JCADDIE_EXTENSION);
            }
        });
        if (currentDirectory != null) fileDialog.setDirectory(currentDirectory.toString());
        if (save) {
            fileDialog.setMode(FileDialog.SAVE);
            fileDialog.setTitle(resource.getString("fileDialog.saveTitle"));
        } else {
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.setTitle(resource.getString("fileDialog.openTitle"));
        }
        fileDialog.setVisible(true);
        String selectedFile = fileDialog.getFile();
        if (selectedFile != null) {
            currentDirectory = new File(fileDialog.getDirectory());
            return currentDirectory + File.separator + selectedFile;
        } else return null;
    }

    public void showError(String message) {
        String title = resource.getString("error.title");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * Displays <code>message</code> in a message box.
	 */
    public void showMessage(String message) {
        String title = this.resource.getString("message.title");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showAboutDialog() {
        JEditorPane messagePane = new JEditorPane("text/html", resource.getString("about.message"));
        messagePane.setOpaque(false);
        messagePane.setEditable(false);
        messagePane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent ev) {
                if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    viewURL(ev.getURL());
                }
            }
        });
        String title = resource.getString("about.title");
        Icon icon = new ImageIcon(ImageInJAR.getInstance().getImage(resource.getString("about.icon")));
        JOptionPane.showMessageDialog(this, messagePane, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }

    private void viewURL(URL url) {
    }

    public boolean confirmExit() {
        String message = resource.getString("confimExit.message");
        String title = resource.getString("confirm Exit.title");
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    public boolean confirmOverwrite(String name) {
        String messageFormat = resource.getString("confirmOverwrite.message");
        String message = String.format(messageFormat, new File(name).getName());
        String title = resource.getString("confirmOverwrite.title");
        String replace = resource.getString("confirmOverwrite.overwrite");
        String cancel = resource.getString("confirmOverwrite.cancel");
        return JOptionPane.showOptionDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { replace, cancel }, cancel) == JOptionPane.OK_OPTION;
    }

    public enum SaveAnswer {

        SAVE, CANCEL, DO_NOT_SAVE
    }

    /**
	 * Méthode demandant à l'utilisateur de confirmer s'il veut quitter
	 * l'application. Cette méthode n'est invoquée que si des données ont été
	 * modifiées
	 * 
	 * @param name
	 *            Le nom du caddie à enregistrer
	 * @return SaveAnswer Le {@link CaddiePane.SaveAnswer type} de réponse obtenue
	 */
    public SaveAnswer confirmSave(String name) {
        String messageFormat = this.resource.getString("confirmSave.message");
        String message;
        if (name != null) message = String.format(messageFormat, "\"" + new File(name).getName() + "\""); else message = String.format(messageFormat, "");
        String title = resource.getString("confirmSave.title");
        String save = resource.getString("confirmSave.save");
        String doNotSave = resource.getString("confirmSave.doNotsave");
        String cancel = resource.getString("confirmSave.cancel");
        switch(JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { save, doNotSave, cancel }, save)) {
            case JOptionPane.YES_OPTION:
                return SaveAnswer.SAVE;
            case JOptionPane.NO_OPTION:
                return SaveAnswer.DO_NOT_SAVE;
            default:
                return SaveAnswer.CANCEL;
        }
    }

    private static class CaddieScrollPane extends JScrollPane {

        /**
		 * 
		 */
        private static final long serialVersionUID = 7476444931212943305L;

        public CaddieScrollPane(JComponent view) {
            super(view);
            if (System.getProperty("os.name").startsWith("Mac OS X")) {
                setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
                setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
            }
        }
    }

    private static final Border UNFOCUSED_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    private static final Border FOCUSED_BORDER = BorderFactory.createLineBorder(UIManager.getColor("textHighlight"), 2);

    private class FocusableViewListener implements FocusListener {

        private CaddieController controller;

        private JComponent feedbackComponent;

        public FocusableViewListener(CaddieController controller, JComponent feedbackComponent) {
            this.controller = controller;
            this.feedbackComponent = feedbackComponent;
            feedbackComponent.setBorder(UNFOCUSED_BORDER);
        }

        /**
		 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
		 */
        public void focusGained(FocusEvent ev) {
            feedbackComponent.setBorder(FOCUSED_BORDER);
            focusedComponent = (JComponent) ev.getComponent();
            controller.focusedViewchanged(focusedComponent);
        }

        /**
		 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
		 */
        public void focusLost(FocusEvent ev) {
            feedbackComponent.setBorder(UNFOCUSED_BORDER);
        }
    }
}
