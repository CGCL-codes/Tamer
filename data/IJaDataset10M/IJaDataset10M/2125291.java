package com.tonbeller.jpivot.table;

import org.olap4j.Cell;
import orcajo.azada.commoms.table.SpanModel;

/**
 * Created on 18.10.2002
 * 
 * @author av
 * @version fsaz
 */
public class CellBuilderDecorator extends PartBuilderDecorator implements CellBuilder {

    public CellBuilderDecorator(CellBuilder delegate) {
        super(delegate);
    }

    public void build(SpanModel sm, int rowIndex, int colIndex, Cell cell, boolean even) {
        ((CellBuilder) delegate).build(sm, rowIndex, colIndex, cell, even);
    }
}
