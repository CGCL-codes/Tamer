package org.jquantlib.examples.utils;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.TypePayoff;
import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.jquantlib.math.randomnumbers.InverseCumulative;
import org.jquantlib.math.randomnumbers.InverseCumulativeRsg;
import org.jquantlib.math.randomnumbers.MersenneTwisterUniformRng;
import org.jquantlib.math.randomnumbers.PseudoRandom;
import org.jquantlib.math.randomnumbers.RandomNumberGenerator;
import org.jquantlib.math.randomnumbers.RandomSequenceGenerator;
import org.jquantlib.math.randomnumbers.RandomSequenceGeneratorIntf;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.methods.montecarlo.MonteCarloModel;
import org.jquantlib.methods.montecarlo.PathGenerator;
import org.jquantlib.methods.montecarlo.SingleVariate;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;

public class ReplicationError {

    private Number maturity_;

    private PlainVanillaPayoff payoff_;

    private Number s0_;

    private Number sigma_;

    private Number r_;

    private Number vega_;

    public ReplicationError(final Option.Type type, final Number maturity, final Number strike, final Number s0, final Number sigma, final Number r) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.maturity_ = maturity;
        payoff_ = new PlainVanillaPayoff(type, strike.doubleValue());
        this.s0_ = s0;
        this.sigma_ = sigma;
        this.r_ = r;
        double rDiscount = Math.exp(-(r.doubleValue()) * maturity_.doubleValue());
        double qDiscount = 1.0;
        double forward = s0_.doubleValue() * qDiscount / rDiscount;
        double stdDev = Math.sqrt(sigma_.doubleValue() * sigma_.doubleValue() * maturity_.doubleValue());
        BlackCalculator black = new BlackCalculator(payoff_, forward, stdDev, rDiscount);
        System.out.println("Option value: " + black.value());
        vega_ = black.vega(maturity.doubleValue());
        System.out.println("Vega: " + vega_);
    }

    public void compute(int nTimeSteps, int nSamples) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Calendar calendar = Target.getCalendar();
        Date today = DefaultDate.getTodaysDate();
        DayCounter dayCount = Actual365Fixed.getDayCounter();
        Handle<Quote> stateVariable = new Handle(new SimpleQuote(s0_.doubleValue()));
        Handle<YieldTermStructure> riskFreeRate = new Handle(new FlatForward(today, r_.doubleValue(), dayCount));
        Handle<YieldTermStructure> dividendYield = new Handle(new FlatForward(today, 0.0, dayCount));
        Handle<BlackVolTermStructure> volatility = new Handle(new BlackConstantVol(today, calendar, sigma_.doubleValue(), dayCount));
        StochasticProcess1D diffusion = new BlackScholesMertonProcess(stateVariable, dividendYield, riskFreeRate, volatility);
        InverseCumulativeRsg<RandomSequenceGenerator<MersenneTwisterUniformRng>, InverseCumulativeNormal> rsg = new PseudoRandom().makeSequenceGenerator(nTimeSteps, 0);
        boolean brownianBridge = false;
        MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics> MCSimulation = new MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics>();
        MCSimulation.addSamples(nSamples);
        Statistics s = MCSimulation.sampleAccumulator();
        double PLMean = s.mean();
        double PLStDev = MCSimulation.sampleAccumulator().standardDeviation();
        double PLSkew = MCSimulation.sampleAccumulator().skewness();
        double PLKurt = MCSimulation.sampleAccumulator().kurtosis();
        double theorStD = Math.sqrt((Math.PI / 4 / nTimeSteps) * vega_.doubleValue() * sigma_.doubleValue());
        StringBuffer sb = new StringBuffer();
        sb.append(nSamples).append(" | ");
        sb.append(nTimeSteps).append(" | ");
        sb.append(PLMean).append(" | ");
        sb.append(PLStDev).append(" | ");
        sb.append(theorStD).append(" | ");
        sb.append(PLSkew).append(" | ");
        sb.append(PLKurt).append(" \n");
        System.out.println(sb.toString());
    }
}
