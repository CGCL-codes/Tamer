package org.fuin.auction.common;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;

public class InternalErrorResultTest {

    @Test
    public final void testConstruction() {
        final InternalErrorResult testee = new InternalErrorResult();
        assertThat(testee.getCode()).isEqualTo(InternalErrorResult.CODE);
    }
}
