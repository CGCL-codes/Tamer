package net.sourceforge.nattable.style.editor.event.action;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.selection.SelectionLayer;
import net.sourceforge.nattable.style.Style;
import net.sourceforge.nattable.style.DefaultCellStyle;
import net.sourceforge.nattable.style.UserStyles;
import net.sourceforge.nattable.style.editor.StyleEditorDialog;
import net.sourceforge.nattable.style.region.MultipleColumnRegionFilter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * Action to let the user edit a style for whatever columns she has selected.
 */
public class EditStyleForSelectedColumnsAction extends EditUserDefinedStylesAction {

    public EditStyleForSelectedColumnsAction(UserStyles userStyles, SelectionLayer selectionStateProvider) {
        super(userStyles, selectionStateProvider);
    }

    public void run(NatTable natTable, MouseEvent e) {
        int[] selectedColumns = getSelectionLayer().getSelectedColumns();
        if (selectedColumns.length == 0) return;
        MultipleColumnRegionFilter columns = new MultipleColumnRegionFilter(selectedColumns);
        StyleEditorDialog styleEditor = new StyleEditorDialog(new Shell());
        styleEditor.setStyleTarget("Selected Columns");
        if (!isDestructiveStyleEditing()) {
            Style newStyle = styleEditor.edit(new DefaultCellStyle(), 0, 0);
            if (newStyle != null) getUserStyles().assign(columns, newStyle);
        } else {
            UserStyles.UserDefinedStyle existingUserStyle = getUserStyles().find(columns);
            if (existingUserStyle == null) {
                Style newStyle = styleEditor.edit(new DefaultCellStyle(), 0, 0);
                if (newStyle != null) getUserStyles().assign(columns, newStyle);
            } else {
                styleEditor.edit(existingUserStyle.getStyle(), 0, 0);
            }
        }
    }
}
