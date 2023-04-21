package net.dataforte.canyon.spi.echo.model;

import java.lang.reflect.Method;
import java.text.Format;
import java.text.MessageFormat;
import nextapp.echo.app.table.TableColumn;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DynaGridColumn implements Comparable<DynaGridColumn> {

    int order;

    String p;

    Method m;

    TableColumn tc;

    Object identifier;

    Format f;

    public DynaGridColumn(int order, String p, TableColumn tc) {
        this.order = order;
        this.m = null;
        this.p = p;
        this.tc = tc;
        this.f = null;
    }

    public DynaGridColumn(int order, String p, TableColumn tc, String f) {
        this.order = order;
        this.m = null;
        this.p = p;
        this.tc = tc;
        this.f = (f != null && f.length() > 0) ? new MessageFormat(f) : null;
    }

    public DynaGridColumn(int order, Method m, TableColumn tc) {
        this.order = order;
        this.m = m;
        this.p = null;
        this.tc = tc;
        this.f = null;
    }

    public DynaGridColumn(int order, Method m, TableColumn tc, String f) {
        this.order = order;
        this.m = m;
        this.p = null;
        this.tc = tc;
        this.f = (f != null && f.length() > 0) ? new MessageFormat(f) : null;
    }

    public int compareTo(DynaGridColumn o) {
        if (order == o.order) {
            return 0;
        } else if (order < o.order) {
            return -1;
        } else {
            return 1;
        }
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this).append("order", this.order).toString();
    }
}
