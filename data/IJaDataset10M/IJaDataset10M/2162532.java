package org.renjin.primitives;

import org.junit.Test;
import org.renjin.EvalTestCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AssignmentTest extends EvalTestCase {

    @Test
    public void stringTarget() throws Exception {
        eval(" \"a\" <- 1 ");
        assertThat(eval("a"), equalTo(c(1)));
    }

    @Test
    public void symbolTarget() throws Exception {
        eval(" x <- 1");
        assertThat(eval("x"), equalTo(c(1)));
    }
}
