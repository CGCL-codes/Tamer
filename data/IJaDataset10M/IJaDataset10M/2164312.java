package net.sf.doodleproject.numerics4j.statistics.distribution;

import net.sf.doodleproject.numerics4j.NumericTestCase;
import net.sf.doodleproject.numerics4j.exception.NumericException;

public class LogNormalDistributionTest extends NumericTestCase {

    public void testConstructor() {
        testConstructorFailure(Double.NaN, 1.0);
        testConstructorSuccess(-1.0, 1.0);
        testConstructorSuccess(0.0, 1.0);
        testConstructorSuccess(1.0, 1.0);
        testConstructorFailure(0.0, Double.NaN);
        testConstructorFailure(0.0, -1.0);
        testConstructorFailure(0.0, 0.0);
    }

    private void testConstructorFailure(double mean, double std) {
        try {
            new LogNormalDistribution(mean, std);
            fail("Invalid constructor values.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testConstructorSuccess(double mean, double std) {
        try {
            LogNormalDistribution d = new LogNormalDistribution(mean, std);
            assertEquals(mean, d.getMean(), 0.0);
            assertEquals(std, d.getStandardDeviation(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid constructor values.");
        }
    }

    public void testCumulativeProbability() throws NumericException {
        LogNormalDistribution d = new LogNormalDistribution();
        testCumulativeProbabilitySuccess(d, 0.0846992079112914, 0.213494339178526, 1.63222927983401, 0.0501667114444123);
        testCumulativeProbabilitySuccess(d, 1.14415366273478, 1.93149588651024, 1.07181486166259, 0.0468262593805173);
        testCumulativeProbabilitySuccess(d, 1.16489821489501, 0.205824798735851, 0.94121155669843, 0.477466402197463);
        testCumulativeProbabilitySuccess(d, 1.48398671991943, 1.08148294434386, 1.27761042931849, 0.295451644978065);
        testCumulativeProbabilitySuccess(d, 2.04376308252098, 0.051714296718071, 1.47918183904415, 0.673022195694127);
        testCumulativeProbabilitySuccess(d, 2.15829547253731, 2.90983970915249, 0.890226575783498, 0.00809802156684303);
        testCumulativeProbabilitySuccess(d, 2.52763763722139, 1.21564659545554, 0.401570762135631, 0.23635280121186);
        testCumulativeProbabilitySuccess(d, 3.12363846112485, 2.86208601025708, 0.83744614816213, 0.0198166642409717);
        testCumulativeProbabilitySuccess(d, 3.95607332241778, 2.67474543649612, 0.679912182208231, 0.0279851603626478);
        testCumulativeProbabilitySuccess(d, 4.14211090457512, 2.93655893739318, 1.7952368193712, 0.199307697601704);
        testCumulativeProbabilitySuccess(d, Double.NaN, 0.0, 1.0, Double.NaN);
        testCumulativeProbabilitySuccess(d, -1.0, 0.0, 1.0, 0.0);
        testCumulativeProbabilitySuccess(d, 0.0, 0.0, 1.0, 0.0);
        testCumulativeProbabilitySuccess(d, Double.POSITIVE_INFINITY, 0.0, 1.0, 1.0);
    }

    private void testCumulativeProbabilitySuccess(LogNormalDistribution d, double x, double mean, double std, double p) throws NumericException {
        d.setMean(mean);
        d.setStandardDeviation(std);
        double actual = d.cumulativeProbability(x);
        assertRelativelyEquals(p, actual, 1.0e-10);
    }

    public void testMean() {
        LogNormalDistribution d = new LogNormalDistribution();
        testMeanFailure(d, Double.NaN);
        testMeanSuccess(d, -1.0);
        testMeanSuccess(d, 0.0);
        testMeanSuccess(d, 1.0);
    }

    public void testStandardDeviation() {
        LogNormalDistribution d = new LogNormalDistribution();
        testStandardDeviationFailure(d, Double.NaN);
        testStandardDeviationFailure(d, -1.0);
        testStandardDeviationFailure(d, 0.0);
        testStandardDeviationSuccess(d, 1.0);
    }

    private void testStandardDeviationFailure(LogNormalDistribution d, double std) {
        try {
            d.setStandardDeviation(std);
            fail("Invalid standard deviation.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testStandardDeviationSuccess(LogNormalDistribution d, double std) {
        try {
            d.setStandardDeviation(std);
            assertEquals(std, d.getStandardDeviation(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid standard deviation.");
        }
    }

    private void testMeanFailure(LogNormalDistribution d, double mean) {
        try {
            d.setMean(mean);
            fail("Invalid mean.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testMeanSuccess(LogNormalDistribution d, double mean) {
        try {
            d.setMean(mean);
            assertEquals(mean, d.getMean(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid mean.");
        }
    }

    public void testInverseCumulativeProbability() throws NumericException {
        LogNormalDistribution d = new LogNormalDistribution();
        testInverseCumulativeProbabilitySuccess(d, 0.0250580952258568, 2.59339038339826, 1.10500036126456, 1.53530151738494);
        testInverseCumulativeProbabilitySuccess(d, 0.00619580449331553, 1.62594155051014, 0.42498824830832, 1.75616864456171);
        testInverseCumulativeProbabilitySuccess(d, 0.0185817181172276, 1.74264259188712, 0.576517582869624, 1.7180684528709);
        testInverseCumulativeProbabilitySuccess(d, 0.670083031134589, 0.0643272200787901, 1.33193128919012, 1.91662671587121);
        testInverseCumulativeProbabilitySuccess(d, 0.264594887763313, 2.50469913065748, 2.65566384920612, 2.30168256851382);
        testInverseCumulativeProbabilitySuccess(d, 0.58220143457506, 0.430628886224621, 2.27358419227507, 2.46566715542854);
        testInverseCumulativeProbabilitySuccess(d, 0.5531920036812, 0.703854074964705, 2.63044715332614, 2.87376802368552);
        testInverseCumulativeProbabilitySuccess(d, 0.598001331263916, 0.760664210470685, 1.97918530907014, 3.49681585675957);
        testInverseCumulativeProbabilitySuccess(d, 0.407666473326614, 1.99860512187619, 2.30219045153925, 4.30994173502677);
        testInverseCumulativeProbabilitySuccess(d, 0.342221540601047, 2.3453354497516, 2.04952797440072, 4.53756965522139);
        testInverseCumulativeProbabilitySuccess(d, -1.0, 1.0, 1.0, Double.NaN);
        testInverseCumulativeProbabilitySuccess(d, 0.0, 1.0, 1.0, 0.0);
        testInverseCumulativeProbabilitySuccess(d, 1.0, 1.0, 1.0, Double.POSITIVE_INFINITY);
        testInverseCumulativeProbabilitySuccess(d, 2.0, 1.0, 1.0, Double.NaN);
        testInverseCumulativeProbabilitySuccess(d, Double.NaN, 1.0, 1.0, Double.NaN);
    }

    private void testInverseCumulativeProbabilitySuccess(LogNormalDistribution d, double p, double mean, double std, double expected) throws NumericException {
        d.setMean(mean);
        d.setStandardDeviation(std);
        double actual = d.inverseCumulativeProbability(p);
        assertRelativelyEquals(expected, actual, 1.0e-10);
    }
}
