package org.plazmaforge.framework.client.swt.controls;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.TableItem;
import org.plazmaforge.framework.client.swt.views.ITableCellRenderer;

/** 
 * @author Oleh Hapon
 *
 */
public class TableProvider extends AbstractTableProvider<TableItem> implements ITableProvider<TableItem> {

    public int indexOfRow(TableItem item) {
        return item.getParent().indexOf(item);
    }

    protected void setRowText(TableItem tableItem, int columnIndex, String text) {
        tableItem.setText(columnIndex, text);
    }

    protected void setRowImage(TableItem tableItem, int columnIndex, Image image) {
        tableItem.setImage(columnIndex, image);
    }

    protected void setRowDataImage(TableItem tableItem, int columnIndex, Image image) {
        tableItem.setData(ITableCellRenderer.CELL_IMAGE + columnIndex, image);
    }

    protected Image getTrImage(TableItem tableItem, Image img) {
        ImageData ideaData = img.getImageData();
        int whitePixel = ideaData.palette.getPixel(new RGB(255, 255, 255));
        ideaData.transparentPixel = whitePixel;
        Image transparentIdeaImage = new Image(tableItem.getDisplay(), ideaData);
        return transparentIdeaImage;
    }
}
