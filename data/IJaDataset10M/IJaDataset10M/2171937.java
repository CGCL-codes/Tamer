package gpl.lonelysingleton.awakener.tests.tsp;

import gpl.lonelysingleton.awakener.tsp.PathIndividualFactory;
import gpl.lonelysingleton.awakener.tsp.Vector2DAlleleFactory;
import gpl.lonelysingleton.sleepwalker.genetic.AlleleFactory;
import gpl.lonelysingleton.sleepwalker.tests.genetic.MockIndividual;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

public final class PathIndividualNegativeTest extends TestCase {

    public void testReplaceGenesInterval() {
        final int START_INDEX = 0;
        final int END_INDEX = 1;
        final int MIN_X = 0;
        final int MAX_X = Integer.MAX_VALUE;
        final int MIN_Y = 0;
        final int MAX_Y = Integer.MAX_VALUE;
        final AlleleFactory CITIES_FACTORY;
        final int CITIES_AMOUNT = 10;
        PathIndividualFactory PathsFactoryToTest;
        Set Cities;
        CITIES_FACTORY = new Vector2DAlleleFactory(MIN_X, MAX_X, MIN_Y, MAX_Y);
        Cities = Collections.synchronizedSet(new HashSet(CITIES_AMOUNT));
        for (int i = 0; i < CITIES_AMOUNT; ++i) {
            Cities.add(CITIES_FACTORY.createRandom());
        }
        PathsFactoryToTest = new PathIndividualFactory(Cities);
        try {
            PathsFactoryToTest.createRandom().replaceGenesInterval(START_INDEX, END_INDEX, new MockIndividual());
            fail("replaceGenesInterval(): " + "UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException Exception_P) {
        }
    }
}
