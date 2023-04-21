package com.cleangwt.client.ext.table.cell;

import java.util.List;
import com.cleangwt.client.ext.CheckField;
import com.cleangwt.client.model.Option;

public class CheckCell extends ValuedFieldRenderer<CheckField> {

    private List<Option> options;

    public CheckCell(List<Option> options) {
        this.options = options;
    }

    @Override
    protected CheckField createCell() {
        return new CheckField(options);
    }
}
