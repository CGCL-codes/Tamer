package org.niceassert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.niceassert.NiceMatchers.*;
import java.util.Date;

public class NiceMatchersTest {

    private static final String MESSAGE = "Message";

    @Test
    public void ofTypeWithMessageMatch() {
        assertThat(ofTypeWithMessage(Exception.class, MESSAGE).matches(new Exception(MESSAGE)), is(true));
    }

    @Test
    public void ofTypeWithMessageNoMatch() {
        assertThat(ofTypeWithMessage(Exception.class, "ANOTHER").matches(new Exception(MESSAGE)), is(false));
    }

    @Test
    public void instanceOfMatch() {
        assertThat(instanceOf(String.class).matches(""), is(true));
    }

    @Test
    public void instanceOfNoMatch() {
        assertThat(instanceOf(String.class).matches(new Integer(1)), is(false));
    }

    @Test
    public void assignableFromMatch() {
        assertThat(assignableFrom(Object.class).matches(String.class), is(true));
    }

    @Test
    public void assignableFromNoMatch() {
        assertThat(assignableFrom(String.class).matches(Integer.class), is(false));
    }

    @Test
    public void beforeMatch() {
        assertThat(before(new Date(1)).matches(new Date(0)), is(true));
    }

    @Test
    public void beforeNoMatch() {
        assertThat(before(new Date(1)).matches(new Date(1)), is(false));
        assertThat(before(new Date(1)).matches(new Date(2)), is(false));
    }

    @Test
    public void afterMatch() {
        assertThat(after(new Date(0)).matches(new Date(1)), is(true));
    }

    @Test
    public void afterNoMatch() {
        assertThat(after(new Date(1)).matches(new Date(1)), is(false));
        assertThat(after(new Date(2)).matches(new Date(1)), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void betweenIllegalRange() {
        between(new Date(2), new Date(1));
    }

    @Test
    public void betweenMatch() {
        assertThat(between(new Date(1), new Date(2)).matches(new Date(1)), is(true));
        assertThat(between(new Date(1), new Date(2)).matches(new Date(2)), is(true));
    }

    @Test
    public void betweenNoMatch() {
        assertThat(between(new Date(1), new Date(2)).matches(new Date(0)), is(false));
        assertThat(between(new Date(1), new Date(2)).matches(new Date(3)), is(false));
    }
}
