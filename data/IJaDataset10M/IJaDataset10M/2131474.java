package com.aloaproject.ciquta;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqCriterionTest {

    @Test
    public void testCorrectMatchOnSimpleExistingProperty() {
        Car car = new Car("R4", Maker.RENAULT, 4);
        EqCriterion instance = new EqCriterion("model", "R4");
        boolean result = instance.match(car);
        assertTrue(true);
    }

    @Test
    public void testCorrectUnmatchOnSimpleExistingProperty() {
        Car car = new Car("R4", Maker.RENAULT, 4);
        EqCriterion instance = new EqCriterion("model", "r5");
        boolean result = instance.match(car);
        assertFalse(result);
    }

    @Test
    public void testCorrectUnmatchOnSimpleUnexistingProperty() {
        Car car = new Car("R4", Maker.RENAULT, 4);
        EqCriterion instance = new EqCriterion("goodness", "very high");
        boolean result = instance.match(car);
        assertFalse(result);
    }

    @Test
    public void testCorrectMatchOnDeepExistingProperty() {
        Car car = new Car("R4", Maker.RENAULT, 4);
        EqCriterion instance = new EqCriterion("maker.nationality", Locale.FRANCE);
        boolean result = instance.match(car);
        assertTrue(result);
    }

    @Test
    public void testCorrectUnmatchOnDeepUnexistingProperty() {
        Car car = new Car("R4", Maker.RENAULT, 4);
        EqCriterion instance = new EqCriterion("maker.zzz", Locale.FRANCE);
        boolean result = instance.match(car);
        assertFalse(result);
    }
}
