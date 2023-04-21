package evolve.genes;

import evolve.BuiltinRegistrar;
import evolve.Context;
import evolve.Gene;
import evolve.ObjectRegistry;
import functional.Obj;
import functional.type.ConsType;
import functional.type.FunctionType;
import functional.type.Parameter;
import functional.type.Type;
import junit.framework.TestCase;

public class LookupGeneTest extends TestCase {

    private Gene buildLookupGene() {
        Parameter p = new Parameter();
        return new LookupGene(new FunctionType(new ConsType(p, p), new Type[] { p, p }), "cons", 0);
    }

    public void testType() {
        Gene gene = buildLookupGene();
        Parameter q = new Parameter();
        assertTrue(gene.type().match(new FunctionType(new ConsType(q, q), new Type[] { q, q })).matches());
    }

    public void testExpress() {
        Gene gene = buildLookupGene();
        Context c = buildContext();
        Obj result = gene.express(c);
        assertNotNull(result);
        assertEquals(result.toString(), "cons");
    }

    private Context buildContext() {
        ObjectRegistry reg = new ObjectRegistry();
        BuiltinRegistrar.registerBuiltins(reg);
        Context c = new Context(reg);
        return c;
    }
}
