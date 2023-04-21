package org.cubicunit.internal.ie;

import java.util.List;
import java.util.Vector;
import org.cubicunit.Table;
import org.cubicunit.TableRow;
import com.tapsterrock.jiffie.IHTMLDOMNode;
import com.tapsterrock.jiffie.IHTMLDocument2;
import com.tapsterrock.jiffie.IHTMLTable;

public class IeTable extends IeElementContainer implements Table {

    private IHTMLTable table;

    private final IHTMLDocument2 document;

    public IeTable(IHTMLTable table, IHTMLDocument2 document) {
        super(table, document);
        this.table = table;
        this.document = document;
    }

    public List<? extends TableRow> getTableRows() {
        List<TableRow> rows = new Vector<TableRow>();
        IeEngine engine = new IeEngine(table);
        for (IHTMLDOMNode node : engine.getDispatches("//tr")) {
            rows.add((TableRow) IeElementFactory.createElement(node, document));
        }
        return rows;
    }
}
