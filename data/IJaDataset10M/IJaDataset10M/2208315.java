package net.sourceforge.metrics.calculators;

import net.sourceforge.metrics.core.Constants;
import net.sourceforge.metrics.core.Metric;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Counts the number of methods in a class. Distinguishes between statics and instance methods, sets NUM_METHODS and NUM_STAT_METHODS
 * 
 * @author Frank Sauer
 */
public class NumberOfMethods extends Calculator implements Constants {

    /**
	 * Constructor for NumberOfMethods.
	 */
    public NumberOfMethods() {
        super(NUM_METHODS);
    }

    /**
	 * @see net.sourceforge.metrics.calculators.Calculator#calculate(net.sourceforge.metrics.core.sources.AbstractMetricSource)
	 */
    @Override
    public void calculate(AbstractMetricSource source) throws InvalidSourceException {
        if (source.getLevel() != TYPE) {
            throw new InvalidSourceException("NumberOfMethods is only applicable to types");
        }
        try {
            IMethod[] methods = ((IType) source.getJavaElement()).getMethods();
            int stats = 0;
            int inst = 0;
            for (IMethod method2 : methods) {
                if ((method2.getFlags() & Flags.AccStatic) != 0) {
                    stats++;
                } else {
                    inst++;
                }
            }
            source.setValue(new Metric(NUM_METHODS, inst));
            source.setValue(new Metric(NUM_STAT_METHODS, stats));
        } catch (JavaModelException e) {
            source.setValue(new Metric(NUM_METHODS, 0));
            source.setValue(new Metric(NUM_STAT_METHODS, 0));
        }
    }
}
