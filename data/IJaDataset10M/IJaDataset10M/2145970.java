package net.sourceforge.ehep.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.editors.*;
import net.sourceforge.ehep.core.*;
import net.sourceforge.ehep.gui.*;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class CursorKeyListener extends KeyAdapter {

    private final HexEditor hexEditor;

    private final HexEditorControl hexEditorControl;

    private final TableCursor cursor;

    private final ControlEditor editor;

    private Font font;

    private Text text = null;

    private CellEditorKeyListener cellEditorKeyListener = null;

    private int eventColumnIndex;

    private HexTableItem eventItem;

    /**
	 * Creates a key listener for table cursor
	 * @param hexEditorControl
	 * @param hexEditor
	 * @param cursor
	 * @param editor
	 */
    public CursorKeyListener(final HexEditorControl hexEditorControl, final HexEditor hexEditor, final TableCursor cursor, final ControlEditor editor) {
        super();
        this.hexEditorControl = hexEditorControl;
        this.hexEditor = hexEditor;
        this.cursor = cursor;
        this.editor = editor;
        FontData fontData = PreferenceConverter.getFontData(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_FONT);
        this.font = new Font(Display.getCurrent(), fontData);
    }

    /**
	 * Event handler for "key pressed" event
	 * @param e an event containing information about the key press
	 */
    public void keyPressed(KeyEvent e) {
        if (hexEditor.isReadOnly()) {
            return;
        }
        if (e.keyCode == SWT.INSERT) {
            keyInsert();
            hexEditorControl.updateStatusPanel();
            return;
        }
        if (e.keyCode == SWT.DEL) {
            keyDelete();
            hexEditorControl.updateStatusPanel();
            return;
        }
        if (e.character != ',' && Character.digit(e.character, 16) == -1) {
            return;
        }
        TableItem row = cursor.getRow();
        int column = cursor.getColumn();
        if (column == 0 || column > EHEP.TABLE_NUM_DATA_COLUMNS) {
            return;
        }
        if (row.getText(column).compareTo(EHEP.TABLE_EMPTY_CELL) == 0) {
            return;
        }
        text = new Text(cursor, SWT.NONE);
        text.insert("" + e.character);
        text.setFont(font);
        text.setBackground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_BACKGROUND_EDITOR)));
        text.setForeground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_FOREGROUND_EDITOR)));
        text.setTextLimit(2);
        editor.setEditor(text);
        text.setFocus();
        HexTable table = hexEditorControl.getHexTable();
        int rowIndex = table.getSelectionIndex();
        eventColumnIndex = cursor.getColumn();
        eventItem = table.getItem(rowIndex);
        cellEditorKeyListener = new CellEditorKeyListener(hexEditorControl, hexEditor, cursor, editor, text);
        text.addKeyListener(cellEditorKeyListener);
    }

    private void keyInsert() {
        HexTable table = hexEditorControl.getHexTable();
        int rowIndex = table.getSelectionIndex();
        int columnIndex = cursor.getColumn();
        HexTableItem item = table.getItem(rowIndex);
        if (columnIndex == 0 || columnIndex == (EHEP.TABLE_NUM_COLUMNS - 1) || item.getText(columnIndex).equals(EHEP.TABLE_EMPTY_CELL)) {
            return;
        }
        hexEditorControl.insertData(1, 0, rowIndex, columnIndex - 1, true);
        hexEditor.setDirty(true);
    }

    private void keyDelete() {
        HexTable table = hexEditorControl.getHexTable();
        int rowIndex = table.getSelectionIndex();
        int columnIndex = cursor.getColumn();
        HexTableItem item = table.getItem(rowIndex);
        if (item == null || columnIndex == 0 || columnIndex == (EHEP.TABLE_NUM_COLUMNS - 1) || item.getText(columnIndex).equals(EHEP.TABLE_EMPTY_CELL)) {
            return;
        }
        hexEditorControl.delete(rowIndex, columnIndex - 1, 1);
        hexEditor.setDirty(true);
    }

    public CellEditorKeyListener getCellEditorKeyListener() {
        return cellEditorKeyListener;
    }

    public Text getText() {
        return (text == null || text.isDisposed()) ? null : text;
    }

    public HexTableItem getEventItem() {
        return eventItem;
    }

    public int getEventColumn() {
        return eventColumnIndex;
    }
}
