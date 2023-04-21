package br.com.mystudies.mock.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class ServiceImplTest extends MockObjectTestCase {

    private ServiceImpl serviceImplTest;

    protected void setUp() throws Exception {
        serviceImplTest = new ServiceImpl();
    }

    protected void tearDown() throws Exception {
        serviceImplTest = null;
    }

    public void testOperation() {
        Mock daoMock = mock(Dao.class);
        daoMock.expects(once()).method("find").with(isA(String.class)).will(returnValue("String"));
        serviceImplTest.setDao((Dao) daoMock.proxy());
        assertNotNull(serviceImplTest.operation("String"));
    }
}
