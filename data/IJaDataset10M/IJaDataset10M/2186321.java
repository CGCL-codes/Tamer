package astcentric.structure.bl;

import static astcentric.structure.bl.DummyDefiningSource.DUMMY_DEFINING_SOURCE;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

public class PatternBasedFunctionTest extends TestCase {

    private static final class MockCalculationContext implements CalculationContext {

        public void afterCalculation(Calculator calculator, Data calculationResult) {
        }

        public void beforeCalculation(Calculator calculator, List<Function> arguments) {
        }
    }

    public void test() {
        TypeVariable a = new TypeVariable();
        TypeVariable b = new TypeVariable();
        DataTypeDeclaration pair = new DataTypeDeclaration("Pair", DUMMY_DEFINING_SOURCE, a, b);
        ConstructorDeclaration pairConstructorDeclaration = pair.addConstructorDeclaration("Pair", DUMMY_DEFINING_SOURCE, a, b);
        TypeVariable a2 = new TypeVariable();
        TypeVariable b2 = new TypeVariable();
        DataTypeSignature s = new DataTypeSignature(pair, a2, b2);
        PatternBasedFunction first = new PatternBasedFunction(new FunctionDeclaration("first", new FunctionSignature(a2, s), DUMMY_DEFINING_SOURCE), DUMMY_DEFINING_SOURCE);
        DataPlaceholder x = new DataPlaceholder(new DummyDefiningSource());
        Constructor cons1 = new Constructor(pairConstructorDeclaration, x, new DataPlaceholder(new DummyDefiningSource()));
        first.addPattern(x.getResolver(), Arrays.<PatternArgument>asList(cons1));
        IntegerData two = new IntegerData(2);
        Data exampleData = new Data(pairConstructorDeclaration, two, new DoubleData(Math.PI));
        first.seal();
        Data result = first.calculate(new MockCalculationContext(), Arrays.<Function>asList(exampleData));
        assertEquals(two, result);
    }
}
