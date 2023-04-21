package org.fudaa.ebli.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibFile;
import com.memoire.bu.BuFileChooser;
import org.fudaa.ebli.tableau.EbliCellDecorator;

/**
 * @author deniger
 * @version $Id: EbliCellFileEditor.java,v 1.4 2004-10-15 06:54:55 deniger Exp $
 */
public class EbliCellFileEditor extends JButton implements TableCellEditor, ActionListener {

    protected transient ChangeEvent changeEvent;

    BuFileChooser bfc_;

    int fileChooserSelectionMode_;

    boolean multipleSelection_;

    private File baseDir_;

    boolean onlyName_;

    EbliCellDecorator decorator_;

    String value_;

    boolean doubleClick_;

    public EbliCellFileEditor() {
        this(null);
    }

    /**
   * 
   */
    public EbliCellFileEditor(EbliCellDecorator _deco) {
        setOpaque(false);
        setEnabled(true);
        addActionListener(this);
        decorator_ = _deco;
        setBorder(UIManager.getBorder("Label.border"));
        setFont(UIManager.getFont("Table.font"));
        setHorizontalAlignment(0);
        setAlignmentX(0);
    }

    /**
   *
   */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setValue((String) value);
        if (decorator_ != null) decorator_.decore(this, table, value, row, column);
        return this;
    }

    public void setValue(String _o) {
        value_ = _o;
        setText(value_);
    }

    /**
   *
   */
    public Object getCellEditorValue() {
        return value_;
    }

    /**
   *
   */
    public boolean isCellEditable(EventObject anEvent) {
        if (!doubleClick_) return true;
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= 2;
        }
        return true;
    }

    /**
   *
   */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
  *
  */
    public void cancelCellEditing() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    /**
   *
   */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
   *
   */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    public boolean stopCellEditing() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
        return false;
    }

    /**
   *
   */
    public void actionPerformed(ActionEvent _ae) {
        if (bfc_ == null) {
            bfc_ = new EbliFileChooser(false);
        }
        bfc_.setFileSelectionMode(fileChooserSelectionMode_);
        bfc_.setMultiSelectionEnabled(multipleSelection_);
        if ((baseDir_ != null) && ((value_ != null))) {
            bfc_.setCurrentDirectory(CtuluLibFile.getAbsolutePath(baseDir_, value_).getParentFile());
        }
        int r = bfc_.showOpenDialog(EbliCellFileEditor.this);
        if (r == JFileChooser.APPROVE_OPTION) {
            if (onlyName_) setValue(bfc_.getSelectedFile().getName()); else if (baseDir_ == null) {
                setValue(bfc_.getSelectedFile().getAbsolutePath());
            } else {
                setValue(CtuluLibFile.getRelativeFile(bfc_.getSelectedFile(), baseDir_, 3));
            }
        }
        stopCellEditing();
    }

    /**
       *
       */
    public EbliCellDecorator getDecorator() {
        return decorator_;
    }

    public void setDecorator(EbliCellDecorator _decorator) {
        decorator_ = _decorator;
    }

    /**
               *
               */
    public boolean isDoubleClick() {
        return doubleClick_;
    }

    /**
               *
               */
    public void setDoubleClick(boolean _b) {
        doubleClick_ = _b;
    }

    /**
   *
   */
    public int getFileChooserSelectionMode() {
        return fileChooserSelectionMode_;
    }

    /**
   *
   */
    public File getBaseDir() {
        return baseDir_;
    }

    /**
   *
   */
    public boolean isMultipleSelection() {
        return multipleSelection_;
    }

    /**
   *
   */
    public boolean isOnlyName() {
        return onlyName_;
    }

    /**
   *
   */
    public void setFileChooserSelectionMode(int _i) {
        fileChooserSelectionMode_ = _i;
    }

    /**
   *
   */
    public void setBaseDir(File _file) {
        baseDir_ = _file;
    }

    /**
   *
   */
    public void setMultipleSelection(boolean _b) {
        multipleSelection_ = _b;
    }

    /**
   *
   */
    public void setOnlyName(boolean _b) {
        onlyName_ = _b;
    }

    /**
   *
   */
    public String getValue() {
        return value_;
    }
}
