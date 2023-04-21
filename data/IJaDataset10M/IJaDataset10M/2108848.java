package org.jquantlib.ooimpl;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.EuropeanOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Target;

/**
 *
 * @author Praneet Tiwari
 */
public class OptionHelper {

    public static double europeanBlackScholes(double strike, double underlying, double riskFreeRate, double volatility, double dividendYield, String optionType, int settlementDay, int settlementMonth, int settlementYear, int maturityDay, int maturityMonth, int maturityYear) {
        Date settlementDate = new Date(settlementDay, settlementMonth, settlementYear);
        Date maturityDate = new Date(maturityDay, maturityMonth, maturityYear);
        Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(settlementDate);
        DayCounter dayCounter = new Actual365Fixed();
        Option.Type type;
        if (optionType.equalsIgnoreCase("call") || optionType.equalsIgnoreCase("c")) {
            type = Option.Type.Call;
        } else if (optionType.equalsIgnoreCase("put") || optionType.equalsIgnoreCase("p")) {
            type = Option.Type.Put;
        } else throw new IllegalArgumentException("Invalid option type");
        Exercise europeanExercise = new EuropeanExercise(maturityDate);
        Payoff payoff = new PlainVanillaPayoff(type, strike);
        final Calendar calendar = new Target();
        Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
        Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield, dayCounter));
        Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate, dayCounter));
        Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate, calendar, volatility, dayCounter));
        GeneralizedBlackScholesProcess stochasticProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS);
        VanillaOption europeanOption = new EuropeanOption(payoff, europeanExercise);
        europeanOption.setPricingEngine(new AnalyticEuropeanEngine(stochasticProcess));
        return europeanOption.NPV();
    }

    public static void main(String args[]) {
        System.out.println(europeanBlackScholes(30, 4030, 0.05, 0.2, 0.0, "put", 1, 1, 2011, 1, 2, 2011));
    }
}
