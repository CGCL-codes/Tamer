package com.extjs.serverside.table;

import java.io.IOException;
import com.extjs.serverside.form.RenderableContainer;

public abstract class Table implements RenderableContainer {

    private String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public abstract TableColumn[] getColumns();

    public abstract void renderData(TableDataRequest request) throws IOException;

    public ColumnRenderer[] getRenderers() {
        return new ColumnRenderer[0];
    }
}
