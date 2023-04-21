package com.gr.staffpm.widget.jqgrid.component.event;

import java.io.Serializable;
import com.gr.staffpm.widget.jqgrid.component.Grid;
import com.gr.staffpm.widget.jqgrid.component.IGridAware;

/**
 * @author Graham Rhodes
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

    @Override
    public Grid<B> getGrid() {
        return grid;
    }

    @Override
    public void setGrid(Grid<B> grid) {
        this.grid = grid;
    }
}
