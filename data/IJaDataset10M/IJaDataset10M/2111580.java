package com.avaje.tests.cache;

import junit.framework.Assert;
import junit.framework.TestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheStatistics;
import com.avaje.tests.model.basic.Address;
import com.avaje.tests.model.basic.Country;
import com.avaje.tests.model.basic.Customer;
import com.avaje.tests.model.basic.ResetBasicData;

public class TestCacheBasic extends TestCase {

    public void test() {
        ResetBasicData.reset();
        Ebean.getServerCacheManager().clear(Country.class);
        ServerCache countryCache = Ebean.getServerCacheManager().getBeanCache(Country.class);
        Ebean.runCacheWarming(Country.class);
        Assert.assertTrue(countryCache.size() > 0);
        countryCache.getStatistics(true);
        Country c0 = Ebean.getReference(Country.class, "NZ");
        ServerCacheStatistics statistics = countryCache.getStatistics(false);
        int hc = statistics.getHitCount();
        Assert.assertEquals(1, hc);
    }
}
