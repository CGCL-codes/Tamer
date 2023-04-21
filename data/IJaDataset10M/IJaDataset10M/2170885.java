package org.itsocial.test.spring3;

import java.util.HashMap;
import java.util.List;
import org.itsocial.framework.audit.AuditLog;

/**
 *
 * @author mookiah
 */
class TestServiceImpl implements TestService {

    public TestServiceImpl() {
    }

    @Override
    public void setValue(String str) {
        System.out.println("Hello World Spring3" + str);
    }

    @AuditLog
    @Override
    public String getValue() {
        System.out.println("Hello World Spring3 Not supported yet");
        return "great";
    }

    @Override
    public void checkQueryResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
