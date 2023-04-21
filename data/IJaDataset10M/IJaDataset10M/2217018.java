package org.awelements.table;

public interface IColumnDecorator {

    String getStartDecoration(RowRenderContext rowRenderContext, Object cellElement);

    String getEndDecoration(RowRenderContext rowRenderContext, Object cellElement);
}
