package fr.soleil.mambo.components.view.images;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.mambo.tools.Messages;

/**
 * The table model used by PartialImageDataTable, this model lists partially
 * loaded images in a given date range. Its rows are the partially loaded
 * images.
 * 
 * @author CLAISSE
 */
public class PartialImageDataTableModel extends DefaultTableModel {

    private ImageData[] rows;

    private String dateMsg;

    private String dimXMsg;

    private String dimYMsg;

    /**
	 * Returns the image row located at row <code>rowIndex</code>.
	 * 
	 * @param rowIndex
	 *            The specified row
	 * @return The image row located at row <code>rowIndex</code>
	 */
    public ImageData getRow(int rowIndex) {
        return rows[rowIndex];
    }

    public PartialImageDataTableModel(Vector partialImageData) {
        if (partialImageData == null || partialImageData.size() == 0) {
            this.rows = null;
        }
        int size = partialImageData.size();
        this.rows = new ImageData[size];
        int i = 0;
        Enumeration myenum = partialImageData.elements();
        while (myenum.hasMoreElements()) {
            ImageData nextImageData = (ImageData) myenum.nextElement();
            this.rows[i] = nextImageData;
            i++;
        }
        dateMsg = Messages.getMessage("VIEW_IMAGE_DATE");
        dimXMsg = Messages.getMessage("VIEW_IMAGE_DIM_X");
        dimYMsg = Messages.getMessage("VIEW_IMAGE_DIM_Y");
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        if (rows == null) {
            return 0;
        }
        return rows.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ImageData row = this.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        switch(columnIndex) {
            case 0:
                return row.getDate();
            case 1:
                return String.valueOf(row.getDimX());
            case 2:
                return String.valueOf(row.getDimY());
            default:
                return null;
        }
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return dateMsg;
            case 1:
                return dimXMsg;
            case 2:
                return dimYMsg;
            default:
                return null;
        }
    }
}
