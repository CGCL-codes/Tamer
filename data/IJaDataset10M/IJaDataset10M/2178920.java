package ru.amse.soultakov.ereditor.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import ru.amse.soultakov.ereditor.controller.DiagramEditor;
import ru.amse.soultakov.ereditor.controller.undo.commands.RemoveViewableCommand;
import ru.amse.soultakov.ereditor.view.SelectedItems;
import ru.amse.soultakov.ereditor.view.SelectedItemsListener;

/**
 * @author sma
 * 
 */
@SuppressWarnings("serial")
public class RemoveSelectionAction extends AbstractAction {

    private DiagramEditor diagramEditor;

    /**
     * @param diagramEditor
     * @param name
     * @param removeIcon
     */
    public RemoveSelectionAction(DiagramEditor diagramEditor, String name, ImageIcon removeIcon) {
        super(name, removeIcon);
        this.diagramEditor = diagramEditor;
        setEnabled(false);
        this.diagramEditor.getSelectedItems().addListener(new SelectedItemsListener() {

            public void selectionChanged(SelectedItems selection) {
                setEnabled(!selection.isEmpty());
            }
        });
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
    }

    public void actionPerformed(ActionEvent e) {
        diagramEditor.getCommandManager().executeCommand(new RemoveViewableCommand(diagramEditor, diagramEditor.getSelectedItems().asSet()));
        diagramEditor.getSelectedItems().clear();
        diagramEditor.repaint();
    }
}
