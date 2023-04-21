package org.vizzini.game.boardgame;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Provides unit tests for the <code>MarkingEnum</code> class.
 *
 * <p>By default, all test methods (methods with a <code>@Test</code>
 * annotation) are run.  Specify individual test methods to ignore using the
 * <code>@Ignore</code> annotation. See the references below to run individual
 * tests from the command line.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class MarkingEnumTest {

    /**
     * Test the <code>getDisplayName()</code> method.
     *
     * @since  v0.4
     */
    @Test
    public void getDisplayName() {
        assertThat(MarkingEnum.CONSTANT.getDisplayName(), is("Constant"));
        assertThat(MarkingEnum.VARYING.getDisplayName(), is("Varying"));
    }

    /**
     * Test the <code>values()</code> method.
     *
     * @since  v0.4
     */
    @Test
    public void values() {
        MarkingEnum[] values = MarkingEnum.values();
        assertThat(values, is(not(nullValue())));
        assertThat(values.length, is(2));
    }
}
