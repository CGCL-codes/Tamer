package de.kumpe.hadooptimizer.hadoop;

import java.util.List;
import org.apache.hadoop.io.DoubleWritable;

final class IndividualReader<I> extends FileReader<I> {

    private final List<I> individuals;

    IndividualReader(final List<I> individuals) {
        this.individuals = individuals;
    }

    @Override
    protected void readIndividual(final DoubleWritable key, final I individual) {
        individuals.add(individual);
    }
}
