package sketch.specs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import sketch.generator.ExhaustiveValueGenerator;
import sketch.generator.ValueGenerator;
import sketch.util.Checker;

public class StepwiseGeneratorGroup<T> {

    public final Map<SimpleName, ValueGenerator<T>> nameGenerators;

    public final Map<SimpleName, Type> typeMaps;

    public final Collection<ValueGenerator<T>> generators;

    public StepwiseGeneratorGroup(Map<SimpleName, ValueGenerator<T>> nameGeneratorMap, Map<SimpleName, Type> typeMaps) {
        Checker.checkTrue(nameGeneratorMap.keySet().equals(typeMaps.keySet()), "The key set should be equal");
        this.nameGenerators = nameGeneratorMap;
        this.typeMaps = typeMaps;
        this.generators = nameGeneratorMap.values();
    }

    public boolean hasNext() {
        for (ValueGenerator<T> generator : generators) {
            if (generator instanceof ExhaustiveValueGenerator) {
                ExhaustiveValueGenerator exhaustGen = (ExhaustiveValueGenerator) generator;
                if (!exhaustGen.hasNextValues()) {
                    return false;
                }
            } else if (!generator.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public List next() {
        if (!hasNext()) {
            throw new RuntimeException("No more element");
        }
        List retList = new LinkedList();
        for (ValueGenerator<T> generator : generators) {
            if (generator instanceof ExhaustiveValueGenerator) {
                ExhaustiveValueGenerator exhaustiveGen = (ExhaustiveValueGenerator) generator;
                retList.add(exhaustiveGen.nextValue());
            } else {
                retList.add(generator.next());
            }
        }
        return retList;
    }

    public Object lookupCurrentValue(SimpleName name) {
        if (this.nameGenerators.get(name) instanceof ExhaustiveValueGenerator) {
            ExhaustiveValueGenerator exhGen = (ExhaustiveValueGenerator) this.nameGenerators.get(name);
            return exhGen.currentValue();
        }
        return this.nameGenerators.get(name).current();
    }
}
