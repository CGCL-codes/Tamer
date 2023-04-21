package org.norecess.nolatte.types;

import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.ast.support.DatumFactory;
import org.norecess.nolatte.ast.visitors.DatumVisitor;
import org.norecess.nolatte.datumprocessors.ToText;
import org.norecess.nolatte.primitives.group.IsGroupVisitor;

public class DataTypeFilterFactory {

    public DataTypeFilter create() {
        return new DataTypeFilter(new DatumFactory(), new IndexedDatumFilter(), createITextFilter(), createWhitespaceAndTextFilter(), new ToText(new DatumFactory()), new TextAppenderFactory(), new IsGroupVisitor(), new GroupOfDataFilter("not a group"), new Groupizer(), new WhitespaceDatumStripper());
    }

    private DatumVisitor<IText> createWhitespaceAndTextFilter() {
        return new WhitespaceAndTextFilter("not proper text");
    }

    public DatumVisitor<IText> createITextFilter() {
        return new TextFilter("not text");
    }
}
