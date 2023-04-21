package org.qsari.effectopedia.gui.toolbars;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.awt.DropDownToggleButton;
import org.qsari.effectopedia.core.Effectopedia;
import org.qsari.effectopedia.core.Effectopedia.DataSourceChange;
import org.qsari.effectopedia.core.Effectopedia.DataSourceChangeListener;
import org.qsari.effectopedia.data.DataSource;
import org.qsari.effectopedia.data.XMLFileDS;
import org.qsari.effectopedia.defaults.DefaultGOSettings;
import org.qsari.effectopedia.defaults.DefaultServerSettings;
import org.qsari.effectopedia.gui.AdjustableUI;
import org.qsari.effectopedia.gui.EffectopediaUI;
import org.qsari.effectopedia.gui.UIResources;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class CommandToolbarUI extends javax.swing.JToolBar implements AdjustableUI, DataSourceChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JButton jbFileOpen;

    private JButton jbFileSave;

    private JPopupMenu jpmSave;

    private DropDownToggleButton ddtbSave;

    private Action aSaveAction;

    private Action aPublishAction;

    public static final int FILE_OPEN = 0x0001;

    public static final int FILE_SAVE = 0x0002;

    public static final int PUBLISH = 0x0004;

    public static final int ALL = 0xFFFF;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new CommandToolbarUI(CommandToolbarUI.ALL));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public CommandToolbarUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(100, 36));
            this.setMinimumSize(new java.awt.Dimension(72, 36));
            this.setMaximumSize(new java.awt.Dimension(100, 36));
            imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            jbFileOpen = createButton(createAction("open XML file", UIResources.imageFileOpen, "Open XML File", "C", FILE_OPEN), FILE_OPEN, ALL);
            jbFileSave = createButton(createAction("save XML file", UIResources.imageFileSave, "Save XML File", "L", FILE_SAVE), FILE_SAVE, ALL);
            addKeyListener(new CommandKeys());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommandToolbarUI(int buttons) {
        super();
        this.setMinimumSize(new java.awt.Dimension(72, 36));
        imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        jbFileOpen = createButton(createAction("open XML file", UIResources.imageFileOpen, "Open XML File", "C", FILE_OPEN), FILE_OPEN, buttons);
        jpmSave = new JPopupMenu();
        jpmSave.add(aSaveAction = createAction("save XML file", UIResources.imageFileSave, "Save XML File", "L", FILE_SAVE));
        jpmSave.add(aPublishAction = createAction("publish changes", UIResources.imagePublish, "Publish", "P", PUBLISH));
        ddtbSave = createDropDownButton(UIResources.imageFileSave, jpmSave, PUBLISH, aSaveAction);
        addKeyListener(new CommandKeys());
    }

    public void dataSourceChanged(DataSourceChange evt) {
        DataSource data = Effectopedia.EFFECTOPEDIA.getData();
        if ((data instanceof XMLFileDS) && (((XMLFileDS) data).isRemoteFile())) ddtbSave.setAction(aPublishAction); else ddtbSave.setAction(aSaveAction);
    }

    public class CommandAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public CommandAction(String actionName, Icon icon, int actionCode, String description) {
            putValue(Action.NAME, actionName);
            putValue(Action.SMALL_ICON, icon);
            putValue("actionCode", actionCode);
            putValue(Action.SHORT_DESCRIPTION, description);
        }

        public void actionPerformed(ActionEvent event) {
            int actionCode = (Integer) getValue("actionCode");
            switch(actionCode) {
                case FILE_OPEN:
                    {
                        if (useFileDialog) {
                            FileDialog f = new FileDialog(EffectopediaUI.frame, "Open Effectopedia Adverse Outcom Pathway AOP(Z-zipped) XML files", FileDialog.LOAD);
                            f.setVisible(true);
                            Effectopedia.EFFECTOPEDIA.loadFromLocalXMLFile(f.getDirectory() + f.getFile());
                            f.dispose();
                        } else {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setCurrentDirectory(new File("."));
                            chooser.setFileFilter(new FileNameExtensionFilter("Effectopedia Adverse Outcom Pathway AOP(Z-zipped) XML files", "xml", "aop", "aopz"));
                            int returnVal = chooser.showOpenDialog(EffectopediaUI.frame);
                            if (returnVal == JFileChooser.APPROVE_OPTION) Effectopedia.EFFECTOPEDIA.loadFromLocalXMLFile(chooser.getSelectedFile().getAbsolutePath());
                        }
                        break;
                    }
                case FILE_SAVE:
                    {
                        if (useFileDialog) {
                            FileDialog f = new FileDialog(EffectopediaUI.frame, "Save Effectopedia Adverse Outcom Pathway AOP(Z-zipped) XML files", FileDialog.SAVE);
                            f.setVisible(true);
                            Effectopedia.EFFECTOPEDIA.saveAsXMLFile(f.getDirectory() + f.getFile(), true);
                            f.dispose();
                        } else {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setCurrentDirectory(new File("."));
                            chooser.setFileFilter(new FileNameExtensionFilter("Effectopedia Adverse Outcom Pathway AOP(Z-zipped) XML files", "xml", "aop", "aopz"));
                            int returnVal = chooser.showSaveDialog(EffectopediaUI.frame);
                            if (returnVal == JFileChooser.APPROVE_OPTION) Effectopedia.EFFECTOPEDIA.saveAsXMLFile(chooser.getSelectedFile().getAbsolutePath(), true);
                        }
                        break;
                    }
                case PUBLISH:
                    {
                        if (Effectopedia.EFFECTOPEDIA.saveAsXMLFile(DefaultServerSettings.getCurrentRevision(), false)) DefaultServerSettings.commitRevision(String.valueOf(Effectopedia.EFFECTOPEDIA.getRevision()), String.valueOf(Effectopedia.EFFECTOPEDIA.getCurrentUserID()));
                        break;
                    }
            }
        }
    }

    public class CommandKeys implements java.awt.event.KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ALT) ;
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ALT) ;
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    private CommandAction createAction(String actionName, ImageIcon icon, String hint, String key, int actionCode) {
        CommandAction action = new CommandAction(actionName, icon, actionCode, hint);
        imap.put(KeyStroke.getKeyStroke(key), actionName);
        getActionMap().put(actionName, action);
        return action;
    }

    private JButton createButton(CommandAction action, int buttonType, int buttons) {
        if ((buttons & buttonType) != 0) {
            JButton button = new JButton(action);
            add(button);
            button.setHideActionText(DefaultGOSettings.hideActionText);
            button.setFocusPainted(false);
            return button;
        }
        return null;
    }

    public void updatePrefferedSize() {
        Dimension d = new Dimension(0, 0);
        d.setSize(72, 36);
        if (getOrientation() == HORIZONTAL) for (Component c : getComponents()) {
            Dimension componentDimension = c.getPreferredSize();
            d.width += componentDimension.width;
            if (d.height < componentDimension.height) d.height = componentDimension.height;
        } else for (Component c : getComponents()) {
            Dimension componentDimension = c.getPreferredSize();
            d.height += componentDimension.height;
            if (d.width < componentDimension.width) d.width = componentDimension.width;
        }
        setPreferredSize(d);
    }

    private DropDownToggleButton createDropDownButton(Icon icon, JPopupMenu popup, int buttonType, Action action) {
        DropDownToggleButton button = new DropDownToggleButton(icon, popup);
        add(button);
        button.setAction(action);
        button.setHideActionText(DefaultGOSettings.hideActionText);
        button.setFocusPainted(false);
        return button;
    }

    /**
		 * Adjust <code>visible</code> properties to the current and the contained
		 * components
		 * 
		 * @see AdjustableUI
		 * 
		 * @param visualOptions
		 *         an long that specifies which of the contained components are
		 *         visible
		 */
    public void adjustUI(long visualOptions) {
        this.setVisible((visualOptions & LIST_TOOLBARS) != 0);
    }

    public boolean useFileDialog = false;

    private InputMap imap;
}
