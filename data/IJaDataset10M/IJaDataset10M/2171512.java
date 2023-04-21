package org.bigk.invoices.utils;

import org.displaytag.decorator.TableDecorator;

public class DTagTableDecorator extends TableDecorator {

    public DTagTableDecorator() {
        super();
    }

    @Override
    public String addRowId() {
        return this.tableModel.getId() + "tableRow" + getViewIndex();
    }
}
