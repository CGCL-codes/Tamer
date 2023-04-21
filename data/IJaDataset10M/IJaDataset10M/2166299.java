package com.wijqgrid.component;

import java.io.Serializable;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IGridAware<B extends Serializable> {

    void setGrid(Grid<B> grid);

    Grid<B> getGrid();
}
