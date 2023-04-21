package sketch.generator.experiment;

import java.util.Collection;
import sketch.generator.Generator;

public abstract class ValueGroupGenerator<T> extends Generator<ValueGroup<T>> {

    protected SingleValue<T> createSingleValue(T t) {
        return new SingleValue<T>(t);
    }

    protected MultiValue<T> createMultiValue(T... ts) {
        return new MultiValue<T>(ts);
    }

    protected MultiValue<T> createMultiValue(Collection<T> ts) {
        return new MultiValue<T>(ts);
    }

    protected EmptyValue<T> createEmptyValue() {
        return new EmptyValue<T>();
    }
}
