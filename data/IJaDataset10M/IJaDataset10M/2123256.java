package org.jquantlib.processes;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.LocalVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.volatilities.BlackVarianceCurve;
import org.jquantlib.termstructures.volatilities.LocalConstantVol;
import org.jquantlib.termstructures.volatilities.LocalVolCurve;
import org.jquantlib.termstructures.volatilities.LocalVolSurface;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;

/**
 * Generalized Black-Scholes stochastic process
 *
 * <p>
 * This class describes the stochastic process governed by
 * <p>
 * {@latex[ dS(t, S) = (r(t) - q(t) - \frac \sigma(t, S)^2}{2}) dt + \sigma dW_t. }
 *
 * @author Richard Gomes
 */
public class GeneralizedBlackScholesProcess extends StochasticProcess1D {

    private final Handle<? extends Quote> x0;

    private final Handle<YieldTermStructure> riskFreeRate;

    private final Handle<YieldTermStructure> dividendYield;

    private final Handle<BlackVolTermStructure> blackVolatility;

    private final RelinkableHandle<LocalVolTermStructure> localVolatility;

    private boolean updated;

    /**
     * @param discretization
     *            is an Object that <b>must</b> implement {@link Discretization}
     *            <b>and</b> {@link Discretization1D}.
     */
    public GeneralizedBlackScholesProcess(final Handle<? extends Quote> x0, final Handle<YieldTermStructure> dividendTS, final Handle<YieldTermStructure> riskFreeTS, final Handle<BlackVolTermStructure> blackVolTS) {
        this(x0, dividendTS, riskFreeTS, blackVolTS, new EulerDiscretization());
    }

    /**
     * @param discretization
     *            is an Object that <b>must</b> implement {@link Discretization}
     *            <b>and</b> {@link Discretization1D}.
     */
    public GeneralizedBlackScholesProcess(final Handle<? extends Quote> x0, final Handle<YieldTermStructure> dividendTS, final Handle<YieldTermStructure> riskFreeTS, final Handle<BlackVolTermStructure> blackVolTS, final StochasticProcess1D.Discretization1D discretization) {
        super(discretization);
        this.localVolatility = new RelinkableHandle<LocalVolTermStructure>();
        this.x0 = x0;
        this.riskFreeRate = riskFreeTS;
        this.dividendYield = dividendTS;
        this.blackVolatility = blackVolTS;
        this.updated = false;
        this.x0.addObserver(this);
        this.riskFreeRate.addObserver(this);
        this.dividendYield.addObserver(this);
        this.blackVolatility.addObserver(this);
    }

    public final Handle<? extends Quote> stateVariable() {
        return x0;
    }

    public final Handle<YieldTermStructure> dividendYield() {
        return dividendYield;
    }

    public final Handle<YieldTermStructure> riskFreeRate() {
        return riskFreeRate;
    }

    public final Handle<BlackVolTermStructure> blackVolatility() {
        return blackVolatility;
    }

    public final Handle<LocalVolTermStructure> localVolatility() {
        if (!updated) {
            final Class<? extends BlackVolTermStructure> klass = blackVolatility.currentLink().getClass();
            if (BlackConstantVol.class.isAssignableFrom(klass)) {
                final BlackConstantVol constVol = (BlackConstantVol) blackVolatility.currentLink();
                localVolatility.linkTo(new LocalConstantVol(constVol.referenceDate(), constVol.blackVol(0.0, x0.currentLink().value()), constVol.dayCounter()));
                updated = true;
                return localVolatility;
            }
            if (BlackVarianceCurve.class.isAssignableFrom(klass)) {
                final Handle<BlackVarianceCurve> volCurve = new Handle<BlackVarianceCurve>((BlackVarianceCurve) blackVolatility().currentLink());
                localVolatility.linkTo(new LocalVolCurve(volCurve));
                updated = true;
                return localVolatility;
            }
            if (LocalVolSurface.class.isAssignableFrom(klass)) {
                localVolatility.linkTo(new LocalVolSurface(blackVolatility, riskFreeRate, dividendYield, x0));
                updated = true;
                return localVolatility;
            }
            throw new LibraryException("unrecognized volatility curve");
        } else return localVolatility;
    }

    @Override
    public double x0() {
        return x0.currentLink().value();
    }

    @Override
    public double drift(final double t, final double x) {
        final double sigma = diffusion(t, x);
        final double t1 = t + 0.0001;
        final YieldTermStructure yts = riskFreeRate.currentLink();
        final double r = yts.forwardRate(t, t1, Compounding.Continuous, Frequency.NoFrequency, true).rate();
        final YieldTermStructure divTs = dividendYield.currentLink();
        final double d = divTs.forwardRate(t, t1, Compounding.Continuous, Frequency.NoFrequency, true).rate();
        return r - d - 0.5 * sigma * sigma;
    }

    @Override
    public double diffusion(final double t, final double x) {
        final double vol = localVolatility().currentLink().localVol(t, x, true);
        return vol;
    }

    @Override
    public final double apply(final double x0, final double dx) {
        final double result = x0 * Math.exp(dx);
        return result;
    }

    @Override
    public final double time(final Date d) {
        final YieldTermStructure yts = riskFreeRate.currentLink();
        return yts.dayCounter().yearFraction(yts.referenceDate(), d);
    }

    @Override
    public final void update() {
        updated = false;
        super.update();
    }
}
