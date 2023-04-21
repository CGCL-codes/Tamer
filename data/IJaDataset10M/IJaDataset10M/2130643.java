package com.jedox.etl.core.transform;

import com.jedox.etl.core.node.ColumnManager;
import com.jedox.etl.core.node.CoordinateNode;
import com.jedox.etl.core.node.IColumn;
import com.jedox.etl.core.node.AnnexNode;
import com.jedox.etl.core.node.Row;
import com.jedox.etl.core.source.processor.IProcessor;
import com.jedox.etl.core.source.processor.Processor;

public abstract class CoordinateProcessor extends Processor {

    private IProcessor input;

    private Row coordinates;

    private Row infos;

    private Row row = new Row();

    public CoordinateProcessor(IProcessor input, ColumnManager columns, boolean includeInfos) {
        this.input = input;
        coordinates = columns.getColumnsOfType(IColumn.ColumnTypes.coordinate);
        infos = new Row();
        for (IColumn source : coordinates.getColumns()) {
            CoordinateNode column = new CoordinateNode(source.getName());
            column.setInput(source);
            row.addColumn(column);
        }
        if (includeInfos) {
            infos = columns.getColumnsOfType(IColumn.ColumnTypes.annex);
            for (IColumn source : infos.getColumns()) {
                CoordinateNode column = new AnnexNode(source.getName());
                column.setInput(source);
                row.addColumn(column);
            }
        }
        setName(input.getName());
    }

    protected IProcessor getInputProcessor() {
        return input;
    }

    protected void removeInfo(String name) {
        infos.removeColumn(name);
        getRow().removeColumn(name);
    }

    protected boolean fillCoordinates(Row inputRow) {
        if (inputRow != null) {
            for (int i = 0; i < coordinates.size(); i++) {
                CoordinateNode c = (CoordinateNode) getRow().getColumn(i);
                c.setInput(inputRow.getColumn(i));
            }
            for (int i = 0; i < infos.size(); i++) {
                CoordinateNode c = (CoordinateNode) getRow().getColumn(coordinates.size() + i);
                c.setInput(inputRow.getColumn(coordinates.size() + i));
            }
        }
        return inputRow != null;
    }

    protected boolean fillRow(Row row) throws Exception {
        return fillCoordinates(getInputProcessor().next());
    }

    protected int getLength() {
        return coordinates.size() + infos.size();
    }

    protected Row getRow() {
        return row;
    }
}
