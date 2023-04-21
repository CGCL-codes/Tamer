package org.jquantlib;

import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import org.jquantlib.ooimpl.MathHelper;
import org.jquantlib.ooimpl.OptionHelper;

public final class JQAddInImpl extends WeakBase implements com.sun.star.lang.XServiceInfo, com.sun.star.lang.XLocalizable, org.jquantlib.XJQAddIn {

    private final XComponentContext m_xContext;

    private static final String m_implementationName = JQAddInImpl.class.getName();

    private static final String[] m_serviceNames = { "org.jquantlib.JQAddIn" };

    private com.sun.star.lang.Locale m_locale = new com.sun.star.lang.Locale();

    public JQAddInImpl(XComponentContext context) {
        m_xContext = context;
    }

    public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
        XSingleComponentFactory xFactory = null;
        if (sImplementationName.equals(m_implementationName)) xFactory = Factory.createComponentFactory(JQAddInImpl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
        return Factory.writeRegistryServiceInfo(m_implementationName, m_serviceNames, xRegistryKey);
    }

    public String getImplementationName() {
        return m_implementationName;
    }

    public boolean supportsService(String sService) {
        int len = m_serviceNames.length;
        for (int i = 0; i < len; i++) {
            if (sService.equals(m_serviceNames[i])) return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    public void setLocale(com.sun.star.lang.Locale eLocale) {
        m_locale = eLocale;
    }

    public com.sun.star.lang.Locale getLocale() {
        return m_locale;
    }

    public int JQlnFactorial(int parameter0) {
        return (int) MathHelper.getlNFactorial(parameter0);
    }

    public double JQfactorial(int n) {
        return MathHelper.getFactorial(n);
    }

    public double BetaContinuedFraction(double BetaContinuedFraction, double b, double x, double accuracy, int a) {
        return 0;
    }

    public int JQIncompleteBetaFunction(double accuracy, double maxiteration, double x, double b, double a) {
        return 0;
    }

    public int JQBetaFunction(double z, double w) {
        return 0;
    }

    public double JQEuropeanBlackScholes(double strike, double underlying, double riskFreeRate, double volatility, double dividendYield, String optionType, int settlementDay, int settlementMonth, int settlementYear, int maturityDay, int maturityMonth, int maturityYear) {
        double npv = OptionHelper.europeanBlackScholes(strike, underlying, riskFreeRate, volatility, dividendYield, optionType, settlementDay, settlementMonth, settlementYear, maturityDay, maturityMonth, maturityYear);
        if (npv >= 0.0) return npv; else return 0.0;
    }

    public double JQEuropeanBlackScholesCall(double strike, double underlying, double riskFreeRate, double volatility, double dividendYield, int settlementDay, int settlementMonth, int settlementYear, int maturityDay, int maturityMonth, int maturityYear) {
        return 0.0;
    }

    public double JQBlackFormula(double strike, double stdDev, double forward, String optionType) {
        return 0;
    }

    public int JQEvaluatePoissonDistribution(double mu, double sigma) {
        return (int) MathHelper.evaluatePoissonDistribution(mu, sigma);
    }

    public double JQEvaluateNonCentralChiSquaredDistribution(double df, double x, double ncp) {
        return MathHelper.evaluateNonCentralChiSquaredDistribution(df, x, ncp);
    }

    public double JQEvaluateInverseCumulativePoisson(double lambda, double x) {
        return MathHelper.evaluateInverseCumulativePoisson(lambda, x);
    }

    public double JQEvaluateInverseCumulativeNormal(double average, double sigma, double x) {
        return MathHelper.evaluateInverseCumulativeNormal(average, sigma, x);
    }

    public double JQEvaluateGammaDistribution(double a, double x) {
        return MathHelper.evaluateGammaDistribution(a, x);
    }

    public double JQEvaluateCumulativePoissonDistribution(double mean, int k) {
        return MathHelper.evaluateCumulativePoissonDistribution(mean, k);
    }

    public double JQEvaluateCumulativeNormalDistribution(double mean, double sigma, double z) {
        return MathHelper.evaluateCumulativeNormalDistribution(mean, sigma, z);
    }

    public int JQEvaluateBinomialDistributionValue(double probability, int k) {
        return (int) MathHelper.evaluateBinomialDistributionValue(probability, k);
    }

    public double JQGetPrimeNumberAt(double absoluteIndex) {
        int n = (int) absoluteIndex;
        return MathHelper.getPrimeNumberAt(n);
    }
}
