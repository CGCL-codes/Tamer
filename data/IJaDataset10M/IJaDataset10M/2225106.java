package org.unitmetrics.experts.ranking;

import org.unitmetrics.experts.IFixPoint;
import org.unitmetrics.junit.TestCase;

/**
 * @author Martin Kersten
 */
public class RankingFactoryTest extends TestCase {

    public void testCreationOfStrategies() {
        assertType("Strategy should be a NearestValueRankingStrategy", NearestValueRankingFunction.class, RankingFactory.createNearestValueStrategy());
        assertType("Strategy should be a LinearRankingStrategy", LinearRankingFunction.class, RankingFactory.createLinearStrategy());
        assertType("Strategy should be a PlateauRankingStrategy", PlateauRankingFunction.class, RankingFactory.createPlateauStrategy());
    }

    public void testCreationOfFixPoints() {
        assertPoint("Default weights should be one", 10, .5, 1, 1, RankingFactory.createPoint(10, .5));
        assertPoint("Weights should equal", 10, .5, .75, .75, RankingFactory.createPoint(10, .5, .75));
        assertPoint("Weights should be {.25, .75}", 10, .5, .25, .75, RankingFactory.createPoint(10, .5, .25, .75));
    }

    private void assertType(String message, Class expectedType, Object actualObject) {
        if (!expectedType.isInstance(actualObject)) fail(message);
    }

    private void assertPoint(String message, double expectedValue, double expectedQuality, double expectedLeftWeight, double expectedRightWeight, IFixPoint point) {
        assertEquals(message + "[value should as expected]", expectedValue, point.value(), 0);
        assertEquals(message + "[quality should as expected]", expectedQuality, point.quality(), 0);
        assertEquals(message + "[left-weight should as expected]", expectedLeftWeight, point.leftWeight(), 0);
        assertEquals(message + "[right-weight should as expected]", expectedRightWeight, point.rightWeight(), 0);
    }
}
