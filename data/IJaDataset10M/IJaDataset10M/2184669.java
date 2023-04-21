package com.phasotron.ectable.view;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.View;
import org.extremecomponents.table.view.html.CalcBuilder;
import org.extremecomponents.table.view.html.RowBuilder;
import org.extremecomponents.util.HtmlBuilder;
import com.phasotron.ectable.view.html.FormBuilder;
import com.phasotron.ectable.view.html.TableBuilder;

public abstract class AbstractHtmlView implements View {

    private HtmlBuilder html;

    private TableModel model;

    private FormBuilder formBuilder;

    private boolean bufferView;

    private TableBuilder tableBuilder;

    private RowBuilder rowBuilder;

    private CalcBuilder calcBuilder;

    protected HtmlBuilder getHtmlBuilder() {
        return html;
    }

    protected TableModel getTableModel() {
        return model;
    }

    protected TableBuilder getTableBuilder() {
        return tableBuilder;
    }

    protected void setTableBuilder(TableBuilder tableBuilder) {
        this.tableBuilder = tableBuilder;
    }

    public RowBuilder getRowBuilder() {
        return rowBuilder;
    }

    protected void setRowBuilder(RowBuilder rowBuilder) {
        this.rowBuilder = rowBuilder;
    }

    public CalcBuilder getCalcBuilder() {
        return calcBuilder;
    }

    protected void setCalcBuilder(CalcBuilder calcBuilder) {
        this.calcBuilder = calcBuilder;
    }

    public final void beforeBody(TableModel model) {
        this.model = model;
        bufferView = model.getTableHandler().getTable().isBufferView();
        if (bufferView) {
            html = new HtmlBuilder();
        } else {
            html = new HtmlBuilder(model.getContext().getWriter());
        }
        formBuilder = new FormBuilder(html, model);
        init(html, model);
        formBuilder.formStart();
        tableBuilder.themeStart();
        beforeBodyInternal(model);
    }

    public void body(TableModel model, Column column) {
        if (column.isFirstColumn()) {
            rowBuilder.rowStart();
        }
        html.append(column.getCellDisplay());
        if (column.isLastColumn()) {
            rowBuilder.rowEnd();
        }
    }

    public final Object afterBody(TableModel model) {
        afterBodyInternal(model);
        tableBuilder.themeEnd();
        formBuilder.formEnd();
        if (bufferView) {
            return html.toString();
        }
        return "";
    }

    protected void init(HtmlBuilder html, TableModel model) {
        setTableBuilder(new TableBuilder(html, model));
        setRowBuilder(new RowBuilder(html, model));
        setCalcBuilder(new CalcBuilder(html, model));
    }

    protected abstract void beforeBodyInternal(TableModel model);

    protected abstract void afterBodyInternal(TableModel model);
}
