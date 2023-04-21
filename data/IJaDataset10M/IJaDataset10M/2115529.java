package evolve.genes;

import evolve.Context;
import evolve.ObjectRegistry;
import functional.Symbol;
import functional.Obj;
import functional.type.BaseType;
import junit.framework.TestCase;

public class SymbolGeneratorTest extends TestCase {

    public void testType() {
        SymbolGenerator gene = new SymbolGenerator(1L, 5);
        assertEquals(gene.type(), BaseType.SYMBOL);
    }

    public void testExpress() {
        SymbolGenerator gene = new SymbolGenerator(1L, 5);
        Obj phene = gene.express(new Context(new ObjectRegistry()));
        assertNotNull(phene);
        assertTrue(phene instanceof Symbol);
        Symbol symbol = (Symbol) phene;
        assertNotNull(symbol);
        assertEquals(5, symbol.name().length());
    }
}
