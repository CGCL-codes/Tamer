package preprocessing.statistic.statisticalBlock.tests;

import jsc.datastructures.PairedData;
import jsc.onesample.SignTest;
import jsc.tests.H1;
import preprocessing.statistic.statisticalBlock.AbstractTwoSampleTest;
import preprocessing.statistic.statisticalBlock.PairedDataSample;

/**
 * Created by IntelliJ IDEA.
 * User: Kuchar Jaroslav - kuchaj1@fel.cvut.cz
 * Date: 29.3.2009
 * Time: 17:23:14
 */
public class Sign extends AbstractTwoSampleTest {

    private double pValue;

    private double testStatistic;

    /**
     * returns name of the two sample test
     *
     * @return String - name of the test
     */
    public String getName() {
        return "Sign test";
    }

    /**
     * returns description of test
     *
     * @return String - test description
     */
    public String getDescription() {
        return "Tests that X and Y have the same median";
    }

    /**
     * returns result of test - p value
     *
     * @return double - p value = result of test
     */
    public double getPValue() {
        return this.pValue;
    }

    /**
     * returns result of test
     *
     * @return double - test statistic of current test
     */
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
    public void processTest(PairedDataSample data, int alternativeHypothesis) {
        PairedData pd = new PairedData(data.getFirstDataSample(), data.getSecondDataSample());
        SignTest kt = null;
        switch(alternativeHypothesis) {
            default:
            case 0:
                kt = new SignTest(pd, H1.NOT_EQUAL);
                break;
            case 1:
                kt = new SignTest(pd, H1.LESS_THAN);
                break;
            case 2:
                kt = new SignTest(pd, H1.GREATER_THAN);
                break;
        }
        this.pValue = kt.getSP();
        this.testStatistic = kt.getTestStatistic();
    }
}
