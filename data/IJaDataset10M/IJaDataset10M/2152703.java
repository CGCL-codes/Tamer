package atp.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import atp.MainFrame;

/**
 * @author DaJunkie
 * @version $Revision: 1.1 $
 */
public class ToolBar extends JToolBar {

    private JButton neuButton = null;

    private JButton oeffnenButton = null;

    private JButton schliessenButton = null;

    private JButton speichernButton = null;

    private JButton importButton = null;

    private JButton undoButton = null;

    private JButton redoButton = null;

    private JButton hilfeButton = null;

    private MainFrame parent = null;

    /**
	 * This method initializes
	 * 
	 * @param _parent
	 *            MainFrame
	 */
    public ToolBar(MainFrame _parent) {
        super();
        parent = _parent;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setFloatable(false);
        this.add(getNeuButton());
        this.add(getOeffnenButton());
        this.add(getSpeichernButton());
        this.add(getImportButton());
        this.addSeparator();
        this.add(getUndoButton());
        this.add(getRedoButton());
    }

    /**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getNeuButton() {
        if (neuButton == null) {
            neuButton = new JButton();
            neuButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/filenew.gif")));
            neuButton.setToolTipText("Neu");
            neuButton.addActionListener(parent);
            neuButton.setActionCommand("NEW_SCHEDULE");
        }
        return neuButton;
    }

    /**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getOeffnenButton() {
        if (oeffnenButton == null) {
            oeffnenButton = new JButton();
            oeffnenButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/open.gif")));
            oeffnenButton.setToolTipText("�ffnen");
            oeffnenButton.addActionListener(parent);
            oeffnenButton.setActionCommand("OPEN_SCHEDULE");
        }
        return oeffnenButton;
    }

    /**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getSchliessenButton() {
        if (schliessenButton == null) {
            schliessenButton = new JButton();
            schliessenButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/close.gif")));
            schliessenButton.setToolTipText("Schlie�en");
            schliessenButton.addActionListener(parent);
        }
        return schliessenButton;
    }

    /**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getSpeichernButton() {
        if (speichernButton == null) {
            speichernButton = new JButton();
            speichernButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/save.gif")));
            speichernButton.setToolTipText("Speichern");
            speichernButton.addActionListener(parent);
            speichernButton.setActionCommand("SAVE_SCHEDULE");
        }
        return speichernButton;
    }

    /**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getImportButton() {
        if (importButton == null) {
            importButton = new JButton();
            importButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/export.gif")));
            importButton.addActionListener(parent);
            importButton.setActionCommand("IMPORT");
            importButton.setToolTipText("Import");
        }
        return importButton;
    }

    /**
	 * This method initializes jButton5
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getUndoButton() {
        if (undoButton == null) {
            undoButton = new JButton();
            undoButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/undo.gif")));
            undoButton.setToolTipText("Undo");
            undoButton.addActionListener(parent);
            undoButton.setActionCommand("Undo");
        }
        return undoButton;
    }

    /**
	 * This method initializes jButton6
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getRedoButton() {
        if (redoButton == null) {
            redoButton = new JButton();
            redoButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/redo.gif")));
            redoButton.setToolTipText("Redo");
            redoButton.addActionListener(parent);
            redoButton.setActionCommand("Redo");
        }
        return redoButton;
    }

    /**
	 * This method initializes jButton7
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getHilfeButton() {
        if (hilfeButton == null) {
            hilfeButton = new JButton();
            hilfeButton.setIcon(new ImageIcon(getClass().getResource("/atp/images/help.gif")));
            hilfeButton.addActionListener(parent);
            hilfeButton.setActionCommand("HELP");
            hilfeButton.setToolTipText("Hilfe");
        }
        return hilfeButton;
    }
}
