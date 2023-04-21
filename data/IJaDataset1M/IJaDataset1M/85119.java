package de.hpi.eworld.exporter.sumo.dialogs;

import java.util.List;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;

/**
 * "special" list widget which emits a signal with the current item's index 
 * if the user presses the delete button while it has focus. this way 
 * the parent widget can handle delete operations on the listwidget. 
 * @author Jonas Truemper
 *
 */
public class ListWidget extends QListWidget {

    /**
	 * is emitted when the user presses the delete button
	 */
    public Signal1<List<QListWidgetItem>> onRemoveItem = new Signal1<List<QListWidgetItem>>();

    /**
	 * Constructor
	 * @param parent The parent widget
	 */
    public ListWidget(QWidget parent) {
        super(parent);
        this.setSelectionMode(QAbstractItemView.SelectionMode.ExtendedSelection);
    }

    public void keyPressEvent(QKeyEvent e) {
        if (e.matches(QKeySequence.StandardKey.Delete)) {
            List<QListWidgetItem> selectedItems = this.selectedItems();
            if (selectedItems.size() > 0) onRemoveItem.emit(selectedItems);
        }
    }
}
