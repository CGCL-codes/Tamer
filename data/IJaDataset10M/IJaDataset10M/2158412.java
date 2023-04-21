package org.nakedobjects.metamodel.commons.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class StringUtilsStripLeadingSlash {

    @Test
    public void shouldStripIfThereIsOne() {
        assertThat(StringUtils.stripLeadingSlash("/foobar"), is("foobar"));
    }

    @Test
    public void shouldLeaveUnchangedIfThereIsNone() {
        assertThat(StringUtils.stripLeadingSlash("foobar"), is("foobar"));
    }

    @Test
    public void shouldConvertSolitarySlashToEmptyString() {
        assertThat(StringUtils.stripLeadingSlash("/"), is(""));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailOnNull() {
        StringUtils.stripLeadingSlash(null);
    }
}
