package org.jazzteam;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class TimeTest {

    @Test
    public void testtime() {
        String D;
        D = Time.show();
        assertEquals(" 20 40 40", D);
    }
}
