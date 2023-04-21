package com.mycila.testing.plugin.annotation;

import com.mycila.testing.junit.MycilaJunit4Test;
import org.junit.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationTestPluginJunit4Test extends MycilaJunit4Test {

    @Test
    @Skip
    public void must_be_skipped_by_plugin() throws Exception {
        fail("must_be_skipped_by_plugin");
    }

    @Test
    @Skip
    @ExpectException(type = IllegalStateException.class, message = "bla bla bla")
    public void must_throw_an_exception_1() throws Exception {
        fail("must_be_skipped_by_plugin");
        throw new IllegalStateException("exception 1");
    }

    @Test
    @ExpectException(type = IllegalStateException.class)
    public void must_throw_an_exception_2() throws Exception {
        throw new IllegalStateException("exception 2");
    }

    @Test
    @ExpectException(type = IllegalStateException.class, message = "exception 3")
    public void must_throw_an_exception_3() throws Exception {
        throw new IllegalStateException("exception 3");
    }

    @Test
    @ExpectException(type = IllegalStateException.class, containing = "4")
    public void must_throw_an_exception_4() throws Exception {
        throw new IllegalStateException("exception 4");
    }

    @Test
    @ExpectException(type = RuntimeException.class, message = "exception 5")
    public void must_throw_an_exception_5() throws Exception {
        throw new IllegalStateException("exception 5");
    }

    @Test(expected = AssertionError.class)
    @ExpectException(type = IllegalArgumentException.class)
    public void must_throw_an_exception_6() throws Exception {
        throw new IllegalStateException("exception 6");
    }

    @Test(expected = AssertionError.class)
    @ExpectException(type = IllegalStateException.class, message = "bla bla")
    public void must_throw_an_exception_7() throws Exception {
        throw new IllegalStateException("exception 7");
    }

    @Test(expected = AssertionError.class)
    @ExpectException(type = IllegalStateException.class, containing = "bla bla")
    public void must_throw_an_exception_8() throws Exception {
        throw new IllegalStateException("exception 8");
    }
}
