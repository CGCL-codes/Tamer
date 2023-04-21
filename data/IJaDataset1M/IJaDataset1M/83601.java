package gnu.testlet.runner.compare;

public class EvolutionTypeVisitor implements ComparisonVisitor {

    private final int[] counters = new int[EvolutionType.values().length];

    public int getCounter(EvolutionType type) {
        return counters[type.ordinal()];
    }

    public void visit(RunComparison run) {
    }

    public void visit(PackageComparison pkg) {
    }

    public void visit(ClassComparison cls) {
    }

    public void visit(TestComparison test) {
        counters[test.getEvolutionType().ordinal()]++;
    }
}
