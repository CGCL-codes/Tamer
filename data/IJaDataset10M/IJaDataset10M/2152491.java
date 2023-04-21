package org.databene.commons.converter;

import static junit.framework.Assert.assertEquals;
import org.databene.commons.TimeUtil;
import org.junit.Test;

/**
 * Tests the {@link Long2DateConverter}.<br/><br/>
 * Created: 26.02.2010 08:30:14
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class Long2DateConverterTest extends AbstractConverterTest {

    public Long2DateConverterTest() {
        super(Long2DateConverter.class);
    }

    @Test
    public void testRevert() {
        assertEquals(null, new Long2DateConverter().convert(null));
        assertEquals(TimeUtil.date(1970, 0, 1, 0, 0, 0, 0), new Long2DateConverter().convert(0L));
    }
}
