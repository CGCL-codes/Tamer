package uk.ac.lkl.migen.system.expresser.ui;

import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.common.util.value.NumericValue;
import uk.ac.lkl.migen.system.ExpresserLauncher;
import uk.ac.lkl.migen.system.expresser.ExternalInterface;
import uk.ac.lkl.migen.system.expresser.ui.menu.*;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

public class ObjectSetCanvasMenuBar<N extends NumericValue<N>> extends JMenuBar {

    private JMenu tasksMenu;

    public ObjectSetCanvasMenuBar(ExpresserTabbedPanel tabbedPanel) {
        JMenu fileMenu = new JMenu(MiGenUtilities.getLocalisedMessage("File"));
        ExternalInterface.setFileMenu(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        if (!MiGenConfiguration.isSingleActivityOnly()) {
            LoadModelTabMenuItem loadTabMenuItem = new LoadModelTabMenuItem(tabbedPanel);
            fileMenu.add(loadTabMenuItem);
            ExternalInterface.setLoadTabMenuItem(loadTabMenuItem);
            ModelSaveAsTabMenuItem saveTabMenuItem = new ModelSaveAsTabMenuItem(tabbedPanel);
            fileMenu.add(saveTabMenuItem);
            ExternalInterface.setSaveTabMenuItem(saveTabMenuItem);
        }
        LoadActivityDocumentTabMenuItem loadActivityDocumentTabMenuItem = new LoadActivityDocumentTabMenuItem(tabbedPanel);
        fileMenu.add(loadActivityDocumentTabMenuItem);
        ExternalInterface.setLoadActivityDocumentTabMenuItem(loadActivityDocumentTabMenuItem);
        ActivityDocumentSaveAsTabMenuItem activityDocumentSaveAsTabMenuItem = new ActivityDocumentSaveAsTabMenuItem(tabbedPanel);
        fileMenu.add(activityDocumentSaveAsTabMenuItem);
        ExternalInterface.setActivityDocumentSaveTabMenuItem(activityDocumentSaveAsTabMenuItem);
        if (!MiGenConfiguration.isSingleActivityOnly()) {
            CloseTabMenuItem closeTabMenuItem = new CloseTabMenuItem(tabbedPanel);
            fileMenu.add(closeTabMenuItem);
            ExternalInterface.setCloseTabMenuItem(closeTabMenuItem);
        }
        JMenuItem exitMenuItem = new JMenuItem(MiGenUtilities.getLocalisedMessage("ExitMenuLabel"));
        fileMenu.add(exitMenuItem);
        ExternalInterface.setExitMenuItem(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ExpresserLauncher.closeExpresserInterface();
            }
        });
        add(fileMenu);
        JMenu editMenu = new JMenu(MiGenUtilities.getLocalisedMessage("Edit"));
        ExternalInterface.setEditMenu(editMenu);
        editMenu.setMnemonic(KeyEvent.VK_E);
        SelectAllMenuItem selectAllMenuItem = new SelectAllMenuItem(tabbedPanel);
        editMenu.add(selectAllMenuItem);
        ExternalInterface.setSelectAllMenuItem(selectAllMenuItem);
        final UndoMenuItem undoDeleteMenuItem = new UndoMenuItem(tabbedPanel);
        editMenu.add(undoDeleteMenuItem);
        ExternalInterface.setUndoDeleteMenuItem(undoDeleteMenuItem);
        final RedoMenuItem redoDeleteMenuItem = new RedoMenuItem(tabbedPanel);
        editMenu.add(redoDeleteMenuItem);
        ExternalInterface.setRedoDeleteMenuItem(redoDeleteMenuItem);
        ItemListener itemListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                undoDeleteMenuItem.setEnabled(undoDeleteMenuItem.canBePerformed());
                redoDeleteMenuItem.setEnabled(redoDeleteMenuItem.canBePerformed());
            }
        };
        editMenu.addItemListener(itemListener);
        add(editMenu);
    }

    public JMenu getTasksMenu() {
        return tasksMenu;
    }
}
