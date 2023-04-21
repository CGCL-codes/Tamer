package org.opentides.service;

import org.junit.Test;
import org.opentides.bean.SystemCodes;
import org.springframework.test.jpa.AbstractJpaTests;

public class ServiceTransactionTest extends AbstractJpaTests {

    private ServiceTransaction serviceTransaction;

    private SystemCodesService systemCodesService;

    public ServiceTransactionTest() {
        super();
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "file:WebContent/WEB-INF/tides/tidesContext*.xml", "file:WebContent/WEB-INF/tides/testConfig.xml" };
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        jdbcTemplate.execute("delete from SYSTEM_CODES where KEY_='TXTEST'");
    }

    @Test
    public void testRollback() {
        SystemCodes obj = new SystemCodes();
        obj.setKey("TXTEST");
        obj.setValue("Transaction Test");
        try {
            serviceTransaction.saveWithException(obj);
            fail("No exception thrown");
        } catch (Exception e) {
        }
        SystemCodes tx = systemCodesService.findByKey(obj);
        System.out.println(tx.getKey());
        assertNull(tx);
    }

    /**
	 * @param serviceTransaction the service to set
	 */
    public void setServiceTransaction(ServiceTransaction serviceTransaction) {
        this.serviceTransaction = serviceTransaction;
    }

    /**
	 * @param systemCodesService the systemCodesService to set
	 */
    public void setSystemCodesService(SystemCodesService systemCodesService) {
        this.systemCodesService = systemCodesService;
    }
}
