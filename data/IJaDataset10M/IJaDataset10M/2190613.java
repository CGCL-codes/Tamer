package org.jowidgets.tools.model.table;

import java.util.ArrayList;
import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;

public class DefaultTableColumnModel implements IDefaultTableColumnModel {

    private final IDefaultTableColumnModel model;

    public DefaultTableColumnModel() {
        this(0);
    }

    public DefaultTableColumnModel(final int columnCount) {
        this.model = Toolkit.getModelFactoryProvider().getTableModelFactory().columnModel(columnCount);
    }

    @Override
    public final int getColumnCount() {
        return model.getColumnCount();
    }

    @Override
    public final ITableColumnModelObservable getTableColumnModelObservable() {
        return model.getTableColumnModelObservable();
    }

    @Override
    public final void addColumnModelListener(final ITableColumnModelListener listener) {
        model.addColumnModelListener(listener);
    }

    @Override
    public final void removeColumnModelListener(final ITableColumnModelListener listener) {
        model.removeColumnModelListener(listener);
    }

    @Override
    public final IDefaultTableColumn getColumn(final int columnIndex) {
        return model.getColumn(columnIndex);
    }

    @Override
    public final ArrayList<IDefaultTableColumn> getColumns() {
        return model.getColumns();
    }

    @Override
    public final IDefaultTableColumn addColumn() {
        return model.addColumn();
    }

    @Override
    public final IDefaultTableColumn addColumn(final int columnIndex) {
        return model.addColumn(columnIndex);
    }

    @Override
    public final void addColumn(final IDefaultTableColumn column) {
        model.addColumn(column);
    }

    @Override
    public final void addColumn(final int columnIndex, final IDefaultTableColumn column) {
        model.addColumn(columnIndex, column);
    }

    @Override
    public final IDefaultTableColumn addColumn(final IDefaultTableColumnBuilder columnBuilder) {
        return model.addColumn(columnBuilder);
    }

    @Override
    public final IDefaultTableColumn addColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
        return model.addColumn(columnIndex, columnBuilder);
    }

    @Override
    public final IDefaultTableColumn addColumn(final String text) {
        return model.addColumn(text);
    }

    @Override
    public final IDefaultTableColumn addColumn(final String text, final String toolTipText) {
        return model.addColumn(text, toolTipText);
    }

    @Override
    public final void removeColumn(final int columnIndex) {
        model.removeColumn(columnIndex);
    }

    @Override
    public final void removeColumns(final int fromColumnIndex, final int toColumnIndex) {
        model.removeColumns(fromColumnIndex, toColumnIndex);
    }

    @Override
    public final void removeColumns(final int... columns) {
        model.removeColumns(columns);
    }

    @Override
    public final void removeAllColumns() {
        model.removeAllColumns();
    }

    @Override
    public final void modifyModelStart() {
        model.modifyModelStart();
    }

    @Override
    public final void modifyModelEnd() {
        model.modifyModelEnd();
    }

    @Override
    public void setFireEvents(final boolean fireEvents) {
        model.setFireEvents(fireEvents);
    }

    @Override
    public final void setColumn(final int columnIndex, final IDefaultTableColumn column) {
        model.setColumn(columnIndex, column);
    }

    @Override
    public final IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
        return model.setColumn(columnIndex, columnBuilder);
    }

    @Override
    public final void setColumnText(final int columnIndex, final String text) {
        model.setColumnText(columnIndex, text);
    }

    @Override
    public final void setColumnToolTipText(final int columnIndex, final String tooltipText) {
        model.setColumnToolTipText(columnIndex, tooltipText);
    }

    @Override
    public final void setColumnIcon(final int columnIndex, final IImageConstant icon) {
        model.setColumnIcon(columnIndex, icon);
    }

    @Override
    public final void setColumnAlignment(final int columnIndex, final AlignmentHorizontal alignment) {
        model.setColumnAlignment(columnIndex, alignment);
    }
}
