package com.genia.toolbox.portlet.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.genia.toolbox.basics.editor.gui.AbstractMenuBar;
import com.genia.toolbox.portlet.editor.PortletEditorController;
import com.genia.toolbox.portlet.editor.model.bean.PortletInitialSettings;
import com.genia.toolbox.portlet.editor.model.document.impl.DocumentModel;
import com.genia.toolbox.portlet.editor.model.portlet.impl.ContainerPortletModel;
import com.genia.toolbox.portlet.editor.model.portlet.impl.PortletModel;
import com.genia.toolbox.web.portlet.description.ContainerPortletDescription.LayoutDirection;

/**
 * The main menu bar.
 */
@SuppressWarnings("serial")
public class PortletEditorMenuBar extends AbstractMenuBar<PortletEditorGUI, PortletEditorController, DocumentModel, PortletModel, PortletInitialSettings> {

    /**
   * The add portlet item.
   */
    private JMenuItem itemAddPortlet = null;

    /**
   * The delete portlet item.
   */
    private JMenuItem itemDeletePortlet = null;

    /**
   * The move up item.
   */
    private JMenuItem itemMoveUp = null;

    /**
   * The move down item.
   */
    private JMenuItem itemMoveDown = null;

    /**
   * The move right item.
   */
    private JMenuItem itemMoveRight = null;

    /**
   * The move left item.
   */
    private JMenuItem itemMoveLeft = null;

    /**
   * Constructor.
   * 
   * @param gui
   *          The gui.
   */
    public PortletEditorMenuBar(PortletEditorGUI gui) {
        super(gui);
    }

    /**
   * Initialise the specific commands of the menu bar.
   */
    @Override
    public void initialiseSpecificCommands() {
        JMenu menuPortlet = new JMenu(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.menuPortlet"));
        this.add(menuPortlet);
        this.itemAddPortlet = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemAddPortlet"));
        this.itemAddPortlet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        URL url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/add.gif");
        this.itemAddPortlet.setIcon(new ImageIcon(url));
        this.itemAddPortlet.addActionListener(this);
        menuPortlet.add(this.itemAddPortlet);
        this.itemDeletePortlet = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemDeletePortlet"));
        url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/delete.gif");
        this.itemDeletePortlet.setIcon(new ImageIcon(url));
        this.itemDeletePortlet.addActionListener(this);
        menuPortlet.add(this.itemDeletePortlet);
        menuPortlet.addSeparator();
        this.itemMoveUp = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemMoveUp"));
        this.itemMoveUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/moveUp.gif");
        this.itemMoveUp.setIcon(new ImageIcon(url));
        this.itemMoveUp.addActionListener(this);
        menuPortlet.add(this.itemMoveUp);
        this.itemMoveDown = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemMoveDown"));
        this.itemMoveDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/moveDown.gif");
        this.itemMoveDown.setIcon(new ImageIcon(url));
        this.itemMoveDown.addActionListener(this);
        menuPortlet.add(this.itemMoveDown);
        this.itemMoveRight = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemMoveRight"));
        this.itemMoveRight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/moveRight.gif");
        this.itemMoveRight.setIcon(new ImageIcon(url));
        this.itemMoveRight.addActionListener(this);
        menuPortlet.add(this.itemMoveRight);
        this.itemMoveLeft = new JMenuItem(this.getGui().getController().notifyTranslation("com.genia.portlet.editor.gui.MainMenuBar.itemMoveLeft"));
        this.itemMoveLeft.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        url = PortletEditorToolBar.class.getClassLoader().getResource("com/genia/toolbox/basics/editor/image/moveLeft.gif");
        this.itemMoveLeft.setIcon(new ImageIcon(url));
        this.itemMoveLeft.addActionListener(this);
        menuPortlet.add(this.itemMoveLeft);
    }

    /**
   * Handle the action events.
   * 
   * @param e
   *          the action event.
   */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        if (source.equals(this.itemAddPortlet)) {
            this.getGui().addSubElement(this.getGui().getSelectedElement());
        } else if (source.equals(this.itemDeletePortlet)) {
            this.getGui().deleteSubElement(this.getGui().getSelectedElement());
        } else if (source.equals(this.itemMoveUp)) {
            this.getGui().moveUpOrRightSubElement(this.getGui().getSelectedElement());
        } else if (source.equals(this.itemMoveDown)) {
            this.getGui().moveDownOrLeftSubElement(this.getGui().getSelectedElement());
        } else if (source.equals(this.itemMoveRight)) {
            this.getGui().moveUpOrRightSubElement(this.getGui().getSelectedElement());
        } else if (source.equals(this.itemMoveLeft)) {
            this.getGui().moveDownOrLeftSubElement(this.getGui().getSelectedElement());
        }
    }

    /**
   * Refresh the access of the items.
   */
    @Override
    public void refresh() {
        super.refresh();
        PortletModel model = this.getGui().getSelectedElement();
        if (model == null) {
            this.itemAddPortlet.setEnabled(false);
            this.itemDeletePortlet.setEnabled(false);
            this.itemMoveUp.setEnabled(false);
            this.itemMoveDown.setEnabled(false);
            this.itemMoveRight.setEnabled(false);
            this.itemMoveLeft.setEnabled(false);
        } else {
            if (model instanceof ContainerPortletModel) {
                ContainerPortletModel containerPortletModel = (ContainerPortletModel) model;
                int nbDisplayedSubPortlets = containerPortletModel.getNbSubPortlets();
                if (nbDisplayedSubPortlets >= containerPortletModel.getMaxNbSubPortlets()) {
                    this.itemAddPortlet.setEnabled(false);
                } else {
                    this.itemAddPortlet.setEnabled(true);
                }
            } else {
                this.itemAddPortlet.setEnabled(false);
            }
            if (model.isSubElement()) {
                this.itemDeletePortlet.setEnabled(true);
                if (LayoutDirection.HORIZONTAL.equals(model.getParentDirection())) {
                    this.itemMoveUp.setEnabled(false);
                    this.itemMoveDown.setEnabled(false);
                    this.itemMoveRight.setEnabled(true);
                    this.itemMoveLeft.setEnabled(true);
                } else {
                    this.itemMoveUp.setEnabled(true);
                    this.itemMoveDown.setEnabled(true);
                    this.itemMoveRight.setEnabled(false);
                    this.itemMoveLeft.setEnabled(false);
                }
            } else {
                this.itemDeletePortlet.setEnabled(false);
                this.itemMoveUp.setEnabled(false);
                this.itemMoveDown.setEnabled(false);
                this.itemMoveRight.setEnabled(false);
                this.itemMoveLeft.setEnabled(false);
            }
        }
    }
}
