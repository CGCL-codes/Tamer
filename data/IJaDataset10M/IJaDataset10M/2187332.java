package com.ctext.ite.gui.main;

import com.ctext.ite.project.ITEProject;
import com.ctext.ite.utils.LocaleReader;
import com.ctext.ite.utils.Logger;
import com.ctext.ite.utils.StringHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import org.omegat.core.Core;

/**
 * The Toolbar for the ITE.
 * @author W. Fourie
 */
public class Toolbar extends JToolBar implements ActionListener {

    private JButton restartBut;

    private JButton prefBut;

    private JButton createTransBut;

    private JButton undoBut;

    private JButton redoBut;

    private JButton cutBut;

    private JButton copyBut;

    private JButton pasteBut;

    private JButton nextSegBut;

    private JButton prevSegBut;

    private JButton insertBut;

    public JButton insertDiacBut;

    private JButton tagPaintBut;

    private JButton tagValBut;

    private JButton splitSegBut;

    private JButton mergeSegBut;

    private JButton helpBut;

    public JMenu diacriticsPopupMenu;

    public JMenuItem[] diacriticsPopupItems;

    private ActionHandler actionHandler;

    /**
     * Initialises the Toolbar
     * @param ah
     */
    public Toolbar(ActionHandler ah) {
        actionHandler = ah;
        restartBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/01_restart.jpg")));
        prefBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/02_pref.jpg")));
        createTransBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/03_create.jpg")));
        undoBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/04_undo.jpg")));
        redoBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/05_redo.jpg")));
        cutBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/06_cut.jpg")));
        copyBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/07_copy.jpg")));
        pasteBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/08_paste.jpg")));
        nextSegBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/09_next.jpg")));
        prevSegBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/10_previous.jpg")));
        insertBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/11_tm.jpg")));
        insertDiacBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/12_diacritics.jpg")));
        tagPaintBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/13_painter.jpg")));
        tagValBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/14_validator.jpg")));
        mergeSegBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/15_merge.jpg")));
        splitSegBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/16_split.jpg")));
        helpBut = new JButton(new ImageIcon(getClass().getResource("/com/ctext/ite/gui/resources/17_help.jpg")));
        restartBut.setToolTipText(StringHandler.getString("TB_RESTART"));
        prefBut.setToolTipText(StringHandler.getString("TB_PREFS"));
        createTransBut.setToolTipText(StringHandler.getString("TB_COMPILE"));
        undoBut.setToolTipText(StringHandler.getString("TB_UNDO"));
        redoBut.setToolTipText(StringHandler.getString("TB_REDO"));
        cutBut.setToolTipText(StringHandler.getString("TB_CUT"));
        copyBut.setToolTipText(StringHandler.getString("TB_COPY"));
        pasteBut.setToolTipText(StringHandler.getString("TB_PASTE"));
        nextSegBut.setToolTipText(StringHandler.getString("TB_NEXT"));
        prevSegBut.setToolTipText(StringHandler.getString("TB_PREVIOUS"));
        insertBut.setToolTipText(StringHandler.getString("TB_INSERT_TRANS"));
        insertDiacBut.setToolTipText(StringHandler.getString("TB_INSERT_DIAC"));
        tagPaintBut.setToolTipText(StringHandler.getString("TB_PAINT"));
        tagValBut.setToolTipText(StringHandler.getString("TB_VALIDATE"));
        splitSegBut.setToolTipText(StringHandler.getString("TB_SPLIT"));
        mergeSegBut.setToolTipText(StringHandler.getString("TB_MERGE"));
        helpBut.setToolTipText(StringHandler.getString("TB_HELP"));
        restartBut.addActionListener(this);
        prefBut.addActionListener(this);
        createTransBut.addActionListener(this);
        undoBut.addActionListener(this);
        redoBut.addActionListener(this);
        cutBut.addActionListener(this);
        copyBut.addActionListener(this);
        pasteBut.addActionListener(this);
        nextSegBut.addActionListener(this);
        prevSegBut.addActionListener(this);
        insertBut.addActionListener(this);
        insertDiacBut.addActionListener(this);
        tagPaintBut.addActionListener(this);
        tagValBut.addActionListener(this);
        splitSegBut.addActionListener(this);
        mergeSegBut.addActionListener(this);
        helpBut.addActionListener(this);
        this.add(restartBut);
        this.add(prefBut);
        this.add(createTransBut);
        this.addSeparator();
        this.add(undoBut);
        this.add(redoBut);
        this.add(cutBut);
        this.add(copyBut);
        this.add(pasteBut);
        this.addSeparator();
        this.add(nextSegBut);
        this.add(prevSegBut);
        this.addSeparator();
        this.add(insertBut);
        this.add(insertDiacBut);
        this.addSeparator();
        this.add(tagPaintBut);
        this.add(tagValBut);
        this.add(splitSegBut);
        this.add(mergeSegBut);
        this.addSeparator();
        this.add(helpBut);
        this.setRollover(true);
        setActionCommands();
    }

    /**
    * Sets the Menu and Toolbar items' value which is only applicable when in
    * the Translate panel.
    */
    public void ableMenuOptions(boolean value) {
        createTransBut.setEnabled(value);
        undoBut.setEnabled(value);
        redoBut.setEnabled(value);
        cutBut.setEnabled(value);
        copyBut.setEnabled(value);
        pasteBut.setEnabled(value);
        nextSegBut.setEnabled(value);
        prevSegBut.setEnabled(value);
        insertBut.setEnabled(value);
        insertDiacBut.setEnabled(value);
        tagPaintBut.setEnabled(value);
        tagValBut.setEnabled(value);
        splitSegBut.setEnabled(value);
        mergeSegBut.setEnabled(value);
    }

    /**
     * Sets project relevant toolbar settings:
     * <br> Merge/Split available.
     * <br> Insert Diacritics available.     
     */
    public void setToolbarSettings(ITEProject project) {
        splitSegBut.setVisible(project.segmentation);
        mergeSegBut.setVisible(project.segmentation);
        String[] diacs = LocaleReader.localeReader.diacritics.get(project.locales.targetLanguage);
        if (diacs != null) {
            diacriticsPopupMenu = new JMenu(project.locales.targetLanguage);
            diacriticsPopupItems = new JMenuItem[diacs.length];
            for (int j = 0; j < diacs.length; j++) {
                diacriticsPopupItems[j] = new JMenuItem(diacs[j]);
                diacriticsPopupItems[j].addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Core.getEditor().insertText(e.getActionCommand());
                        Core.getEditor().requestFocus();
                    }
                });
                diacriticsPopupMenu.add(diacriticsPopupItems[j]);
            }
            insertDiacBut.setVisible(true);
        } else {
            insertDiacBut.setVisible(false);
        }
    }

    /**
    * Set 'actionCommand' for all menu items.
    * Shamelessly stolen from OmegaT (org.omegat.gui.main.MainWindowMenu.java)
    */
    private void setActionCommands() {
        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (JButton.class.isAssignableFrom(f.getType())) {
                    JButton toolbarItem = (JButton) f.get(this);
                    toolbarItem.setActionCommand(f.getName());
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.logger.log(Level.SEVERE, "Toolbar: Unable to set ActionCommands", ex);
        }
    }

    /**
     * Dispatches the action to be performed by the ActionHandler.
     * Shamelessly stolen from OmegaT (org.omegat.gui.main.MainWindowMenu.java)
     * @param e ActionEvent information
     */
    public void actionPerformed(ActionEvent e) {
        JButton toolbarItem = (JButton) e.getSource();
        String action = toolbarItem.getActionCommand();
        String methodName = action + "ActionPerformed";
        Method method = null;
        try {
            method = actionHandler.getClass().getMethod(methodName);
            method.invoke(actionHandler);
        } catch (Exception ex) {
            Logger.logger.log(Level.SEVERE, "Error execute method", ex);
        }
    }
}
