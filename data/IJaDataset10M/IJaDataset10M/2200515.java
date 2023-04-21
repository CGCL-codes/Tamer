package net.sf.sail.webapp.dao.offering.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.sail.webapp.dao.AbstractTransactionalDaoTests;
import net.sf.sail.webapp.domain.Offering;
import net.sf.sail.webapp.domain.Workgroup;
import net.sf.sail.webapp.domain.impl.OfferingImpl;
import net.sf.sail.webapp.domain.impl.WorkgroupImpl;
import net.sf.sail.webapp.domain.sds.SdsCurnit;
import net.sf.sail.webapp.domain.sds.SdsJnlp;
import net.sf.sail.webapp.domain.sds.SdsOffering;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Test for HibernateOfferingDao
 * 
 * @author Hiroki Terashima
 * @version $Id: HibernateOfferingDaoTest.java 1251 2007-09-30 21:43:46Z hiroki $
 */
public class HibernateOfferingDaoTest extends AbstractTransactionalDaoTests<HibernateOfferingDao, Offering> {

    private static final Long SDS_ID = new Long(7);

    private static final String DEFAULT_NAME = "Airbags";

    private static final String DEFAULT_URL = "http://mrpotatoiscoolerthanwoody.com";

    private static final SdsCurnit DEFAULT_SDS_CURNIT = new SdsCurnit();

    private static final SdsJnlp DEFAULT_SDS_JNLP = new SdsJnlp();

    private SdsOffering sdsOffering;

    private Workgroup workgroup;

    /**
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onTearDownAfterTransaction()
	 */
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        this.sdsOffering = null;
        this.workgroup = null;
    }

    /**
	 * @see net.sf.sail.webapp.junit.AbstractTransactionalDbTests#onSetUpBeforeTransaction()
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        this.dao = (HibernateOfferingDao) this.applicationContext.getBean("offeringDao");
        this.dataObject = (OfferingImpl) this.applicationContext.getBean("offering");
        DEFAULT_SDS_CURNIT.setSdsObjectId(SDS_ID);
        DEFAULT_SDS_CURNIT.setName(DEFAULT_NAME);
        DEFAULT_SDS_CURNIT.setUrl(DEFAULT_URL);
        DEFAULT_SDS_JNLP.setSdsObjectId(SDS_ID);
        DEFAULT_SDS_JNLP.setName(DEFAULT_NAME);
        DEFAULT_SDS_JNLP.setUrl(DEFAULT_URL);
        this.sdsOffering.setSdsObjectId(SDS_ID);
        this.sdsOffering.setName(DEFAULT_NAME);
        this.dataObject.setSdsOffering(this.sdsOffering);
        this.workgroup = (WorkgroupImpl) this.applicationContext.getBean("workgroup");
        this.workgroup.setOffering(this.dataObject);
    }

    /**
	 * @see org.springframework.test.AbstractTransactionalSpringContextTests#onSetUpInTransaction()
	 */
    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        Session session = this.sessionFactory.getCurrentSession();
        session.save(DEFAULT_SDS_CURNIT);
        session.save(DEFAULT_SDS_JNLP);
        this.sdsOffering.setSdsCurnit(DEFAULT_SDS_CURNIT);
        this.sdsOffering.setSdsJnlp(DEFAULT_SDS_JNLP);
    }

    public void testSave_NonExistentCurnit() {
        this.sdsOffering.setSdsJnlp(DEFAULT_SDS_JNLP);
        SdsCurnit nonExistentSdsCurnit = (SdsCurnit) this.applicationContext.getBean("sdsCurnit");
        nonExistentSdsCurnit.setName(DEFAULT_NAME);
        nonExistentSdsCurnit.setSdsObjectId(SDS_ID);
        nonExistentSdsCurnit.setUrl(DEFAULT_URL);
        this.sdsOffering.setSdsCurnit(nonExistentSdsCurnit);
        this.dataObject.setSdsOffering(sdsOffering);
        try {
            this.dao.save(this.dataObject);
            fail("DataIntegrityViolationException expected");
        } catch (DataIntegrityViolationException expected) {
        }
    }

    public void testSave_NonExistentJnlp() {
        this.sdsOffering.setSdsCurnit(DEFAULT_SDS_CURNIT);
        SdsJnlp nonExistentSdsJnlp = (SdsJnlp) this.applicationContext.getBean("sdsJnlp");
        nonExistentSdsJnlp.setName(DEFAULT_NAME);
        nonExistentSdsJnlp.setSdsObjectId(SDS_ID);
        nonExistentSdsJnlp.setUrl(DEFAULT_URL);
        this.sdsOffering.setSdsJnlp(nonExistentSdsJnlp);
        this.dataObject.setSdsOffering(sdsOffering);
        try {
            this.dao.save(this.dataObject);
            fail("DataIntegrityViolationException expected");
        } catch (DataIntegrityViolationException expected) {
        }
    }

    public void testSave() {
        verifyDataStoreIsEmpty();
        this.dao.save(this.dataObject);
        List<?> actualList = retrieveOfferingListFromDb();
        assertEquals(1, actualList.size());
        Map<?, ?> actualOfferingMap = (Map<?, ?>) actualList.get(0);
        assertEquals(SDS_ID, actualOfferingMap.get(SdsOffering.COLUMN_NAME_OFFERING_ID.toUpperCase()));
        assertEquals(DEFAULT_NAME, actualOfferingMap.get(SdsOffering.COLUMN_NAME_OFFERING_NAME.toUpperCase()));
    }

    public void testGetWorkgroupsForOffering() {
        verifyDataStoreIsEmpty();
        this.dao.save(this.dataObject);
        Set<Workgroup> workgroups = this.dao.getWorkgroupsForOffering(this.dataObject.getId());
        assertNotNull(workgroups);
    }

    private static final String RETRIEVE_OFFERING_LIST_SQL = "SELECT * FROM " + OfferingImpl.DATA_STORE_NAME + ", " + SdsOffering.DATA_STORE_NAME + " WHERE " + OfferingImpl.DATA_STORE_NAME + "." + OfferingImpl.COLUMN_NAME_SDS_OFFERING_FK + " = " + SdsOffering.DATA_STORE_NAME + ".id";

    private List<?> retrieveOfferingListFromDb() {
        return this.jdbcTemplate.queryForList(RETRIEVE_OFFERING_LIST_SQL, (Object[]) null);
    }

    @Override
    protected List<?> retrieveDataObjectListFromDb() {
        return this.jdbcTemplate.queryForList("SELECT * FROM " + OfferingImpl.DATA_STORE_NAME, (Object[]) null);
    }

    /**
	 * @param sdsOffering the sdsOffering to set
	 */
    public void setSdsOffering(SdsOffering sdsOffering) {
        this.sdsOffering = sdsOffering;
    }
}
