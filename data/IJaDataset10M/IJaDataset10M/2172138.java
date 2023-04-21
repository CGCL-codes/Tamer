package org.yuzz.web;

import static org.yuzz.xml.NodeStatics.a;
import static org.yuzz.xml.NodeStatics.t;
import static org.yuzz.xml.NodeStatics.td;
import static org.yuzz.xml.NodeStatics.th;
import static org.yuzz.xml.NodeStatics.tr;
import java.util.Iterator;
import org.snuvy.DbRow;
import org.snuvy.Schema;
import org.yuzz.functor.Fun.F;
import org.yuzz.xml.Xhtml.Tr;

public class RowToTr extends F<DbRow, Tr> {

    private final Schema _schema;

    public RowToTr(Schema schema) {
        _schema = schema;
    }

    @Override
    public Tr f(DbRow row) {
        Tr tr = tr(a("class", "dbrow"));
        Iterator<String> colnames = _schema.getColumnNames();
        while (colnames.hasNext()) {
            String colname = colnames.next();
            tr.add(td(t(row.toString(colname))));
        }
        return tr;
    }

    public Tr header() {
        Iterator<String> colnames = _schema.getColumnNames();
        Tr tr = tr(a("class", "dbrowheader"));
        while (colnames.hasNext()) {
            String colname = colnames.next();
            tr.add(th(t(colname)));
        }
        return tr;
    }
}
