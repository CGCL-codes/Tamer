package org.cilogon.d2.storage.impl.memory;

import org.cilogon.d2.ServiceTestBase;
import org.cilogon.d2.TestStoreProvider;
import org.cilogon.d2.storage.impl.IdentityProviderTest;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 3/14/12 at  11:04 AM
 */
public class MemoryIDPTest extends IdentityProviderTest {

    @Override
    public TestStoreProvider getTSProvider() {
        return ServiceTestBase.getMemoryStoreProvider();
    }
}
