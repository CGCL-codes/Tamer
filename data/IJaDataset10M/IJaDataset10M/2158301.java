package com.threerings.antidote;

import org.junit.Test;
import com.threerings.antidote.EnumHelper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumHelperTest {

    @Test
    public void testParseEnum() {
        assertEquals(TestEnum.TESTFIELD, EnumHelper.parseEnum("TESTFIELD", TestEnum.class));
        assertEquals(TestEnum.TESTFIELD, EnumHelper.parseEnum("testfield", TestEnum.class));
        assertEquals(TestEnum.TESTFIELD, EnumHelper.parseEnum("TestField", TestEnum.class));
        assertNull(EnumHelper.parseEnum("bogus-field", TestEnum.class));
    }
}
