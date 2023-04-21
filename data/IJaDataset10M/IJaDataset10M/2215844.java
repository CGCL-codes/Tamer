package org.openuss.lecture;

import org.apache.log4j.Logger;
import org.openuss.TestUtility;
import org.openuss.aop.DepartmentIndexingAspect;
import org.openuss.foundation.DomainObject;
import org.openuss.search.IndexerApplicationException;
import org.openuss.search.IndexerService;
import org.openuss.security.User;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Test case for the spring aspect initiating the create, update and delete 
 * process of the department indexing.
 * 
 * @author Kai Stettner
 */
public class DepartmentIndexingAspectTest extends AbstractTransactionalDataSourceSpringContextTests {

    private static final Logger logger = Logger.getLogger(DepartmentIndexingAspectTest.class);

    private DepartmentService departmentService;

    private DepartmentDao departmentDao;

    private DepartmentIndexingAspect departmentIndexAspectBean;

    private IndexerServiceMock indexerMock;

    private TestUtility testUtility;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        indexerMock = new IndexerServiceMock();
        departmentIndexAspectBean.setIndexerService(indexerMock);
    }

    public void testLectureIndex() throws Exception {
        University university = testUtility.createUniqueUniversityInDB();
        User owner = testUtility.createUniqueUserInDB();
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setName("Wirtschaftswissenschaften - FB 4");
        departmentInfo.setDescription("Testdescription");
        departmentInfo.setShortcut("FB4");
        departmentInfo.setOwnerName("Administrator");
        departmentInfo.setEnabled(true);
        departmentInfo.setUniversityId(university.getId());
        departmentInfo.setDefaultDepartment(false);
        departmentInfo.setDepartmentType(DepartmentType.OFFICIAL);
        Long departmentId = departmentService.create(departmentInfo, owner.getId());
        departmentInfo.setId(departmentId);
        departmentService.update(departmentInfo);
        assertEquals(1, indexerMock.create);
        assertEquals(1, indexerMock.update);
        testUtility.createAdminSecureContext();
        departmentService.removeCompleteDepartmentTree(departmentId);
        assertEquals(1, indexerMock.delete);
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-lucene.xml", "classpath*:applicationContext-cache.xml", "classpath*:applicationContext-messaging.xml", "classpath*:applicationContext-resources.xml", "classpath*:applicationContext-aop.xml", "classpath*:applicationContext-events.xml", "classpath*:testContext.xml", "classpath*:testSecurity.xml", "classpath*:testDataSource.xml" };
    }

    public DepartmentIndexingAspect getDepartmentIndexAspectBean() {
        return departmentIndexAspectBean;
    }

    public void setDepartmentIndexAspectBean(DepartmentIndexingAspect departmentIndexAspectBean) {
        this.departmentIndexAspectBean = departmentIndexAspectBean;
    }

    private static class IndexerServiceMock implements IndexerService {

        private int create;

        private int delete;

        private int update;

        private int recreate;

        public void createIndex(DomainObject domainObject) throws IndexerApplicationException {
            logger.debug("method createIndex: Increment testCreateIndex");
            create++;
        }

        public void deleteIndex(DomainObject domainObject) throws IndexerApplicationException {
            logger.debug("method deleteIndex: Increment testDeleteIndex");
            delete++;
        }

        public void updateIndex(DomainObject domainObject) throws IndexerApplicationException {
            logger.debug("method updateIndex: Increment testUpdateIndex");
            update++;
        }

        public void recreate() throws IndexerApplicationException {
            logger.debug("method recreateIndex: Increment testRecreateIndex");
            recreate++;
        }
    }

    public TestUtility getTestUtility() {
        return testUtility;
    }

    public void setTestUtility(TestUtility testUtility) {
        this.testUtility = testUtility;
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public DepartmentDao getDepartmentDao() {
        return departmentDao;
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
}
