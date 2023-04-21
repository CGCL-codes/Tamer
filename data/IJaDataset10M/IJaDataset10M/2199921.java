package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SymbolDumperTest {

    @Test
    public void testSymbolDumper() {
        NonTerminal A = new NonTerminal("A").addChildren(new Terminal("B", "b")).addChildren(new NonTerminal("C").addChildren(new Terminal("D", "d")));
        SymbolDumper dumper = new SymbolDumper();
        A.accept(dumper);
        assertEquals("A(B<b>, C(D<d>))", dumper.toString());
    }
}
