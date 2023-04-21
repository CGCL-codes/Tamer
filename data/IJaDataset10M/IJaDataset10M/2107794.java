package unbbayes.gui.oobn;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;
import unbbayes.controller.ConfigurationsController;
import unbbayes.controller.FileHistoryController;
import unbbayes.controller.IconController;
import unbbayes.controller.MSBNController;
import unbbayes.controller.oobn.OOBNController;
import unbbayes.gui.FileIcon;
import unbbayes.gui.MDIDesktopPane;
import unbbayes.gui.SimpleFileFilter;
import unbbayes.io.BaseIO;
import unbbayes.io.exception.LoadException;
import unbbayes.io.exception.UBIOException;
import unbbayes.io.oobn.IObjectOrientedBayesianNetworkIO;
import unbbayes.prs.Graph;
import unbbayes.prs.bn.SingleEntityNetwork;
import unbbayes.prs.msbn.AbstractMSBN;
import unbbayes.prs.msbn.SingleAgentMSBN;
import unbbayes.prs.oobn.IOOBNClass;
import unbbayes.prs.oobn.IObjectOrientedBayesianNetwork;
import unbbayes.util.Debug;
import unbbayes.util.extension.UnBBayesModule;

/**
 * @author Shou Matsumoto
 *
 */
public class OOBNWindow extends UnBBayesModule {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    public static String EDITION_PANE = "editionPane";

    private static final String[] SUPPORTED_FILE_EXTENSIONS = { IObjectOrientedBayesianNetworkIO.fileExtension };

    /** Load resource file from this package */
    private static ResourceBundle resource = unbbayes.util.ResourceController.newInstance().getBundle(unbbayes.gui.oobn.resources.OOBNGuiResource.class.getName());

    private JScrollPane oobnClassScroll;

    private JList oobnClassList;

    private JButton compileBtn;

    private JButton removeBtn;

    private JButton newBtn;

    private JButton newFromFileBtn;

    private CardLayout btnCard;

    private JToolBar jtbBtns;

    private JPanel statusPanel;

    private JLabel statusBar;

    private JPanel netPanel;

    private JPanel editionPane;

    protected IconController iconController = IconController.getInstance();

    private OOBNController controller = null;

    private FileHistoryController fileHistoryController = null;

    private String moduleName = "OOBN";

    /**
	 * Builds a window to visualize and edit OOBN
	 * @param oobn: the model representation of OOBN
	 * @param controller: who is the controller of this OOBN
	 */
    protected OOBNWindow(IObjectOrientedBayesianNetwork oobn, OOBNController controller) {
        super(oobn.getTitle());
        this.setModuleName(this.resource.getString("OOBNModuleName"));
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        Container pane = this.getContentPane();
        this.initComponents();
        pane.setLayout(new BorderLayout());
        pane.add(buildClassNavigationPanel(), BorderLayout.WEST);
        pane.add(this.buildStatusBar(), BorderLayout.SOUTH);
        this.fillListeners();
    }

    /**
	 * Builds a window to visualize and edit OOBN
	 * @param oobn: the model representation of OOBN
	 * @param controller: who is the controller of this OOBN
	 */
    public static OOBNWindow newInstance(IObjectOrientedBayesianNetwork oobn, OOBNController controller) {
        return new OOBNWindow(oobn, controller);
    }

    /**
	 * Build up the basic component hierarchy of this window
	 * and initializes some attributes
	 */
    private void initComponents() {
        this.oobnClassList = new JList(new OOBNListModel());
        this.oobnClassList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.oobnClassList.setEnabled(true);
        this.oobnClassList.setDragEnabled(true);
        this.oobnClassList.setToolTipText(resource.getString("dragNDropToAddInstance"));
        this.oobnClassList.setTransferHandler(new TransferHandler() {

            @Override
            protected Transferable createTransferable(JComponent c) {
                try {
                    IOOBNClass oobnclass = getController().getSelectedClass();
                    Debug.println(this.getClass(), "Creating transferable element for oobnclass: " + oobnclass.getClassName());
                    return oobnclass;
                } catch (Exception e) {
                    Debug.println(this.getClass(), "It was not possible to create transferable data", e);
                }
                return null;
            }

            @Override
            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY;
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                Debug.println(this.getClass(), "Export of data was done");
                renewClassListIndex();
                super.exportDone(source, data, action);
            }
        });
        this.compileBtn = new JButton(iconController.getCompileIcon());
        this.removeBtn = new JButton(iconController.getDeleteClassIcon());
        this.newBtn = new JButton(iconController.getNewClassIcon());
        this.newFromFileBtn = new JButton(iconController.getNewClassFromFileIcon());
        this.compileBtn.setToolTipText(resource.getString("compileToolTip"));
        this.removeBtn.setToolTipText(resource.getString("removeToolTip"));
        this.newBtn.setToolTipText(resource.getString("newToolTip"));
        this.newFromFileBtn.setToolTipText(resource.getString("newFromFileToolTip"));
        this.fileHistoryController = FileHistoryController.getInstance();
    }

    /**
	 * Builds up the status bar
	 * @return the status bar
	 */
    private Container buildStatusBar() {
        statusPanel = new JPanel();
        statusBar = new JLabel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBorder(new TitledBorder(resource.getString("status")));
        statusBar.setText(resource.getString("statusReadyLabel"));
        statusPanel.add(statusBar);
        return this.statusPanel;
    }

    /**
	 * build up the class navigation panel
	 * @return a JPanel with class navigation
	 */
    private JPanel buildClassNavigationPanel() {
        this.netPanel = new JPanel(new BorderLayout());
        this.oobnClassScroll = new JScrollPane(oobnClassList);
        this.oobnClassScroll.setToolTipText(resource.getString("dragNDropToAddInstance"));
        netPanel.setBorder(new TitledBorder(resource.getString("classNavigationPanelLabel")));
        netPanel.add(this.oobnClassScroll, BorderLayout.CENTER);
        this.jtbBtns = new JToolBar();
        this.jtbBtns.add(buildButtonsPanel(), EDITION_PANE);
        netPanel.add(jtbBtns, BorderLayout.NORTH);
        return netPanel;
    }

    /**
	 * Sets up the edition pane (which contains basic buttons to control classes).
	 * Also fills up some attributes of this class
	 */
    private JPanel buildButtonsPanel() {
        this.btnCard = new CardLayout();
        this.jtbBtns.setLayout(this.btnCard);
        this.editionPane = new JPanel();
        editionPane.add(this.newBtn);
        editionPane.add(this.newFromFileBtn);
        editionPane.add(this.removeBtn);
        editionPane.add(this.compileBtn);
        showBtnPanel(EDITION_PANE);
        return this.editionPane;
    }

    public void addCompileBtnActionListener(ActionListener a) {
        this.compileBtn.addActionListener(a);
    }

    public void addRemoveBtnActionListener(ActionListener a) {
        this.removeBtn.addActionListener(a);
    }

    public void addNewBtnActionListener(ActionListener a) {
        this.newBtn.addActionListener(a);
    }

    public void addNewFromFileBtnActionListener(ActionListener a) {
        this.newFromFileBtn.addActionListener(a);
    }

    public void addListMouseListener(MouseListener l) {
        this.oobnClassList.addMouseListener(l);
    }

    public void showBtnPanel(String paneName) {
        this.btnCard.show(this.jtbBtns, paneName);
    }

    /**
	 * starts filling the action listeners
	 */
    private void fillListeners() {
        addListMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    try {
                        int index = getNetList().locationToIndex(e.getPoint());
                        getController().setSelectedClass((getController().getOobn().getOOBNClassList().get(index)));
                    } catch (Exception exc) {
                        Debug.println(this.getClass(), "It was not possible to perform mouse pressed event", exc);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                this.mouseEntered(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                renewClassListIndex();
                super.mouseEntered(e);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    if (e.getClickCount() < 2) {
                        Debug.println(this.getClass(), "Use double click to activate an oobnclass.");
                        return;
                    }
                }
                int index = getNetList().locationToIndex(e.getPoint());
                if ((index >= 0) && (getNetList().getModel().getElementAt(index) != getController().getActive().getSingleEntityNetwork())) {
                    OOBNClassWindow classWindow = OOBNClassWindow.newInstance((getController().getOobn().getOOBNClassList().get(index)));
                    getController().changeActiveOOBNClass(classWindow);
                }
                if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    ListSelectionModel selmodel = getNetList().getSelectionModel();
                    selmodel.setLeadSelectionIndex(index);
                    Object item = getNetList().getModel().getElementAt(index);
                    String text = JOptionPane.showInputDialog(resource.getString("renameClass"), item);
                    String newName = null;
                    if (text != null) {
                        newName = text.trim();
                    } else {
                        return;
                    }
                    if (getController().containsOOBNClassByName(newName)) {
                        Debug.println(this.getClass(), "The name already exists");
                        JOptionPane.showMessageDialog(getController().getPanel(), resource.getString("DuplicatedClassName"), resource.getString("renameClass"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (newName != null && !newName.equalsIgnoreCase("")) {
                        try {
                            getController().getOobn().getOOBNClassList().get(index).setClassName(newName);
                            getController().updateStatusBarEditionMessage();
                        } catch (Exception e1) {
                            Debug.println(this.getClass(), "Invalid name", e1);
                            System.err.print(e1.getMessage());
                        }
                    }
                    Debug.println(this.getClass(), "Changing references at renaming event is not implemented yet.");
                }
            }
        });
        addNewBtnActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getController().addNewOOBNClass(resource.getString("newOOBNClass") + getController().getOobn().getOOBNClassList().size());
                getNetList().updateUI();
            }
        });
        addNewFromFileBtnActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String[] nets = new String[] { "net", "oobn" };
                JFileChooser chooser = new JFileChooser(fileHistoryController.getCurrentDirectory());
                chooser.setDialogTitle(resource.getString("openClassFromFile"));
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileView(new FileIcon(getDesktopPane().getParent()));
                chooser.addChoosableFileFilter(new SimpleFileFilter(nets, resource.getString("oobnFileFilter")));
                int option = chooser.showOpenDialog(getDesktopPane().getParent());
                if (option == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile() != null) {
                        chooser.setVisible(false);
                        getDesktopPane().repaint();
                        File file = chooser.getSelectedFile();
                        fileHistoryController.setCurrentDirectory(chooser.getCurrentDirectory());
                        chooser.setVisible(false);
                        chooser.setEnabled(false);
                        try {
                            Collection<IOOBNClass> newClasses = getController().loadOOBNClassesFromFile(file);
                            for (IOOBNClass loadedClass : newClasses) {
                                try {
                                    getController().addOOBNClass(loadedClass);
                                } catch (IllegalArgumentException iae) {
                                    Debug.println(this.getClass(), "Loaded a class already loaded.");
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(getController().getPanel(), resource.getString("ErrorLoadingClass"), e.getMessage(), JOptionPane.ERROR_MESSAGE);
                            Debug.println(this.getClass(), "Error opening file", e);
                        }
                        getNetList().updateUI();
                    }
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        addRemoveBtnActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (getController().getActive() == null) {
                    return;
                }
                int index = getNetList().getSelectedIndex();
                if (index < 0) {
                    return;
                }
                getController().removeClassAt(index);
                getNetList().setSelectedIndex(0);
                getNetList().updateUI();
                getNetList().repaint();
            }
        });
        addCompileBtnActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                try {
                    AbstractMSBN msbn = getController().compileActiveOOBNClassToMSBN();
                    MSBNController controller = new MSBNController((SingleAgentMSBN) msbn);
                    if (getDesktopPane() instanceof MDIDesktopPane) {
                        ((MDIDesktopPane) getDesktopPane()).add(controller.getPanel());
                    } else {
                        getDesktopPane().add(controller.getPanel());
                    }
                    controller.getPanel().setVisible(true);
                } catch (NullPointerException npe) {
                    JOptionPane.showMessageDialog(getController().getPanel(), resource.getString("NoClassSelected"), resource.getString("compilationError"), JOptionPane.ERROR_MESSAGE);
                    Debug.println(this.getClass(), resource.getString("NoClassSelected"), npe);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getController().getPanel(), e.getMessage(), resource.getString("compilationError"), JOptionPane.ERROR_MESSAGE);
                    Debug.println(this.getClass(), "Unknown", e);
                }
            }
        });
    }

    /**
	 * Returns the oobnClassList.
	 * @return JList
	 */
    public JList getNetList() {
        return this.oobnClassList;
    }

    public void changeToTreeView(JTree tree) {
        this.oobnClassScroll.setViewportView(tree);
    }

    public void changeToListView() {
        this.oobnClassScroll.setViewportView(this.oobnClassList);
    }

    /**
	 * This method resets the currently selected class list element
	 * to the currently active class window.
	 * It is useful to make sure that the selected class at class list is
	 * allways the currently active class.
	 * @see OOBNController#getActive()
	 */
    protected void renewClassListIndex() {
        try {
            SingleEntityNetwork activeNetwork = getController().getActive().getController().getSingleEntityNetwork();
            int indexOfActiveNetwork = getController().getOobn().getOOBNClassList().indexOf(activeNetwork);
            getOobnClassList().setSelectedIndex(indexOfActiveNetwork);
            getOobnClassList().updateUI();
            Debug.println(this.getClass(), "Setted active class list index to " + indexOfActiveNetwork);
        } catch (Exception exc) {
            Debug.println(this.getClass(), "Could not treat event in order to change selected OOBN class", exc);
        }
    }

    public OOBNController getController() {
        return controller;
    }

    public void setController(OOBNController controller) {
        this.controller = controller;
    }

    /**
	 * Inner class to make it easier to  manage JList and its
	 * contents
	 * @author Shou Matsumoto
	 *
	 */
    private class OOBNListModel extends AbstractListModel {

        /** Serialization runtime version number */
        private static final long serialVersionUID = 0;

        public int getSize() {
            return getController().getOobn().getOOBNClassCount();
        }

        public Object getElementAt(int index) {
            try {
                return getController().getOobn().getOOBNClassList().get(index);
            } catch (RuntimeException e) {
                try {
                    Debug.println(this.getClass(), "Cannot retrieve oobn class at " + index + " from " + getController().getOobn().getTitle());
                } catch (Exception e2) {
                    Debug.println(this.getClass(), "Unknown error - may be no OOBN is set.");
                }
                throw e;
            }
        }
    }

    /**
	 * @return the oobnClassScroll
	 */
    public JScrollPane getOobnClassScroll() {
        return oobnClassScroll;
    }

    /**
	 * @param oobnClassScroll the oobnClassScroll to set
	 */
    public void setOobnClassScroll(JScrollPane oobnClassScroll) {
        this.oobnClassScroll = oobnClassScroll;
    }

    /**
	 * @return the oobnClassList
	 */
    public JList getOobnClassList() {
        return oobnClassList;
    }

    /**
	 * @param oobnClassList the oobnClassList to set
	 */
    public void setOobnClassList(JList oobnClassList) {
        this.oobnClassList = oobnClassList;
    }

    /**
	 * @return the compileBtn
	 */
    public JButton getCompileBtn() {
        return compileBtn;
    }

    /**
	 * @param compileBtn the compileBtn to set
	 */
    public void setCompileBtn(JButton compileBtn) {
        this.compileBtn = compileBtn;
    }

    /**
	 * @return the removeBtn
	 */
    public JButton getRemoveBtn() {
        return removeBtn;
    }

    /**
	 * @param removeBtn the removeBtn to set
	 */
    public void setRemoveBtn(JButton removeBtn) {
        this.removeBtn = removeBtn;
    }

    /**
	 * @return the newBtn
	 */
    public JButton getNewBtn() {
        return newBtn;
    }

    /**
	 * @param newBtn the newBtn to set
	 */
    public void setNewBtn(JButton newBtn) {
        this.newBtn = newBtn;
    }

    /**
	 * @return the newFromFileBtn
	 */
    public JButton getNewFromFileBtn() {
        return newFromFileBtn;
    }

    /**
	 * @param newFromFileBtn the newFromFileBtn to set
	 */
    public void setNewFromFileBtn(JButton newFromFileBtn) {
        this.newFromFileBtn = newFromFileBtn;
    }

    /**
	 * @return the btnCard
	 */
    public CardLayout getBtnCard() {
        return btnCard;
    }

    /**
	 * @param btnCard the btnCard to set
	 */
    public void setBtnCard(CardLayout btnCard) {
        this.btnCard = btnCard;
    }

    /**
	 * @return the jtbBtns
	 */
    public JToolBar getJtbBtns() {
        return jtbBtns;
    }

    /**
	 * @param jtbBtns the jtbBtns to set
	 */
    public void setJtbBtns(JToolBar jtbBtns) {
        this.jtbBtns = jtbBtns;
    }

    /**
	 * @return the statusPanel
	 */
    public JPanel getStatusPanel() {
        return statusPanel;
    }

    /**
	 * @param statusPanel the statusPanel to set
	 */
    public void setStatusPanel(JPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
	 * @return the statusBar
	 */
    public JLabel getStatusBar() {
        return statusBar;
    }

    /**
	 * @param statusBar the statusBar to set
	 */
    public void setStatusBar(JLabel statusBar) {
        this.statusBar = statusBar;
    }

    /**
	 * @return the netPanel
	 */
    public JPanel getNetPanel() {
        return netPanel;
    }

    /**
	 * @param netPanel the netPanel to set
	 */
    public void setNetPanel(JPanel netPanel) {
        this.netPanel = netPanel;
    }

    /**
	 * @return the editionPane
	 */
    public JPanel getEditionPane() {
        return editionPane;
    }

    /**
	 * @param editionPane the editionPane to set
	 */
    public void setEditionPane(JPanel editionPane) {
        this.editionPane = editionPane;
    }

    /**
	 * @return the iconController
	 */
    public IconController getIconController() {
        return iconController;
    }

    /**
	 * @param iconController the iconController to set
	 */
    public void setIconController(IconController iconController) {
        this.iconController = iconController;
    }

    /**
	 * @return the fileHistoryController
	 */
    public FileHistoryController getFileController() {
        return fileHistoryController;
    }

    /**
	 * @param fileHistoryController the fileHistoryController to set
	 */
    public void setFileController(FileHistoryController fileHistoryController) {
        this.fileHistoryController = fileHistoryController;
    }

    public String getSavingMessage() {
        return resource.getString("saveTitle");
    }

    /**
	 * Setts the status bar's message
	 * @param text
	 */
    public void setStatusBarText(String text) {
        this.getStatusBar().setText(text);
    }

    public JInternalFrame getInternalFrame() {
        return this;
    }

    public BaseIO getIO() {
        return this.getController().getBaseIO();
    }

    public Graph getPersistingGraph() {
        try {
            return this.getController().getActive().getController().getControlledClass().getNetwork();
        } catch (NullPointerException e) {
            Debug.println(this.getClass(), "Found null at getController().getActive().getController().getControlledClass().getNetwork()", e);
        }
        return null;
    }

    @Override
    public String getModuleName() {
        return this.moduleName;
    }

    /**
	 * Setter for this module name.
	 * @param moduleName
	 * @see #getModuleName()
	 */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
        this.setName(moduleName);
        this.setTitle(moduleName);
        this.updateUI();
        this.repaint();
    }

    /**
	 * Opens a new desktop window into currently used java desktop
	 * @see unbbayes.util.extension.UnBBayesModule#openFile(java.io.File)
	 */
    @Override
    public UnBBayesModule openFile(File file) throws IOException {
        Graph g = null;
        g = this.getIO().load(file);
        OOBNController controller = null;
        try {
            ConfigurationsController.getInstance().addFileToListRecentFiles(file);
            controller = OOBNController.newInstance((IObjectOrientedBayesianNetwork) g);
        } catch (Exception e) {
            throw new RuntimeException(this.resource.getString("unsupportedGraphFormat"), e);
        }
        this.dispose();
        OOBNWindow ret = (OOBNWindow) controller.getPanel();
        ret.setModuleName(file.getName());
        return ret;
    }
}
