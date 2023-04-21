package viewer.geometry.manage;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.TreeCellEditor;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class GeometryCellEditor extends AbstractCellEditor implements TreeCellEditor {

    private static final long serialVersionUID = -3728440816616354346L;

    private JTextField textField = new JTextField();

    /**
	 * Default constructor
	 */
    public GeometryCellEditor() {
        textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing();
                }
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= 3;
        }
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        super.stopCellEditing();
        System.out.println("stop: " + textField.getText());
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        super.cancelCellEditing();
        fireEditingCanceled();
        System.out.println("cancel: " + textField.getText());
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        textField.setText(value.toString());
        int width = tree.getWidth();
        Container jsp = tree.getParent();
        if (jsp instanceof JViewport) {
            width = jsp.getWidth();
        }
        Dimension dim = textField.getPreferredSize();
        textField.setPreferredSize(new Dimension(width, (int) dim.getHeight()));
        return textField;
    }
}
