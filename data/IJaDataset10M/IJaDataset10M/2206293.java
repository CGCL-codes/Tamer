package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IProjection;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IFormatter;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;

public abstract class DataTableColumn<RowClass> extends TableHeader {

    protected IProjection<RowClass> projection;

    protected IFormatter formatter;

    public DataTableColumn(String header, String width, IProjection<RowClass> projection, IFormatter formatter) {
        super(header, width);
        this.projection = projection;
        this.formatter = formatter;
    }

    public boolean isSortable() {
        return projection != null ? projection.isSortable() : false;
    }

    public IProjection<RowClass> getProjection() {
        return projection;
    }

    public IFormatter getFormatter() {
        return formatter;
    }

    public abstract boolean isWidget();
}
