package de.kumpe.hadooptimizer.examples.benchmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.kumpe.hadooptimizer.EaOptimizerConfiguration;
import de.kumpe.hadooptimizer.EvaluationResult;
import de.kumpe.hadooptimizer.Evaluator;
import de.kumpe.hadooptimizer.Recombiner;
import de.kumpe.hadooptimizer.examples.OptimizerExample;
import de.kumpe.hadooptimizer.impl.IdentityMutator;

/**
 * A benchmark example which sleeps a specified number of nanoseconds in the
 * evaluator.
 * 
 * @author <a href="http://kumpe.de/christian/java">Christian Kumpe</a>
 */
public class IncrementingOffspring extends OptimizerExample<double[]> {

    private static final Log log = LogFactory.getLog(IncrementingOffspring.class);

    private static final class IncrementingRecombiner implements Recombiner<double[]> {

        private static final long serialVersionUID = 1L;

        private long cycle;

        @Override
        public Collection<double[]> recombine(final Collection<EvaluationResult<double[]>> parents) {
            cycle++;
            final double[] individual = parents.iterator().next().getIndividual();
            final long offspring = cycle;
            final Collection<double[]> children = new ArrayList<double[]>();
            for (int i = 0; i < offspring; i++) {
                children.add(individual);
            }
            return children;
        }
    }

    private static final class SleepingEvaluator implements Evaluator<double[]> {

        private static final long serialVersionUID = 1L;

        @Override
        public double evaluate(final double[] individual) {
            final long nanosToSleep = (long) individual[0];
            if (0 == nanosToSleep) {
                return 0;
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("Sleeping %,dns...", nanosToSleep));
            }
            final long start = System.nanoTime();
            try {
                Thread.sleep(nanosToSleep / 1000000, (int) (nanosToSleep % 1000000));
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
            final long sleptNanos = System.nanoTime() - start;
            if (log.isDebugEnabled()) {
                log.debug(String.format("Slept %,dns.", sleptNanos));
            }
            return Math.abs(sleptNanos - nanosToSleep);
        }
    }

    @Override
    protected EaOptimizerConfiguration<double[]> createEaOptimizerConfiguration() {
        final EaOptimizerConfiguration<double[]> conf = new EaOptimizerConfiguration<double[]>();
        @SuppressWarnings("unchecked") final List<String> remainingArgs = commandLine.getArgList();
        final int size = Integer.parseInt(remainingArgs.get(1));
        final double[] startIndividual = new double[size];
        startIndividual[0] = Double.parseDouble(remainingArgs.get(0));
        initConfiguration(conf, startIndividual);
        conf.setRecombiner(new IncrementingRecombiner());
        conf.setMutator(new IdentityMutator<double[]>());
        conf.setEvaluator(new SleepingEvaluator());
        return conf;
    }

    @Override
    protected String getVersionInfo() {
        return "$Id: IncrementingOffspring.java 3891 2011-04-20 15:16:25Z baumbart $";
    }
}
