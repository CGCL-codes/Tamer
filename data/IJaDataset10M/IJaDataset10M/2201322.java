package com.google.code.ssm.providers.spymemcached;

import static org.junit.Assert.assertEquals;
import com.google.code.ssm.providers.CachedObject;
import net.spy.memcached.CachedData;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Jakub Białek
 * 
 */
public class CachedObjectWrapperTest {

    private CachedObjectWrapper cachedObjectWrapper;

    private CachedData cachedData;

    private int flags = 7;

    private byte[] data = new byte[] { 1, 1, 1, 1, 0, 0 };

    @Before
    public void setUp() {
        cachedData = new CachedData(flags, data, CachedObject.MAX_SIZE);
        cachedObjectWrapper = new CachedObjectWrapper(cachedData);
    }

    @Test
    public void getData() {
        assertEquals(data, cachedObjectWrapper.getData());
    }

    @Test
    public void getFlags() {
        assertEquals(flags, cachedObjectWrapper.getFlags());
    }
}
