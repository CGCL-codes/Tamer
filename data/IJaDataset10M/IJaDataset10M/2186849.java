package com.dmanski.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.dmanski.DomainObject.UnknownMp3;
import com.dmanski.dao.UnknownMp3Dao;
import com.dmanski.infrastrukture.MDBObjectAM;
import com.dmanski.mp3.ui.DialogReadMp3;
import com.dmanski.mp3.ui.MainMp3AM;
import com.dmanski.mp3.ui.MainMpUnknown3AM;
import com.dmanski.mp3.ui.MainMpUnknown3Panel;
import com.dmanski.ui.MainMenuBarPanel.EmenuItem;
import com.dmanski.ui.MainTreePanel.ENODE;
import com.dmanski.util.ui.CommonObjects;
import com.dmanski.util.ui.ImageCache;
import com.dmanski.util.ui.MDBLanguage;
import com.dmanski.util.ui.ImageCache.EIMAGE;

/**
 * Die Hauptklasse der Anwendung 
 * @author 08.02.2009  Daniel Manski  D.Manski@dmanski.com 
 */
public class MainAM extends JFrame implements ActionListener, TreeSelectionListener, WindowListener {

    private static final long serialVersionUID = 5205150623446389415L;

    private StartMainPanel panel;

    private JMenuBar menu;

    private MainTreePanel tree;

    private MainStatusBar statusbar;

    private JComponent currentComponent;

    private MDBObjectAM currentAM;

    /**
	 * Konstruktor der Haupfensterkasse der Anwendung
	 */
    public MainAM() {
        super(MDBLanguage.getInstance().get("mdb.general.title"));
        this.setMinimumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setIconImage(ImageCache.getInstance().getImage(EIMAGE.LOGO16));
        this.setGlassPane(new MainGlassPane());
        init();
    }

    /**
	 * Initialisiert das Gui
	 */
    private void init() {
        panel = new StartMainPanel();
        currentComponent = panel;
        statusbar = new MainStatusBar();
        menu = new MainMenuBarPanel(this, statusbar);
        tree = new MainTreePanel(this, statusbar);
        this.setJMenuBar(menu);
        addComponentInLayout(tree, 0, 0, 1, 1, GridBagConstraints.VERTICAL, GridBagConstraints.WEST, 0., 0., new Insets(0, 0, 0, 0));
        switchMainComponent(panel);
        addComponentInLayout(statusbar, 0, 1, 2, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST, 0., 0., new Insets(0, 0, 0, 0));
    }

    @Override
    public void setVisible(boolean b) {
        int width = this.getWidth();
        int height = this.getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        this.setBounds(x, y, width, height);
        this.pack();
        super.setVisible(b);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith(MainMenuBarPanel.MENUPREFIX)) {
            menuBarActionPreformed(e);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath thePath = e.getNewLeadSelectionPath();
        if (thePath != null) {
            DefaultMutableTreeNode theNode = (DefaultMutableTreeNode) thePath.getLastPathComponent();
            if (theNode.equals(ENODE.SUBNODEMP3.getNode())) {
                currentAM = new MainMp3AM(this);
                switchMainComponent(currentAM.getPanel());
            } else if (theNode.equals(ENODE.SUBNODEMP3NEW_ERKANNT.getNode())) {
                switchMainComponent(new MainMp3AM(this).getPanel());
            } else if (theNode.equals(ENODE.SUBNODEMP3NEW_UNBEKANNT.getNode())) {
                currentAM = new MainMpUnknown3AM(this);
                switchMainComponent(currentAM.getPanel());
            } else {
                switchMainComponent(new StartMainPanel());
            }
        }
    }

    /**
	 * Wechselt das Hauptelement im Layout aus
	 * @param newComponent
	 */
    private void switchMainComponent(JComponent newComponent) {
        getContentPane().remove(currentComponent);
        currentComponent = newComponent;
        addComponentInLayout(newComponent, 1, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST, 1., 1., new Insets(0, 0, 0, 0));
        validate();
        repaint();
    }

    /**
	 * Actions der MenuBar
	 * @param e
	 */
    private void menuBarActionPreformed(ActionEvent e) {
        if (e.getActionCommand().equals(EmenuItem.EXIT.getActionCommand())) {
            this.dispose();
            System.exit(0);
        } else if (e.getActionCommand().equals(EmenuItem.ABOUT.getActionCommand())) {
        } else if (e.getActionCommand().equals(EmenuItem.READMP3FOLDER.getActionCommand())) {
            CommonObjects.getInstance().disableMainAndShowWaitCursor();
            DialogReadMp3 theDialog = new DialogReadMp3(this);
            theDialog.startUp();
            if (theDialog.getThreadStatus()) {
                UnknownMp3Dao theDao = new UnknownMp3Dao();
                for (File eachFile : theDialog.getUnknownMp3Files()) {
                    UnknownMp3 theMp3 = new UnknownMp3();
                    theMp3.buildObject(eachFile);
                    theDao.insert(theMp3);
                }
                if (currentComponent instanceof MainMpUnknown3Panel) {
                    ((MainMpUnknown3AM) currentAM).init();
                }
            }
            CommonObjects.getInstance().enableMainAndShowDefaultCursor();
        }
    }

    /**
	 * Setzt die uebergebene Componente entsprechend den Constraintparametern
	 * in das Gridbaglayout der uebergebenen Component ein.
	 *
	 * @predcondition Der uebergegeben Container muss ein Gridbaglayout haben!!!
	 * @param aGridbagLayoutetContainer javax.swing.JComponent
	 * @param aComponentToAdd javax.swing.JComponent
	 * @param aGridX int
	 * @param aGridY int
	 * @param aGridWidth int
	 * @param aGridHeight int
	 * @param aFillConstraint int
	 * @param anAnchorConstraint int
	 * @param aWeightX int
	 * @param aWeightY int
	 * @param anInsets java.awt.Insets
	 */
    protected void addComponentInContainer(JComponent aGridBagLayoutetContainer, JComponent aComponentToAdd, int aGridX, int aGridY, int aGridWidth, int aGridHeigth, int aFillConstraint, int anAnchorConstraint, double aWeightX, double aWeightY, Insets anInsets) {
        GridBagConstraints theConstraint = new GridBagConstraints();
        theConstraint.gridx = aGridX;
        theConstraint.gridy = aGridY;
        theConstraint.gridwidth = aGridWidth;
        theConstraint.gridheight = aGridHeigth;
        theConstraint.fill = aFillConstraint;
        theConstraint.anchor = anAnchorConstraint;
        theConstraint.weightx = aWeightX;
        theConstraint.weighty = aWeightY;
        theConstraint.insets = anInsets;
        aGridBagLayoutetContainer.add(aComponentToAdd, theConstraint);
    }

    /**
	 * Setzt die uebergebene Componente entsprechend den Constraintparametern
	 * in das Gridbaglayout des Panels ein.
	 *
	 * @precondition Das Panel hat ein GridBagLayout. Dies ist auch so im
	 * initialize definiert und darf von Unterklassen nicht geaendert werden.
	 *
	 * @param aComponent javax.swing.JComponent
	 * @param aGridX int
	 * @param aGridY int
	 * @param aGridWidth int
	 * @param aGridHeight int
	 * @param aFillConstraint int
	 * @param anAnchorConstraint int
	 * @param aWeightX int
	 * @param aWeightY int
	 * @param anInsets java.awt.Insets
	 */
    protected void addComponentInLayout(JComponent aComponent, int aGridX, int aGridY, int aGridWidth, int aGridHeigth, int aFillConstraint, int anAnchorConstraint, double aWeightX, double aWeightY, Insets anInsets) {
        GridBagConstraints theConstraint = new GridBagConstraints();
        theConstraint.gridx = aGridX;
        theConstraint.gridy = aGridY;
        theConstraint.gridwidth = aGridWidth;
        theConstraint.gridheight = aGridHeigth;
        theConstraint.fill = aFillConstraint;
        theConstraint.anchor = anAnchorConstraint;
        theConstraint.weightx = aWeightX;
        theConstraint.weighty = aWeightY;
        theConstraint.insets = anInsets;
        this.getContentPane().add(aComponent, theConstraint);
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            CommonObjects.getInstance().getConnection().close();
        } catch (SQLException e1) {
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    public void disableMainFrame() {
        this.getGlassPane().setVisible(true);
        this.getRootPane().setEnabled(false);
    }

    /**
	 * Setzt den Mainframe enabled. 
	 * @see disableMainFrame
	 */
    public void enableMainFrame() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                CommonObjects.getInstance().getMainAM().getGlassPane().setVisible(false);
            }
        });
        this.getRootPane().setEnabled(true);
    }

    /**
	 * Liefert die Liste mit den unerkannten Mp3 Dateien
	 * @return
	 */
    public List<UnknownMp3> getUnknownMp3ListFromDB() {
        UnknownMp3Dao theDao = new UnknownMp3Dao();
        theDao.putSelect();
        return theDao.executeQuery();
    }
}
