package net.sf.derquinsej;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementations for number generators.
 * All provided implementations are thread-safe.
 * 
 * @author Andres Rodriguez
 */
public class NumberGenerators {

    private NumberGenerators() {
    }

    /**
	 * Creates a new integer generator.
	 * @param initialValue The first value the generator will supply.
	 * @return The created generator.
	 */
    public static NumberGenerator<Integer> newIntGenerator(final int initialValue) {
        return new NumberGenerator<Integer>() {

            private final AtomicInteger value = new AtomicInteger(initialValue);

            public Integer get() {
                return value.getAndIncrement();
            }
        };
    }

    /**
	 * Creates a new integer generator with an initial value of zero.
	 * @return The created generator.
	 */
    public static NumberGenerator<Integer> newIntGenerator() {
        return newIntGenerator(0);
    }

    /**
	 * Creates a new long integer generator.
	 * @param initialValue The first value the generator will supply.
	 * @return The created generator.
	 */
    public static NumberGenerator<Long> newLongGenerator(final long initialValue) {
        return new NumberGenerator<Long>() {

            private final AtomicLong value = new AtomicLong(initialValue);

            public Long get() {
                return value.getAndIncrement();
            }
        };
    }

    /**
	 * Creates a new long integer generator with an initial value of zero.
	 * @return The created generator.
	 */
    public static NumberGenerator<Long> newLongGenerator() {
        return newLongGenerator(0L);
    }
}
