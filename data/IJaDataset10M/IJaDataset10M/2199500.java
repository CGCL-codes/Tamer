package org.callbackparams.internal;

import java.util.Arrays;
import org.callbackparams.junit4.CallbackParamsRunner;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * @author Henrik Kaipe
 */
@RunWith(CallbackParamsRunner.class)
public class TestHamcrestClassloading {

    @Test
    public void createMatchers() {
        Matcher<Iterable<String>> m = JUnitMatchers.hasItem(CoreMatchers.is("foo"));
        assertThat(Arrays.asList("oof", "foo", "rab"), m);
    }
}
