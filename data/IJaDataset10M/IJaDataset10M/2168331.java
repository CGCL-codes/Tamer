package org.dozer.functional_tests.proxied;

import org.dozer.functional_tests.DataObjectInstantiator;
import org.dozer.functional_tests.MapTypeTest;

/**
 * @author tierney.matt
 * @author garsombke.franz
 */
public class ProxiedMapTypeTest extends MapTypeTest {

    @Override
    protected DataObjectInstantiator getDataObjectInstantiator() {
        return ProxyDataObjectInstantiator.INSTANCE;
    }
}
