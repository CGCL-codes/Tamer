package net.sf.doodleproject.numerics4j.statistics.distribution;

import net.sf.doodleproject.numerics4j.NumericTestCase;
import net.sf.doodleproject.numerics4j.exception.NumericException;

public class WeibullDistributionTest extends NumericTestCase {

    public void testConstructor() {
        testConstructorSuccess(1.0, 1.0, 0.0);
        testConstructorFailure(1.0, 1.0, Double.NaN);
        testConstructorFailure(1.0, Double.NaN, 0.0);
        testConstructorFailure(1.0, -1.0, 0.0);
        testConstructorFailure(1.0, 0.0, 0.0);
        testConstructorFailure(Double.NaN, 1.0, 0.0);
        testConstructorFailure(-1.0, 1.0, 0.0);
        testConstructorFailure(0.0, 1.0, 0.0);
    }

    private void testConstructorFailure(double shape, double scale, double location) {
        try {
            new WeibullDistribution(shape, scale, location);
            fail("Invalid constructor values.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testConstructorSuccess(double shape, double scale, double location) {
        try {
            WeibullDistribution d = new WeibullDistribution(shape, scale, location);
            assertEquals(scale, d.getScale(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid constructor values.");
        }
    }

    public void testCumulativeProbability() throws NumericException {
        WeibullDistribution d = new WeibullDistribution();
        testCumulativeProbabilitySuccess(d, 1.22931220058439, 3.81706374680275, 8.44571877491856, 0.0, 0.000638376085637105);
        testCumulativeProbabilitySuccess(d, 2.6413075921965, 6.98902447883083, 4.3151113165992, 1.0, 0.00116343094488824);
        testCumulativeProbabilitySuccess(d, 3.84601931198996, 4.2542337484891, 4.14009577010443, 2.0, 0.0316776899441669);
        testCumulativeProbabilitySuccess(d, 5.38065266236988, 9.78110976282209, 7.55989869815737, 3.0, 0.0000123492734541708);
        testCumulativeProbabilitySuccess(d, 6.43742259677681, 7.89250687858556, 9.75466902744349, 4.0, 0.0000176393942245001);
        testCumulativeProbabilitySuccess(d, 2.47808793573758, 6.70295847020646, 2.57091797453942, 0.0, 0.542293163482414);
        testCumulativeProbabilitySuccess(d, 1.59798459946312, 2.32558700198291, 7.71520750787579, -1.0, 0.0764730920995607);
        testCumulativeProbabilitySuccess(d, 0.75961025842482, 3.99817940798179, 8.7588737092784, -2.0, 0.00982581806183036);
        testCumulativeProbabilitySuccess(d, 0.13851735817706, 9.97996709425872, 4.75241110848113, -3.0, 0.0157857222269193);
        testCumulativeProbabilitySuccess(d, 0.35197403432518, 9.00890898464398, 7.5911652026518, -3.0, 0.000633329190134591);
        testCumulativeProbabilitySuccess(d, -1.0, 1.0, 1.0, 0.0, 0.0);
        testCumulativeProbabilitySuccess(d, 0.0, 1.0, 1.0, 0.0, 0.0);
        testCumulativeProbabilitySuccess(d, Double.POSITIVE_INFINITY, 1.0, 1.0, 0.0, 1.0);
        testCumulativeProbabilitySuccess(d, Double.NaN, 1.0, 1.0, 0.0, Double.NaN);
    }

    private void testCumulativeProbabilitySuccess(WeibullDistribution d, double x, double shape, double scale, double location, double expected) throws NumericException {
        d.setShape(shape);
        d.setScale(scale);
        d.setLocation(location);
        double actual = d.cumulativeProbability(x);
        assertRelativelyEquals(expected, actual, 1.0e-10);
    }

    public void testInverseCumulativeProbability() throws NumericException {
        WeibullDistribution d = new WeibullDistribution();
        testInverseCumulativeProbabilitySuccess(d, 0.105135664813846, 1.22647140233083, 7.22404864034271, 0.0, 1.20407242529073);
        testInverseCumulativeProbabilitySuccess(d, 0.137009533195414, 9.41778427534869, 9.93581237962266, 0.0, 8.10769821208801);
        testInverseCumulativeProbabilitySuccess(d, 0.156931620493814, 2.47006069896281, 9.30130299625793, 0.0, 4.54697142994499);
        testInverseCumulativeProbabilitySuccess(d, 0.161114199246836, 4.05371410473906, 1.47180237488734, 0.0, 0.958368372238279);
        testInverseCumulativeProbabilitySuccess(d, 0.172450933812638, 4.93925614821851, 5.68422863596935, 0.0, 4.05805211541146);
        testInverseCumulativeProbabilitySuccess(d, 0.25660766955989, 3.24969209729778, 8.67948899249644, 0.0, 5.97088833945669);
        testInverseCumulativeProbabilitySuccess(d, 0.413373990115621, 3.24412733412671, 5.54168633211663, 0.0, 4.56560289222832);
        testInverseCumulativeProbabilitySuccess(d, 0.582478526900945, 4.30056004139503, 5.65852689464597, 0.0, 5.48322471903638);
        testInverseCumulativeProbabilitySuccess(d, 0.665759330024324, 8.98339370146095, 1.73829528285226, 0.0, 1.7561048671075);
        testInverseCumulativeProbabilitySuccess(d, 0.915412952903179, 2.99460030909564, 2.53424831934016, 0.0, 3.42754102943836);
        testInverseCumulativeProbabilitySuccess(d, 0.0, 1.0, 1.0, 0.0, 0.0);
        testInverseCumulativeProbabilitySuccess(d, 1.0, 1.0, 1.0, 0.0, Double.POSITIVE_INFINITY);
        testInverseCumulativeProbabilitySuccess(d, Double.NaN, 1.0, 1.0, 0.0, Double.NaN);
        testInverseCumulativeProbabilitySuccess(d, -1.0, 1.0, 1.0, 0.0, Double.NaN);
        testInverseCumulativeProbabilitySuccess(d, 2.0, 1.0, 1.0, 0.0, Double.NaN);
    }

    private void testInverseCumulativeProbabilitySuccess(WeibullDistribution d, double p, double shape, double scale, double location, double expected) throws NumericException {
        d.setShape(shape);
        d.setScale(scale);
        d.setLocation(location);
        double actual = d.inverseCumulativeProbability(p);
        assertRelativelyEquals(expected, actual, 1.0e-10);
    }

    public void testLocation() {
        WeibullDistribution d = new WeibullDistribution();
        testLocationFailure(d, Double.NaN);
        testLocationSuccess(d, 1.0);
    }

    private void testLocationFailure(WeibullDistribution d, double s) {
        try {
            d.setLocation(s);
            fail("Invalid location.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testLocationSuccess(WeibullDistribution d, double s) {
        try {
            d.setLocation(s);
            assertEquals(s, d.getLocation(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid location.");
        }
    }

    public void testScale() {
        WeibullDistribution d = new WeibullDistribution();
        testScaleFailure(d, Double.NaN);
        testScaleFailure(d, -1.0);
        testScaleFailure(d, 0.0);
        testScaleSuccess(d, 1.0);
    }

    private void testScaleFailure(WeibullDistribution d, double scale) {
        try {
            d.setScale(scale);
            fail("Invalid scale.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testScaleSuccess(WeibullDistribution d, double scale) {
        try {
            d.setScale(scale);
            assertEquals(scale, d.getScale(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid scale.");
        }
    }

    public void testShape() {
        WeibullDistribution d = new WeibullDistribution();
        testShapeFailure(d, Double.NaN);
        testShapeFailure(d, -1.0);
        testShapeFailure(d, 0.0);
        testShapeSuccess(d, 1.0);
    }

    private void testShapeFailure(WeibullDistribution d, double s) {
        try {
            d.setShape(s);
            fail("Invalid shape.");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testShapeSuccess(WeibullDistribution d, double s) {
        try {
            d.setShape(s);
            assertEquals(s, d.getShape(), 0.0);
        } catch (IllegalArgumentException ex) {
            fail("Valid shape.");
        }
    }
}
