package org.fjank.jcache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.util.jcache.CacheAccessFactory;
import junit.framework.TestCase;

/**
 * @author Frank Karlstr�m
 */
public class CacheObjectInfoImplTest extends TestCase {

    private CacheObject testObj;

    private CacheObjectInfoImpl impl;

    private static final long TTL = 5;

    @Override
    protected void setUp() throws Exception {
        testObj = new CacheObject("key", "value", new CacheGroup("group"), new CacheRegion("name", new AttributesImpl()), null);
        CacheAccessFactory.getInstance();
        AttributesImpl att = new AttributesImpl();
        att.setTimeToLive(TTL);
        testObj.setAttributes(att);
        impl = new CacheObjectInfoImpl(testObj);
    }

    public final void testGetRegion() {
        impl.getRegion();
    }

    public final void testGetName() {
        impl.getName();
    }

    public final void testGetType() {
        impl.getType();
    }

    public final void testGetGroup() {
        impl.getGroup();
    }

    public final void testGetRefCount() {
        impl.getRefCount();
    }

    public final void testGetAccesses() {
        impl.getAccesses();
    }

    public final void testGetExpire() throws ParseException {
        SimpleDateFormat form = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        AttributesImpl att = testObj.getAttributes();
        long ttl = att.getTimeToLive();
        long createTime = att.getCreateTime();
        long expire = form.parse(impl.getExpire()).getTime();
        long expectedExpiry = form.parse(form.format(new Date((ttl * 1000) + createTime))).getTime();
        assertEquals(expectedExpiry, expire);
        CacheObject obj = new CacheObject("key", "value", null, null, null);
        CacheAccessFactory.getInstance();
        AttributesImpl att1 = new AttributesImpl();
        obj.setAttributes(att1);
        CacheObjectInfoImpl imp = new CacheObjectInfoImpl(obj);
        assertEquals(CacheObjectInfoImpl.NEVER_EXPIRES, imp.getExpire());
    }
}
