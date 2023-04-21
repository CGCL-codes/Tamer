package org.jquantlib.instruments;

import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.AnalyticDividendEuropeanEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDDividendAmericanEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.time.Date;

/**
 * Single-asset vanilla option (no barriers) with discrete dividends
 *
 * @category instruments
 *
 * @author Richard Gomes
 */
public class DividendVanillaOption extends VanillaOption {

    private static final String WRONG_ARGUMENT_TYPE = "wrong argument type";

    private final List<? extends Dividend> cashFlow;

    public DividendVanillaOption(final Payoff payoff, final Exercise exercise, final List<Date> dates, final List<Double> dividends) {
        super(payoff, exercise);
        cashFlow = Dividend.DividendVector(dates, dividends);
    }

    @Override
    public double impliedVolatility(final double price, final GeneralizedBlackScholesProcess process) {
        return impliedVolatility(price, process, 1.0e-4, 100, 1.0e-7, 4.0);
    }

    @Override
    public double impliedVolatility(final double price, final GeneralizedBlackScholesProcess process, final double accuracy) {
        return impliedVolatility(price, process, accuracy, 100, 1.0e-7, 4.0);
    }

    @Override
    public double impliedVolatility(final double price, final GeneralizedBlackScholesProcess process, final double accuracy, final int maxEvaluations) {
        return impliedVolatility(price, process, accuracy, maxEvaluations, 1.0e-7, 4.0);
    }

    @Override
    public double impliedVolatility(final double price, final GeneralizedBlackScholesProcess process, final double accuracy, final int maxEvaluations, final double minVol) {
        return impliedVolatility(price, process, accuracy, maxEvaluations, minVol, 4.0);
    }

    @Override
    public double impliedVolatility(final double targetValue, final GeneralizedBlackScholesProcess process, final double accuracy, final int maxEvaluations, final double minVol, final double maxVol) {
        QL.require(!isExpired(), "option expired");
        final SimpleQuote volQuote = new SimpleQuote();
        final GeneralizedBlackScholesProcess newProcess = ImpliedVolatilityHelper.clone(process, volQuote);
        final PricingEngine engine;
        switch(exercise.type()) {
            case European:
                engine = new AnalyticDividendEuropeanEngine(newProcess);
                break;
            case American:
                engine = new FDDividendAmericanEngine(newProcess);
                break;
            case Bermudan:
                throw new LibraryException("engine not available for Bermudan option with dividends");
            default:
                throw new LibraryException("unknown exercise type");
        }
        return ImpliedVolatilityHelper.calculate(this, engine, volQuote, targetValue, accuracy, maxEvaluations, minVol, maxVol);
    }

    @Override
    public void setupArguments(final PricingEngine.Arguments args) {
        QL.require(DividendVanillaOption.ArgumentsImpl.class.isAssignableFrom(args.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE);
        super.setupArguments(args);
        final DividendVanillaOption.ArgumentsImpl arguments = (DividendVanillaOption.ArgumentsImpl) args;
        arguments.cashFlow = cashFlow;
    }

    public static class ArgumentsImpl extends VanillaOption.ArgumentsImpl implements DividendVanillaOption.Arguments {

        public List<? extends Dividend> cashFlow;

        @Override
        public void validate() {
            super.validate();
            final Date exerciseDate = exercise.lastDate();
            for (int i = 0; i < cashFlow.size(); i++) {
                final Date d = cashFlow.get(i).date();
                QL.require(d.le(exerciseDate), "dividend date later than the exercise date");
            }
        }
    }

    public static class ResultsImpl extends VanillaOption.ResultsImpl implements DividendVanillaOption.Results {
    }

    public abstract static class EngineImpl extends VanillaOption.EngineImpl implements DividendVanillaOption.Engine {

        protected EngineImpl() {
            super(new DividendVanillaOption.ArgumentsImpl(), new DividendVanillaOption.ResultsImpl());
        }
    }
}
