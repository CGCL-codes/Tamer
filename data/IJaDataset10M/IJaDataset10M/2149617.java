package org.dozer.functional_tests.proxied;

import org.dozer.functional_tests.DataObjectInstantiator;
import org.dozer.functional_tests.GranularDozerBeanMapperTest;

/**
 * @author tierney.matt
 * @author garsombke.franz
 */
public class ProxiedGranularDozerBeanMapperTest extends GranularDozerBeanMapperTest {

    @Override
    protected DataObjectInstantiator getDataObjectInstantiator() {
        return ProxyDataObjectInstantiator.INSTANCE;
    }
}
