package ca.sandstorm.luminance.test.math;

import android.test.AndroidTestCase;
import ca.sandstorm.luminance.math.Lerp;

/**
 * Testing of the Lerp class
 * 
 * @author Chet
 * 
 */
public class LerpTest extends AndroidTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testLerp() throws Exception {
        float start = 0.0f;
        float target = 6.0f;
        float duration = 15.0f;
        float result1 = Lerp.lerp(start, target, duration, 0.0f);
        assert (result1 == start);
        float result2 = Lerp.lerp(start, target, duration, 12.0f);
        float range = target - start;
        float percent = 12.0f / duration;
        float value = start + (range * percent);
        assert (result2 == value);
        float result3 = Lerp.lerp(start, target, duration, 20.0f);
        assert (result3 == target);
    }

    public void testEase() throws Exception {
        float start = 0.0f;
        float target = 6.0f;
        float duration = 15.0f;
        float result1 = Lerp.ease(start, target, duration, 0.0f);
        assert (result1 == start);
        float result2 = Lerp.ease(start, target, duration, 12.0f);
        float range = target - start;
        float percent = 12.0f / (duration / 2.0f);
        float value;
        if (percent < 1.0f) {
            value = start + ((range / 2.0f) * percent * percent * percent);
        } else {
            float shiftedPercent = percent - 2.0f;
            value = start + ((range / 2.0f) * ((shiftedPercent * shiftedPercent * shiftedPercent) + 2.0f));
        }
        assert (result2 == value);
        float result3 = Lerp.ease(start, target, duration, 20.0f);
        assert (result3 == target);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
