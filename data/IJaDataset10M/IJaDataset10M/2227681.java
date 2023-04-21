package preprocessing.statistic.statisticalBlock.tests;

import jsc.independentsamples.SmirnovTest;
import jsc.tests.H1;
import preprocessing.statistic.statisticalBlock.AbstractTwoSampleTest;
import preprocessing.statistic.statisticalBlock.PairedDataSample;

/**
 * Created by IntelliJ IDEA.
 * User: Kuchar Jaroslav - kuchaj1@fel.cvut.cz
 * Date: 29.3.2009
 * Time: 17:16:55
 */
public class KolmogorovSmirnov extends AbstractTwoSampleTest {

    private double pValue;

    private double testStatistic;

    /**
     * returns name of the two sample test
     *
     * @return String - name of the test
     */
    @Override
    public String getName() {
        return "Kolmogorov Smirnov Test";
    }

    /**
     * returns description of test
     *
     * @return String - test description
     */
    @Override
    public String getDescription() {
        return "This tests the null hypothesis that A and B have the same distribution";
    }

    /**
     * returns result of test - p value
     *
     * @return double - p value = result of test
     */
    @Override
    public double getPValue() {
        return this.pValue;
    }

    /**
     * returns result of test
     *
     * @return double - test statistic of current test
     */
    @Override
    public double getTestStatistic() {
        return this.testStatistic;
    }

    /**
     * start of the test with specified alternative hypothese
     *
     * @param data                  Paired data sample for processing the test
     * @param alternativeHypothesis code of the alternative hypothese
     *                              0 - NOT_EQUAL
     *                              1 - LESS_THAN
     *                              2 - GREATER_THAN
     */
    @Override
    public void processTest(PairedDataSample data, int alternativeHypothesis) {
        SmirnovTest kt = null;
        try {
            switch(alternativeHypothesis) {
                default:
                case 0:
                    kt = new SmirnovTest(data.getFirstDataSample(), data.getSecondDataSample(), H1.NOT_EQUAL);
                    break;
                case 1:
                    kt = new SmirnovTest(data.getFirstDataSample(), data.getSecondDataSample(), H1.LESS_THAN);
                    break;
                case 2:
                    kt = new SmirnovTest(data.getFirstDataSample(), data.getSecondDataSample(), H1.GREATER_THAN);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pValue = kt.getSP();
        this.testStatistic = kt.getTestStatistic();
    }
}
