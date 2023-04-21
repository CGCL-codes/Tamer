package com.nickokiss.investor.usecase;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.nickokiss.investor.calc.BondCalc;
import com.nickokiss.investor.calc.CashFlowStreamCalc;
import com.nickokiss.investor.fin.element.Bond;
import com.nickokiss.investor.fin.element.CashFlowStream;
import com.nickokiss.investor.fin.element.StreamElement;
import com.nickokiss.investor.fin.env.ConstantInterestRateStrategy;
import com.nickokiss.investor.fin.env.ContinuousCompoundingStrategy;
import com.nickokiss.investor.fin.env.Env;
import com.nickokiss.investor.fin.env.GeneralInterestRateStrategy;
import com.nickokiss.investor.fin.env.PeriodicCompoundingStrategy;
import com.nickokiss.investor.testhelp.TkFinLibTC;

/**
 * 
 * @author Tomasz Koscinski <tomasz.koscinski@nickokiss.com>
 */
public class SpotRatesUseCasesTest extends TkFinLibTC {

    /**
   * If the spot rates for 1 and 2 years are s[1] = 6.3% and s[2] = 6.9%, what is the forward rate f[1,2] ?
   */
    public void testOneForwardRate() throws Exception {
        Env env = new Env();
        Map<BigDecimal, BigDecimal> spotRates = new HashMap<BigDecimal, BigDecimal>();
        spotRates.put(new BigDecimal("1"), new BigDecimal("0.063"));
        spotRates.put(new BigDecimal("2"), new BigDecimal("0.069"));
        env.setInterestRateStrategy(new GeneralInterestRateStrategy(spotRates));
        env.setCompoundingStrategy(new ContinuousCompoundingStrategy());
        BigDecimal forwardRateCC = env.getForwardRate("1", "2");
        checkResult(forwardRateCC, new BigDecimal("0.07500"), "one forward rate (CC)", 5);
        env.setCompoundingStrategy(new PeriodicCompoundingStrategy("1"));
        BigDecimal forwardRatePC = env.getForwardRate("1", "2");
        checkResult(forwardRatePC, new BigDecimal("0.07503"), "one forward rate (PC)", 5);
    }

    /**
   * Given the yearly spot rate curve s = (6.0%, 6.45%, 6.8%, 7.1%, 7.36%, 7.56%, 7.77%), find the spot rate curve for next year.
   */
    public void testSpotUpdate() throws Exception {
        Map<BigDecimal, BigDecimal> spotRates = new HashMap<BigDecimal, BigDecimal>();
        spotRates.put(new BigDecimal("1"), new BigDecimal("0.0600"));
        spotRates.put(new BigDecimal("2"), new BigDecimal("0.0645"));
        spotRates.put(new BigDecimal("3"), new BigDecimal("0.0680"));
        spotRates.put(new BigDecimal("4"), new BigDecimal("0.0710"));
        spotRates.put(new BigDecimal("5"), new BigDecimal("0.0736"));
        spotRates.put(new BigDecimal("6"), new BigDecimal("0.0756"));
        spotRates.put(new BigDecimal("7"), new BigDecimal("0.0777"));
        Env env = new Env();
        env.setInterestRateStrategy(new GeneralInterestRateStrategy(spotRates));
        env.setCompoundingStrategy(new PeriodicCompoundingStrategy("1"));
        BigDecimal forwardRate12 = env.getForwardRate("1", "2");
        BigDecimal forwardRate13 = env.getForwardRate("1", "3");
        BigDecimal forwardRate14 = env.getForwardRate("1", "4");
        BigDecimal forwardRate15 = env.getForwardRate("1", "5");
        BigDecimal forwardRate16 = env.getForwardRate("1", "6");
        BigDecimal forwardRate17 = env.getForwardRate("1", "7");
        checkResult(forwardRate12, new BigDecimal("0.0690"), "forward rate [1,2]", 4);
        checkResult(forwardRate13, new BigDecimal("0.0720"), "forward rate [1,3]", 4);
        checkResult(forwardRate14, new BigDecimal("0.0747"), "forward rate [1,4]", 4);
        checkResult(forwardRate15, new BigDecimal("0.0770"), "forward rate [1,5]", 4);
        checkResult(forwardRate16, new BigDecimal("0.07875"), "forward rate [1,6]", 5);
        checkResult(forwardRate17, new BigDecimal("0.08068"), "forward rate [1,6]", 5);
    }

    /**
   * <pre>
   * Consider two 5-year bonds: 
   * first one has a 9% coupon and sells for 101.00
   * second one has a 7% coupon and sells for 93.20. 
   * 
   * Find the price of a 5-year zero-coupon bond.
   * </pre>
   */
    public void testConstructionOfAZero() throws Exception {
        Bond bond1 = new Bond();
        bond1.setFaceValue("100");
        bond1.setCouponRate("0.09");
        bond1.setPaymentsPerYear("2");
        bond1.setTimeToMaturity("5");
        Bond bond2 = new Bond();
        bond2.setFaceValue("100");
        bond2.setCouponRate("0.07");
        bond2.setPaymentsPerYear("2");
        bond2.setTimeToMaturity("5");
        BondCalc bondCalc = new BondCalc();
        BigDecimal yield1 = bondCalc.getYield(bond1, new BigDecimal("101.00"), 3);
        BigDecimal yield2 = bondCalc.getYield(bond2, new BigDecimal("93.20"), 3);
        checkResult(yield1, new BigDecimal("0.087"), "yield 1", 4);
        checkResult(yield2, new BigDecimal("0.087"), "yield 2", 4);
        Env env = new Env();
        env.setCompoundingStrategy(new PeriodicCompoundingStrategy("2"));
        env.setInterestRateStrategy(new ConstantInterestRateStrategy(yield1));
        StreamElement streamElement = new StreamElement();
        streamElement.setTime("5");
        streamElement.setValue("100");
        BigDecimal result = streamElement.getValue(env, BigDecimal.ZERO);
        checkResult(result, new BigDecimal("65.3244"), "zero coupon bond's price", 4);
    }

    /**
   * <pre>
   * A yearly cash flow stream is x = (-40, 10, 10, 10, 10, 10, 10).
   * The spot rates are: (6.0%, 6.45%, 6.8%, 7.1%, 7.36%, 7.56%, 7.77%)
   * (a) Find the current discount factors and use them to determine the net present value of the stream
   * (b) Find the series of expectations dynamics short-rate discount factors and use the running present value method to evaluate the stream.
   * </pre>
   */
    public void testRunningPV() throws Exception {
        BigDecimal time = new BigDecimal("0");
        Env env = new Env();
        Map<BigDecimal, BigDecimal> spotRates = new HashMap<BigDecimal, BigDecimal>();
        spotRates.put(new BigDecimal("0"), new BigDecimal("0.0600"));
        spotRates.put(new BigDecimal("1"), new BigDecimal("0.0645"));
        spotRates.put(new BigDecimal("2"), new BigDecimal("0.0680"));
        spotRates.put(new BigDecimal("3"), new BigDecimal("0.0710"));
        spotRates.put(new BigDecimal("4"), new BigDecimal("0.0736"));
        spotRates.put(new BigDecimal("5"), new BigDecimal("0.0756"));
        spotRates.put(new BigDecimal("6"), new BigDecimal("0.0777"));
        env.setCompoundingStrategy(new PeriodicCompoundingStrategy(new BigDecimal("1")));
        env.setInterestRateStrategy(new GeneralInterestRateStrategy(spotRates));
        CashFlowStream cashFlowStream = new CashFlowStream();
        cashFlowStream.addElement(new StreamElement(new BigDecimal("-40"), new BigDecimal("0")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("1")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("2")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("3")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("4")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("5")));
        cashFlowStream.addElement(new StreamElement(new BigDecimal("10"), new BigDecimal("6")));
        BigDecimal cfsPresentValueA = cashFlowStream.getValue(env, time);
        CashFlowStreamCalc calc = new CashFlowStreamCalc();
        BigDecimal cfsPresentValueB = calc.getRunningPresentValue(cashFlowStream, env, time);
        checkResult(cfsPresentValueA, cfsPresentValueB, "cash flow stream present value", 8);
    }
}
