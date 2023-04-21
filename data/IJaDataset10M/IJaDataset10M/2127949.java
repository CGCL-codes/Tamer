package net.sourceforge.domian.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;
import org.junit.Test;
import org.apache.commons.lang.NotImplementedException;
import net.sourceforge.domian.entity.Entity;
import net.sourceforge.domian.specification.Specification;
import net.sourceforge.domian.test.domain.Customer;
import net.sourceforge.domian.test.domain.Order;
import net.sourceforge.domian.test.domain.OrderLine;
import net.sourceforge.domian.util.concurrent.task.ConcurrentTaskExecutor;
import net.sourceforge.domian.util.concurrent.task.RepositoryTask;
import static java.util.Arrays.asList;
import static net.sourceforge.domian.predicate.SpecificationPredicate.getPredicate;
import static net.sourceforge.domian.repository.AbstractDomianCoreRepository.DEFAULT_DOMIAN_ROOT_DIR_NAME;
import static net.sourceforge.domian.repository.InMemoryAndXStreamXmlFileRepository.DEFAULT_REPOSITORY_ROOT_DIR_NAME;
import static net.sourceforge.domian.repository.InMemoryAndXStreamXmlFileRepository.DEFAULT_REPOSITORY_ROOT_FILE_NAME;
import static net.sourceforge.domian.specification.SpecificationFactory.allInstancesOfType;
import static net.sourceforge.domian.specification.SpecificationFactory.allOfType;
import static net.sourceforge.domian.specification.SpecificationFactory.matchesWildcardExpression;
import static net.sourceforge.domian.specification.SpecificationUtils.createUniqueSpecificationFor;
import static net.sourceforge.domian.test.domain.Customer.Gender.FEMALE;
import static net.sourceforge.domian.test.domain.Testdata.childFemaleCustomer;
import static net.sourceforge.domian.test.domain.Testdata.createCustomer;
import static net.sourceforge.domian.test.domain.Testdata.createOrder;
import static net.sourceforge.domian.test.domain.Testdata.createOrderLine;
import static net.sourceforge.domian.test.domain.Testdata.femaleCustomer;
import static net.sourceforge.domian.test.domain.Testdata.maleCustomer;
import static net.sourceforge.domian.test.domain.Testdata.today;
import static net.sourceforge.domian.util.DateUtils.getTime;
import static net.sourceforge.domian.util.FileUtils.rmdir;
import static net.sourceforge.domian.util.InstrumentationUtils.buildTestingOfMethodString;
import static org.apache.commons.collections.CollectionUtils.select;
import static org.apache.commons.lang.SystemUtils.FILE_SEPARATOR;
import static org.apache.commons.lang.SystemUtils.USER_DIR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InMemoryAndXStreamXmlFileRepository_PersistentRepositoryTest extends AbstractPersistentRepositoryTest {

    @Override
    protected String getTestRepoRootPath() {
        return InMemoryAndXStreamXmlFileRepository.DEFAULT_REPOSITORY_ROOT_PATH;
    }

    @Override
    protected <T extends Entity> Repository<T> getRepository() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> Repository<T> getRepository(Class<T> entityType, String repositoryId) {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> PersistentRepository<T> getPersistentRepository(String repositoryId, PersistenceDefinition persistenceDefinition) {
        return new InMemoryAndXStreamXmlFileRepository<T>(getTestRepoRootPath(), repositoryId);
    }

    protected <T extends Entity> PersistentRepository<T> getPersistentRepository(String repositoryRootPath, String repositoryId, PersistenceDefinition persistenceDefinition) {
        return new InMemoryAndXStreamXmlFileRepository<T>(repositoryRootPath, repositoryId);
    }

    @Override
    protected <T extends Entity> PartitionRepository<T> getPartitionRepository() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> PartitionRepository<T> getPartitionRepository(Class<T> entityType, String repositoryId) {
        return new InMemoryAndXStreamXmlFileRepository<T>(getTestRepoRootPath(), repositoryId).makePartition();
    }

    @Override
    protected <R extends Repository<? extends Entity>> void reset(R repository, int maxNumberOfEntitiesToPurge) {
        if (repository instanceof PartitionRepository) {
            Map<Specification<? extends Entity>, PartitionRepository> partitionMap = new HashMap<Specification<? extends Entity>, PartitionRepository>();
            ((PartitionRepository<Entity>) repository).collectAllPartitions(partitionMap);
            for (PartitionRepository partitionRepository : partitionMap.values()) {
                if (partitionRepository instanceof PersistentRepository) {
                    File repoDir = ((PersistentRepository) partitionRepository).getRepositoryDirectory();
                    rmdir(repoDir, log, maxNumberOfEntitiesToPurge);
                }
            }
            if (repository instanceof PersistentRepository) {
                File repoDir = ((PersistentRepository) repository).getRepositoryDirectory();
                rmdir(repoDir, log, maxNumberOfEntitiesToPurge);
            }
        } else if (repository instanceof PersistentRepository) {
            File repoDir = ((PersistentRepository) repository).getRepositoryDirectory();
            rmdir(repoDir, log, maxNumberOfEntitiesToPurge);
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    protected List<PersistenceDefinition> getSupportedPersistenceDefinitions() {
        return asList(PersistenceDefinition.INMEMORY_AND_FILE);
    }

    @Override
    protected long countNumberOfFilesIn(final Repository repository) {
        if (repository instanceof PersistentRepository) {
            return ((PersistentRepository) repository).getRepositoryDirectory().listFiles().length;
        }
        throw new IllegalArgumentException("Not a persistent repository");
    }

    @Test
    public void shouldImplementSimplePaasPersistenceDefinition() {
        log.info(buildTestingOfMethodString("shouldImplementSimplePaasPersistenceDefinition"));
        assertSame(PersistenceDefinition.INMEMORY_AND_FILE, getPersistentRepository(DEFAULT_TEST_REPOSITORY_NAME, PersistenceDefinition.INMEMORY_AND_FILE).getPersistenceDefinition());
    }

    @Test
    public void checkRepositoryPathAndFilenames_defaultPaths() {
        log.info(buildTestingOfMethodString("checkRepositoryPathAndFilenames_defaultPaths"));
        PersistentRepository<Customer> repo = getPersistentRepository(DEFAULT_TEST_REPOSITORY_NAME, PersistenceDefinition.INMEMORY_AND_FILE);
        try {
            repo.put(maleCustomer);
            repo.put(femaleCustomer);
            assertEquals(0, countNumberOfFilesIn(repo));
            repo.persist();
            assertEquals(2, countNumberOfFilesIn(repo));
            Specification<File> entityRepoFilesSpec = allOfType(File.class).where("name", matchesWildcardExpression("*entities*"));
            Specification<File> metaDataRepoFilesSpec = allOfType(File.class).where("name", matchesWildcardExpression("*metadata*"));
            File[] persistenceUnits = repo.getRepositoryDirectory().listFiles();
            Collection<File> entityRepoFiles = select(asList(persistenceUnits), getPredicate(entityRepoFilesSpec));
            assertEquals(1, entityRepoFiles.size());
            File entityRepoFile = entityRepoFiles.iterator().next();
            StringTokenizer stringTokenizer = new StringTokenizer(entityRepoFile.getAbsolutePath(), FILE_SEPARATOR, false);
            List<String> absolutePathElements = new ArrayList<String>(stringTokenizer.countTokens());
            while (stringTokenizer.hasMoreElements()) {
                absolutePathElements.add((String) stringTokenizer.nextElement());
            }
            String domianFolderName = null;
            String repositoryTypeFolderName = null;
            String repositoryFolderName = null;
            String repositoryFilename = null;
            Iterator<String> absolutePathIterator = absolutePathElements.iterator();
            while (absolutePathIterator.hasNext()) {
                String pathElement = absolutePathIterator.next();
                if (pathElement.startsWith(".")) {
                    domianFolderName = pathElement;
                    repositoryTypeFolderName = absolutePathIterator.next();
                    repositoryFolderName = absolutePathIterator.next();
                    repositoryFilename = absolutePathIterator.next();
                    assertFalse(absolutePathIterator.hasNext());
                }
            }
            assertEquals(DEFAULT_DOMIAN_ROOT_DIR_NAME, domianFolderName);
            assertEquals(DEFAULT_REPOSITORY_ROOT_DIR_NAME, repositoryTypeFolderName);
            assertEquals(DEFAULT_TEST_REPOSITORY_NAME, repositoryFolderName);
            assertTrue(repositoryFilename != null);
            assertTrue(repositoryFilename.startsWith(DEFAULT_REPOSITORY_ROOT_FILE_NAME + "_" + DEFAULT_TEST_REPOSITORY_NAME + "_"));
            assertTrue(repositoryFilename.contains("entities"));
            assertTrue(repositoryFilename.endsWith(".xstream-1.3.1.xml"));
            Collection<File> metaDataRepoFiles = select(asList(persistenceUnits), getPredicate(metaDataRepoFilesSpec));
            assertEquals(1, metaDataRepoFiles.size());
            File metaDataRepoFile = metaDataRepoFiles.iterator().next();
            stringTokenizer = new StringTokenizer(metaDataRepoFile.getAbsolutePath(), FILE_SEPARATOR, false);
            absolutePathElements = new ArrayList<String>(stringTokenizer.countTokens());
            while (stringTokenizer.hasMoreElements()) {
                absolutePathElements.add((String) stringTokenizer.nextElement());
            }
            domianFolderName = null;
            repositoryTypeFolderName = null;
            repositoryFolderName = null;
            repositoryFilename = null;
            absolutePathIterator = absolutePathElements.iterator();
            while (absolutePathIterator.hasNext()) {
                String pathElement = absolutePathIterator.next();
                if (pathElement.startsWith(".")) {
                    domianFolderName = pathElement;
                    repositoryTypeFolderName = absolutePathIterator.next();
                    repositoryFolderName = absolutePathIterator.next();
                    repositoryFilename = absolutePathIterator.next();
                    assertFalse(absolutePathIterator.hasNext());
                }
            }
            assertEquals(DEFAULT_DOMIAN_ROOT_DIR_NAME, domianFolderName);
            assertEquals(DEFAULT_REPOSITORY_ROOT_DIR_NAME, repositoryTypeFolderName);
            assertEquals(DEFAULT_TEST_REPOSITORY_NAME, repositoryFolderName);
            assertTrue(repositoryFilename != null);
            assertTrue(repositoryFilename.startsWith(DEFAULT_REPOSITORY_ROOT_FILE_NAME + "_" + DEFAULT_TEST_REPOSITORY_NAME + "_"));
            assertTrue(repositoryFilename.contains("metadata"));
            assertTrue(repositoryFilename.endsWith(".xstream-1.3.1.xml"));
        } finally {
            reset(repo);
        }
    }

    @Test
    public void checkRepositoryPathAndFilenames_customPaths() {
        log.info(buildTestingOfMethodString("checkRepositoryPathAndFilenames_customPaths"));
        String defaultRepositoryRootDirName = ".domain";
        String repositoryRootPath = USER_DIR + FILE_SEPARATOR + "target" + FILE_SEPARATOR + defaultRepositoryRootDirName;
        String repositoryId = "test-repo";
        PersistentRepository<Customer> repo = getPersistentRepository(repositoryRootPath, repositoryId, PersistenceDefinition.INMEMORY_AND_FILE);
        try {
            repo.put(maleCustomer);
            repo.put(femaleCustomer);
            assertEquals(0, countNumberOfFilesIn(repo));
            repo.persist();
            assertEquals(2, countNumberOfFilesIn(repo));
            Specification<File> entityRepoFilesSpec = allOfType(File.class).where("name", matchesWildcardExpression("*entities*"));
            Specification<File> metaDataRepoFilesSpec = allOfType(File.class).where("name", matchesWildcardExpression("*metadata*"));
            File[] persistenceUnits = repo.getRepositoryDirectory().listFiles();
            Collection<File> entityRepoFiles = select(asList(persistenceUnits), getPredicate(entityRepoFilesSpec));
            assertEquals(1, entityRepoFiles.size());
            File entityRepoFile = entityRepoFiles.iterator().next();
            StringTokenizer stringTokenizer = new StringTokenizer(entityRepoFile.getAbsolutePath(), FILE_SEPARATOR, false);
            List<String> absolutePathElements = new ArrayList<String>(stringTokenizer.countTokens());
            while (stringTokenizer.hasMoreElements()) {
                absolutePathElements.add((String) stringTokenizer.nextElement());
            }
            String domianFolderName = null;
            String repositoryFolderName = null;
            String repositoryFilename = null;
            Iterator<String> absolutePathIterator = absolutePathElements.iterator();
            while (absolutePathIterator.hasNext()) {
                String pathElement = absolutePathIterator.next();
                if (pathElement.equals(defaultRepositoryRootDirName)) {
                    domianFolderName = pathElement;
                    repositoryFolderName = absolutePathIterator.next();
                    repositoryFilename = absolutePathIterator.next();
                    if (absolutePathIterator.hasNext()) {
                        fail("repositoryRootPath=" + repositoryRootPath + ", next=" + absolutePathIterator.next());
                    }
                }
            }
            assertEquals(defaultRepositoryRootDirName, domianFolderName);
            assertEquals(repositoryId, repositoryFolderName);
            assertTrue(repositoryFilename != null);
            assertTrue(repositoryFilename.startsWith(((InMemoryAndXStreamXmlFileRepository) repo).getRepositoryTypeName() + "_" + repositoryId + "_"));
            assertTrue(repositoryFilename.contains("entities"));
            assertTrue(repositoryFilename.endsWith(".xstream-1.3.1.xml"));
            Collection<File> metaDataRepoFiles = select(asList(persistenceUnits), getPredicate(metaDataRepoFilesSpec));
            assertEquals(1, metaDataRepoFiles.size());
            File metaDataRepoFile = metaDataRepoFiles.iterator().next();
            stringTokenizer = new StringTokenizer(metaDataRepoFile.getAbsolutePath(), FILE_SEPARATOR, false);
            absolutePathElements = new ArrayList<String>(stringTokenizer.countTokens());
            while (stringTokenizer.hasMoreElements()) {
                absolutePathElements.add((String) stringTokenizer.nextElement());
            }
            domianFolderName = null;
            repositoryFolderName = null;
            repositoryFilename = null;
            absolutePathIterator = absolutePathElements.iterator();
            while (absolutePathIterator.hasNext()) {
                String pathElement = absolutePathIterator.next();
                if (pathElement.equals(defaultRepositoryRootDirName)) {
                    domianFolderName = pathElement;
                    repositoryFolderName = absolutePathIterator.next();
                    repositoryFilename = absolutePathIterator.next();
                    assertFalse(absolutePathIterator.hasNext());
                }
            }
            assertEquals(defaultRepositoryRootDirName, domianFolderName);
            assertEquals(repositoryId, repositoryFolderName);
            assertTrue(repositoryFilename != null);
            assertTrue(repositoryFilename.startsWith(((InMemoryAndXStreamXmlFileRepository) repo).getRepositoryTypeName() + "_" + repositoryId + "_"));
            assertTrue(repositoryFilename.contains("metadata"));
            assertTrue(repositoryFilename.endsWith(".xstream-1.3.1.xml"));
        } finally {
            reset(repo);
        }
    }

    /** Tests concurrent adding of entities, subPartitions - and persisting of entities. */
    @Test
    public void multiTest() throws BrokenBarrierException, InterruptedException {
        PartitionRepository<Entity> repo = getPartitionRepository(Entity.class, DEFAULT_TEST_REPOSITORY_NAME);
        log.info(buildTestingOfMethodString("multiTest"));
        try {
            ConcurrentTaskExecutor<RepositoryTask<PartitionRepository<Entity>>> taskExecutor = new ConcurrentTaskExecutor<RepositoryTask<PartitionRepository<Entity>>>();
            taskExecutor.addTask(new ClientTask1(repo));
            taskExecutor.addTask(new ClientTask2(repo));
            taskExecutor.addTask(new ClientTask3(repo));
            taskExecutor.execute();
            Collection<Entity> entities = repo.find(allInstancesOfType(Entity.class));
            assertEquals(2400, entities.size());
            Collection<? extends Customer> customers = repo.find(allInstancesOfType(Customer.class));
            assertEquals(1100, customers.size());
            Collection<? extends Order> orders = repo.find(allInstancesOfType(Order.class));
            assertEquals(1100, orders.size());
            Collection<? extends OrderLine> orderLines = repo.find(allInstancesOfType(OrderLine.class));
            assertEquals(200, orderLines.size());
        } finally {
            reset(repo);
        }
    }

    class ClientTask1 extends RepositoryTask<PartitionRepository<Entity>> {

        public ClientTask1(PartitionRepository<Entity> repository) {
            super(repository);
        }

        @Override
        public void doConcurrentTask() {
            for (int i = 1; i <= 1000; ++i) {
                this.repository.put(createCustomer());
            }
            if (this.repository instanceof PersistentRepository) {
                ((PersistentRepository) this.repository).persist();
            } else {
                fail("Repository to test is not persistent - something is wrong here...");
            }
            this.repository.addPartitionFor(allInstancesOfType(Order.class), "orders");
            for (int i = 1; i <= 100; ++i) {
                this.repository.put(createCustomer());
                if (this.repository instanceof PersistentRepository) {
                    ((PersistentRepository) this.repository).persist();
                }
            }
        }
    }

    class ClientTask2 extends RepositoryTask<PartitionRepository<Entity>> {

        public ClientTask2(PartitionRepository<Entity> repository) {
            super(repository);
        }

        @Override
        public void doConcurrentTask() {
            for (int i = 1; i <= 1000; ++i) {
                this.repository.put(createOrder());
            }
            if (this.repository instanceof PersistentRepository) {
                ((PersistentRepository) this.repository).persist();
            }
            for (int i = 1; i <= 100; ++i) {
                this.repository.put(createOrder());
                this.repository.addPartitionFor(allInstancesOfType(OrderLine.class), "orderlines");
                this.repository.addPartitionFor(allInstancesOfType(Customer.class), "customers");
                if (this.repository instanceof PersistentRepository) {
                    ((PersistentRepository) this.repository).persist();
                }
            }
        }
    }

    class ClientTask3 extends RepositoryTask<PartitionRepository<Entity>> {

        public ClientTask3(PartitionRepository<Entity> repository) {
            super(repository);
        }

        @Override
        public void doConcurrentTask() {
            for (int i = 1; i <= 100; ++i) {
                this.repository.put(createOrderLine());
            }
            this.repository.addPartitionFor(allInstancesOfType(OrderLine.class), "orderlines");
            this.repository.addPartitionFor(allInstancesOfType(Customer.class), "customers");
            if (this.repository instanceof PersistentRepository) {
                ((PersistentRepository) this.repository).persist();
            }
            this.repository.addPartitionFor(allInstancesOfType(Order.class), "orders");
            for (int i = 1; i <= 100; ++i) {
                this.repository.put(createOrderLine());
                if (this.repository instanceof PersistentRepository) {
                    ((PersistentRepository) this.repository).persist();
                }
            }
        }
    }

    @Test
    public void shouldHaveItsOwnMetaDataFile() {
        log.info(buildTestingOfMethodString(this, "shouldHaveItsOwnMetaDataFile"));
        PersistentRepository<Entity> repo = getPersistentRepository(DEFAULT_TEST_REPOSITORY_NAME, PersistenceDefinition.INMEMORY_AND_FILE);
        try {
            System.out.println(repo.getRepositoryDirectory());
            assertEquals(0, countNumberOfFilesIn(repo));
            EntityMetaData entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNull(entityMetaData);
            assertEquals(0, countNumberOfFilesIn(repo));
            Customer childFemaleCustomer = new Customer(814L, today).name("Mari").gender(FEMALE).birthDate(getTime(2006, 4, 12));
            repo.put(childFemaleCustomer);
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNull(entityMetaData);
            repo.persist();
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 0L, entityMetaData.getNumberOfTimesRead());
            repo.put(childFemaleCustomer);
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 0L, entityMetaData.getNumberOfTimesRead());
            repo.load();
            childFemaleCustomer = repo.findSingleEntitySpecifiedBy(createUniqueSpecificationFor(childFemaleCustomer));
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesRead());
            childFemaleCustomer.setName("Anna");
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesRead());
            repo.load();
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 1L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 2L, entityMetaData.getNumberOfTimesRead());
            repo.persist();
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 2L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 2L, entityMetaData.getNumberOfTimesRead());
            repo.load();
            assertEquals(2, countNumberOfFilesIn(repo));
            entityMetaData = repo.getMetaDataFor(childFemaleCustomer);
            assertNotNull(entityMetaData);
            assertEquals((Long) 2L, entityMetaData.getNumberOfTimesWritten());
            assertEquals((Long) 3L, entityMetaData.getNumberOfTimesRead());
        } finally {
            reset(repo);
        }
    }
}
