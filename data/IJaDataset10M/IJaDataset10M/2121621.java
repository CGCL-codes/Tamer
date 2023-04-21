package net.sf.sail.webapp.dao.authentication.impl;

import java.util.List;
import java.util.Map;
import org.springframework.security.GrantedAuthorityImpl;
import net.sf.sail.webapp.dao.AbstractTransactionalDaoTests;
import net.sf.sail.webapp.domain.authentication.MutableAclSid;
import net.sf.sail.webapp.domain.authentication.impl.PersistentAclSid;

/**
 * @author Cynick Young
 * 
 * @version $Id: HibernateAclSidDaoTest.java 2381 2009-05-16 21:07:50Z honchikun@gmail.com $
 */
public class HibernateAclSidDaoTest extends AbstractTransactionalDaoTests<HibernateAclSidDao, MutableAclSid> {

    private static final String SID_NAME = "Sid Vicious";

    /**
     * @see net.sf.sail.webapp.junit.AbstractTransactionalDbTests#onSetUpBeforeTransaction()
     */
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        this.dao = (HibernateAclSidDao) this.applicationContext.getBean("aclSidDao");
        this.dataObject = (MutableAclSid) this.applicationContext.getBean("mutableAclSid");
        this.dataObject.setGrantedAuthority(new GrantedAuthorityImpl(SID_NAME));
    }

    public void testRetrieveBySidName() {
        this.verifyDataStoreIsEmpty();
        this.dao.save(this.dataObject);
        MutableAclSid actual = this.dao.retrieveBySidName(SID_NAME);
        assertTrue(!actual.isPrincipal());
        assertEquals(SID_NAME, actual.getGrantedAuthority());
        try {
            actual.getPrincipal();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        assertEquals(this.dataObject, actual);
        assertNull(this.dao.retrieveBySidName("blah"));
    }

    /**
     * @see net.sf.sail.webapp.dao.AbstractTransactionalDaoTests#testSave()
     */
    @Override
    public void testSave() {
        this.verifyDataStoreIsEmpty();
        this.dao.save(this.dataObject);
        List<?> actualList = this.retrieveDataObjectListFromDb();
        assertEquals(1, actualList.size());
        Map<?, ?> actualValueMap = (Map<?, ?>) actualList.get(0);
        String actualValue = (String) actualValueMap.get(PersistentAclSid.COLUMN_NAME_SID.toUpperCase());
        assertEquals(SID_NAME, actualValue);
    }

    /**
     * @see net.sf.sail.webapp.dao.AbstractTransactionalDaoTests#retrieveDataObjectListFromDb()
     */
    @Override
    protected List<?> retrieveDataObjectListFromDb() {
        return this.jdbcTemplate.queryForList("SELECT * FROM " + PersistentAclSid.DATA_STORE_NAME, (Object[]) null);
    }
}
