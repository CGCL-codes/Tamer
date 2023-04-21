package org.jquantlib.methods.finitedifferences;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * General class for one dimensional PDE's
 *
 * @author Srinivas Hasti
 *
 * @param <T>
 */
public class PdeConstantCoeff<T extends Pde> extends PdeSecondOrderParabolic {

    private final double diffusion;

    private final double drift;

    private final double discount;

    private final Class<? extends Pde> classT;

    public PdeConstantCoeff(final Class<? extends Pde> classT, final GeneralizedBlackScholesProcess process, final double t, final double x) {
        this.classT = classT;
        final T pde = getInstance(classT, process);
        diffusion = pde.diffusion(t, x);
        drift = pde.drift(t, x);
        discount = pde.discount(t, x);
    }

    @Override
    public double diffusion(final double t, final double x) {
        return diffusion;
    }

    @Override
    public double discount(final double t, final double x) {
        return discount;
    }

    @Override
    public double drift(final double t, final double x) {
        return drift;
    }

    protected T getInstance(final Class<? extends Pde> classT, final GeneralizedBlackScholesProcess process) {
        try {
            return (T) classT.getConstructor(GeneralizedBlackScholesProcess.class).newInstance(process);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }
}
