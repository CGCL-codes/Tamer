package com.wijqgrid.model;

import java.io.Serializable;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *	
 * @param <B> The bean.
 */
public interface IColumn<B extends Serializable> extends Serializable {

    /**
	 * Supported alignments.
	 * 
	 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
	 *
	 */
    public static enum Alignment {

        RIGHT, CENTER, LEFT
    }

    /**
	 * @return The width of the column (in pixels)
	 */
    public int getWidth();

    /**
	 * @return whether the column is sortable or not.
	 */
    public boolean isSortable();

    /**
	 * @return the alignment
	 */
    public Alignment getAlignment();

    /**
	 * @param alignment the alignment to set
	 */
    public abstract void setAlignment(Alignment alignment);

    /**
	 * @return the initialSort
	 */
    public abstract boolean isInitialSort();

    /**
	 * @param initialSort the initialSort to set
	 */
    public abstract void setInitialSort(boolean initialSort);

    /**
	 * @return returns the model used to print the title.
	 */
    public IModel<String> getTitleModel();

    /**
	 * Returns the contents of a cell.
	 * 
	 * @param row
	 * @param column
	 * @param rowModel
	 * @return
	 */
    public String renderCell(int row, int column, IModel<B> rowModel);

    /**
	 * @return the resizable
	 */
    public boolean isResizable();

    /**
	 * @param resizable the resizable to set
	 */
    public void setResizable(boolean resizable);

    /**
	 * 
	 * @return The path used as sort property.
	 */
    public String getSortPath();

    /**
	 *  The paths used to identify the column on a JavaBean.
	 *  
	 * @return
	 */
    public String getPropertyPath();

    /**
	 * 
	 * @param gridModel
	 */
    public void setGridModel(GridModel<B> gridModel);
}
