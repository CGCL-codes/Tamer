package org.nakedobjects.nof.reflect.java.facets.propparam.validate.mask;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MaskEvaluatorTest {

    private MaskEvaluator maskEvaluator;

    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] { { "AAA", "aby", true }, { "A", "A", true }, { "A", "Z", true }, { "A", "a", true }, { "A", "z", true }, { "A", "0", true }, { "A", "9", true }, { "A", "@", false }, { "A", "~", false }, { "a", "A", true }, { "a", "Z", true }, { "a", "a", true }, { "a", "z", true }, { "a", "0", true }, { "a", "9", true }, { "a", " ", true }, { "a", "~", false }, { "9", "z", false }, { "9", "0", true }, { "9", "9", true }, { "9", " ", true }, { "9", "~", false }, { "&", "A", true }, { "&", "Z", true }, { "&", "a", true }, { "&", "z", true }, { "&", "9", false }, { "?", "A", true }, { "?", "Z", true }, { "?", "a", true }, { "?", "z", true }, { "U", "A", true }, { "U", "Z", true }, { "U", "a", false }, { "L", "a", true }, { "L", "z", true }, { "L", "A", false }, { "#", "0", true }, { "#", "9", true }, { "#", "X", false }, { "#", "#", false }, { ",", ",", true }, { ":", ":", true }, { ":", "X", false }, { "/", "/", true }, { "/", "X", false }, { "##", "23", true } });
    }

    private Locale previousLocale = Locale.getDefault();

    @Before
    public void setUp() {
        previousLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void tearDown() {
        Locale.setDefault(previousLocale);
    }

    private final String mask;

    private final String proposed;

    private final boolean expected;

    public MaskEvaluatorTest(final String mask, final String proposed, final boolean expected) {
        this.mask = mask;
        this.proposed = proposed;
        this.expected = expected;
    }

    @Test
    public void all() {
        assertThat(new MaskEvaluator(mask).evaluate(proposed), is(expected));
    }
}
