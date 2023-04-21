package com.wijqgrid.component.event;

import java.io.Serializable;
import com.wijqgrid.component.Grid;
import com.wijqgrid.component.IGridAware;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class AbstractAjaxGridAwareEvent<B extends Serializable> extends AbstractAjaxEvent<B> implements IGridAware<B> {

    private static final long serialVersionUID = 1L;

    private Grid<B> grid;

    /**
	 * @param event
	 */
    public AbstractAjaxGridAwareEvent(GridEvent event) {
        super(event);
    }

    public Grid<B> getGrid() {
        return grid;
    }

    public void setGrid(Grid<B> grid) {
        this.grid = grid;
    }
}
