package commonapp.widget;

import javax.swing.table.TableCellRenderer;

/**
   Table cell renderer classes that implement this interface can decode an
   {@link commonapp.datacon.IntfItemData} object for rendering the table cell
   that displays the object data.  This enables applications to create cell
   renderers for application-defined data types, i.e., data types not defined
   in commonapp.
*/
public interface IntfItemDataRenderer extends TableCellRenderer {
}
