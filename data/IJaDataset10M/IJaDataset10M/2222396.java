package net.sourceforge.nattable.dataset.pricing.valuegenerator;

import net.sourceforge.nattable.dataset.valuegenerator.AbstractListValueGenerator;

public class ErrorSeverityValueGenerator extends AbstractListValueGenerator<Integer> {

    public ErrorSeverityValueGenerator() {
        super(new Integer[] { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2) });
    }
}
