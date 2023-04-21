package net.sf.josceleton.connection.impl.service.user;

import com.google.inject.Module;
import net.sf.josceleton.commons.test.AbstractModuleTest;

public class ConnectionImplServiceUserModuleTest extends AbstractModuleTest {

    @Override
    protected final Module createTestee() {
        return new ConnectionImplServiceUserModule();
    }
}
