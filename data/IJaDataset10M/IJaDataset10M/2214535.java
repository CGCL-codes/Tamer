package br.usp.iterador.util;

import br.usp.iterador.internal.logic.ExecutionIterable;

/**
 * Extracts a value from any field except dimension fields.
 *
 * @author Guilherme Silveira
 */
public class AdvancedValueExtractor implements ValueExtractor {

    private final String name;

    public AdvancedValueExtractor(String name) {
        this.name = name;
    }

    public double extractValue(ExecutionIterable iteration) {
        return iteration.getRealValue(name);
    }
}
