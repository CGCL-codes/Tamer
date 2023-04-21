package com.googlecode.ehcache.annotations.integration;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;

/**
 * @author Eric Dalquist
 * @version $Revision: 656 $
 */
public interface KeyGeneratorAnnotationTestInterface {

    @Cacheable(cacheName = "interfaceAnnotatedCache", keyGenerator = @KeyGenerator(name = "ListCacheKeyGenerator", properties = { @Property(name = "useReflection", value = "true"), @Property(name = "checkforCycles", value = "true"), @Property(name = "includeMethod", value = "false") }))
    public String listCacheKeyGenerator();

    public int listCacheKeyGeneratorCount();

    @Cacheable(cacheName = "interfaceAnnotatedCache", keyGenerator = @KeyGenerator(name = "com.googlecode.ehcache.annotations.integration.MockCacheKeyGenerator"))
    public String customGeneratorOne();

    @Cacheable(cacheName = "interfaceAnnotatedCache", keyGenerator = @KeyGenerator(name = "com.googlecode.ehcache.annotations.integration.MockCacheKeyGenerator", properties = { @Property(name = "enumProperty", value = "SECONDS"), @Property(name = "listProperty", ref = "listBean") }))
    public String customGeneratorTwo();

    @Cacheable(cacheName = "interfaceAnnotatedCache", keyGenerator = @KeyGenerator(name = "com.googlecode.ehcache.annotations.integration.MockCacheKeyGenerator", properties = { @Property(name = "listProperty", ref = "listBean"), @Property(name = "enumProperty", value = "SECONDS") }))
    public String customGeneratorThree();

    @Cacheable(cacheName = "interfaceAnnotatedCache", keyGenerator = @KeyGenerator(name = "com.googlecode.ehcache.annotations.integration.MockCacheKeyGenerator", properties = { @Property(name = "enumProperty", value = "SECONDS") }))
    public String customGeneratorFour();
}
