package net.sf.crepido.util;

import net.sf.crepido.io.TextWriter;
import junit.framework.TestCase;
import net.sf.crepido.base.*;

public class CsvBuilderTest extends TestCase {

    public void testThis() {
        Seq<Seq<?>> seq = Seq.range(1, 4).map(new AbstractUnaryFunction<Integer, Seq<?>>() {

            public Seq<?> apply(final Integer n) {
                return Seq.ofItems(n, "Product " + n, n * 100, "", null);
            }
        });
        seq = seq.prepend(Seq.ofItems("ProductId", "ProductName", "stock", "empties", "nulls"));
        Observable<Seq<?>> obsOfSeq = Observable.of(seq).autoconnect(false);
        CsvBuilder csv = CsvBuilder.SEMICOLON_SEPARATED.ifEmpty("<emtpy>").ifNull("<null>");
        String s = csv.iterate(seq).toStringList().toString();
        assertEquals(s, "[ProductId;ProductName;stock;empties;nulls, 1;Product 1;100;<emtpy>;<null>, 2;Product 2;200;<emtpy>;<null>, 3;Product 3;300;<emtpy>;<null>]");
        Seq.range(1, 5).emitTo(TextWriter.of(System.out).toObserver());
    }
}
