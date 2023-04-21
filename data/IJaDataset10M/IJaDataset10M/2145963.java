package com.ideo.sweetdevria.test.provider.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ideo.sweetdevria.taglib.grid.common.model.GridCellModel;
import com.ideo.sweetdevria.taglib.grid.common.model.GridColumnModel;
import com.ideo.sweetdevria.taglib.grid.common.model.GridRowModel;
import com.ideo.sweetdevria.taglib.grid.data.IGridDataProvider;
import com.ideo.sweetdevria.test.bean.Book;
import com.ideo.sweetdevria.test.data.DataFunction;
import com.ideo.sweetdevria.test.hibernate.HibernateSessionRequestFilter;

public class BookDatabaseProvider implements IGridDataProvider {

    private static final long serialVersionUID = -5046783981621738052L;

    private static Log log = LogFactory.getLog(HibernateSessionRequestFilter.class);

    public void addRow(GridRowModel model) {
    }

    public void defaultSortColumns(List columnsModel) {
        List colToSort = new ArrayList();
        for (Iterator it = columnsModel.iterator(); it.hasNext(); ) {
            GridColumnModel columnModel = (GridColumnModel) it.next();
            if (columnModel.getSortAscendant() != null) colToSort.add(columnModel);
        }
        sortColumns(colToSort);
    }

    public List getAllRowModels() {
        List books = null;
        try {
            books = DataFunction.getInstance().getBooks();
        } catch (Exception e) {
            log.error("getAllRowModels ()", e);
        }
        List rowsModel = new ArrayList(books.size());
        for (Iterator it = books.iterator(); it.hasNext(); ) {
            rowsModel.add(convertBookToRow((Book) it.next()));
        }
        return rowsModel;
    }

    public GridRowModel getRowForId(String id) {
        try {
            return convertBookToRow(DataFunction.getInstance().getBook(Integer.parseInt(id)));
        } catch (NumberFormatException e) {
            log.error("getRowForId (" + id + ")", e);
        } catch (Exception e) {
            log.error("getRowForId (" + id + ")", e);
        }
        return null;
    }

    public List getRowModels(int firstIndex, int number) {
        List products = null;
        try {
            products = DataFunction.getInstance().getBooks(firstIndex, firstIndex + number);
        } catch (Exception e) {
            log.error("getRowModels (" + firstIndex + ", " + number + ")", e);
        }
        List rowsModel = new ArrayList(products.size());
        for (Iterator it = products.iterator(); it.hasNext(); ) {
            rowsModel.add(convertBookToRow((Book) it.next()));
        }
        return rowsModel;
    }

    protected GridRowModel convertBookToRow(Book b) {
        List cellContent = new ArrayList(5);
        cellContent.add(new GridCellModel(b.getTitre(), null));
        cellContent.add(new GridCellModel(b.getAuteur(), null));
        cellContent.add(new GridCellModel(b.getIsbn(), null));
        cellContent.add(new GridCellModel(b.getCycle(), null));
        cellContent.add(new GridCellModel(b.getDate(), null));
        cellContent.add(new GridCellModel(b.getEdition(), null));
        cellContent.add(new GridCellModel(null, b.getPrix()));
        cellContent.add(new GridCellModel(b.getResume(), null));
        cellContent.add(new GridCellModel(b.getImage(), null));
        return new GridRowModel(cellContent, b.getResume(), String.valueOf(b.getId()), (int) b.getId());
    }

    public int getSize() {
        return DataFunction.getInstance().getSize();
    }

    public boolean requireIteratorEvaluation() {
        return false;
    }

    public void sortColumns(List columnsModel) {
        DataFunction.getInstance().clearOrders();
        for (int i = 0; i < columnsModel.size(); ++i) {
            GridColumnModel col = (GridColumnModel) columnsModel.get(i);
            DataFunction.getInstance().addOrders(getPropertyForColumn(col), col.getSortAscendant().booleanValue());
        }
    }

    /**
	 * 
	 * @param columnModel
	 * @return the hibernate property corresponding to a column. Required for sort. Check your .hbm.xml to have these properties
	 */
    public String getPropertyForColumn(GridColumnModel columnModel) {
        if (columnModel.getId().equals("TITRE")) return "titre";
        if (columnModel.getId().equals("AUTEUR")) return "auteur";
        if (columnModel.getId().equals("ISBN")) return "isbn";
        if (columnModel.getId().equals("CYCLE")) return "cycle";
        if (columnModel.getId().equals("DATE")) return "date";
        if (columnModel.getId().equals("EDITION")) return "edition";
        if (columnModel.getId().equals("PRIX")) return "prix";
        if (columnModel.getId().equals("RESUME")) return "resume";
        throw new IllegalArgumentException("This id not exists");
    }

    public void addRow(GridRowModel model, int position) {
    }

    public void deleteRow(String rowId) {
    }

    public void updateRow(GridRowModel model) {
    }
}
