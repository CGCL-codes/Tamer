package net.sourceforge.domian.repository.xstream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.apache.commons.lang.NotImplementedException;
import net.sourceforge.domian.entity.Entity;
import net.sourceforge.domian.repository.AbstractRepository;
import net.sourceforge.domian.repository.AbstractRepositoryTestCase;
import net.sourceforge.domian.repository.BinaryFormatRepository;
import net.sourceforge.domian.repository.ConcurrentHashSetRepository;
import net.sourceforge.domian.repository.HumanReadableFormatRepository;
import net.sourceforge.domian.repository.PartitionRepository;
import net.sourceforge.domian.repository.PersistenceDefinition;
import net.sourceforge.domian.repository.PersistentRepository;
import net.sourceforge.domian.repository.Repository;
import net.sourceforge.domian.repository.TextualFormatRepository;
import net.sourceforge.domian.repository.VolatileRepository;
import net.sourceforge.domian.specification.Specification;
import static net.sourceforge.domian.specification.SpecificationFactory.allEntities;
import net.sourceforge.domian.test.domain.Customer;
import net.sourceforge.domian.test.domain.OrderLine;
import static net.sourceforge.domian.test.domain.Testdata.maleCustomer;
import static net.sourceforge.domian.test.domain.Testdata.order22;
import net.sourceforge.domian.test.domain.VipCustomer;

public class PartitionRepositoryWithMultipleImplementationsTest extends AbstractRepositoryTestCase {

    protected <T extends Entity> Repository<T> getRepository() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    protected <T extends Entity> PartitionRepository<T> getPartitionRepository(Class<T> entityType, String repositoryId) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    protected <T extends Entity> PersistentRepository<T> getPersistentRepository(String repositoryId, PersistenceDefinition persistenceDefinition) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    protected <T extends Entity> Repository<T> getRepository(Class<T> entityType, String repositoryId) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    protected <T extends Entity> PartitionRepository<T> getPartitionRepository() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    protected <R extends Repository<? extends Entity>> void reset(R repository, int maxNumberOfEntitiesToPurge) {
        if (repository instanceof PartitionRepository) {
            Map<Specification<? extends Entity>, PartitionRepository> partitionMap = new HashMap<Specification<? extends Entity>, PartitionRepository>();
            ((PartitionRepository) repository).collectAllPartitions(partitionMap);
            for (PartitionRepository partitionRepository : partitionMap.values()) {
                if (partitionRepository instanceof PersistentRepository) {
                    Repository targetRepo = partitionRepository.getTargetRepository();
                    if (targetRepo instanceof XStreamXmlFileRepository) {
                        purgeDir(((XStreamXmlFileRepository) targetRepo).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
                    } else if (targetRepo instanceof XStreamXmlFileInMemoryPartitionedRepository) {
                        purgeDir(((XStreamXmlFileInMemoryPartitionedRepository) targetRepo).getRepositoryDirectory(), log, 2);
                    } else {
                        throw new IllegalStateException("Something is very wrong here...");
                    }
                }
            }
            Repository targetRepo = ((PartitionRepository) repository).getTargetRepository();
            if (targetRepo instanceof XStreamXmlFileRepository) {
                purgeDir(((XStreamXmlFileRepository) targetRepo).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
            } else if (targetRepo instanceof XStreamXmlFileInMemoryPartitionedRepository) {
                purgeDir(((XStreamXmlFileInMemoryPartitionedRepository) targetRepo).getRepositoryDirectory(), log, 2);
            }
        } else if (repository instanceof PersistentRepository) {
            if (repository instanceof XStreamXmlFileRepository) {
                purgeDir(((XStreamXmlFileRepository) repository).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
            } else if (repository instanceof XStreamXmlFileInMemoryPartitionedRepository) {
                purgeDir(((XStreamXmlFileInMemoryPartitionedRepository) repository).getRepositoryDirectory(), log, 2);
            } else {
                throw new IllegalStateException("Something is very wrong here...");
            }
        } else {
            throw new NotImplementedException();
        }
    }

    protected List<PersistenceDefinition> getSupportedPersistenceDefinitions() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_METHOD_MESSAGE);
    }

    @Test
    public void repositoryPartitionShouldInheritAllRepositoryInterfacesFromTargetRepositoryClass() {
        Repository<Entity> repo = new ConcurrentHashSetRepository<Entity>();
        assertTrue(repo instanceof Repository);
        assertTrue(repo instanceof VolatileRepository);
        assertFalse(repo instanceof PartitionRepository);
        assertFalse(repo instanceof PersistentRepository);
        assertFalse(repo instanceof TextualFormatRepository);
        assertFalse(repo instanceof HumanReadableFormatRepository);
        assertFalse(repo instanceof BinaryFormatRepository);
        repo = ((AbstractRepository<Entity>) repo).makePartition();
        assertTrue(repo instanceof Repository);
        assertTrue(repo instanceof VolatileRepository);
        assertTrue(repo instanceof PartitionRepository);
        assertFalse(repo instanceof PersistentRepository);
        assertFalse(repo instanceof TextualFormatRepository);
        assertFalse(repo instanceof HumanReadableFormatRepository);
        assertFalse(repo instanceof BinaryFormatRepository);
        repo = new XStreamXmlFileRepository<Entity>(DEFAULT_TEST_REPOSITORY_NAME);
        try {
            assertTrue(repo instanceof Repository);
            assertFalse(repo instanceof VolatileRepository);
            assertFalse(repo instanceof PartitionRepository);
            assertTrue(repo instanceof PersistentRepository);
            assertTrue(repo instanceof TextualFormatRepository);
            assertTrue(repo instanceof HumanReadableFormatRepository);
            assertFalse(repo instanceof BinaryFormatRepository);
            repo = ((AbstractRepository<Entity>) repo).makePartition();
            assertTrue(repo instanceof Repository);
            assertFalse(repo instanceof VolatileRepository);
            assertTrue(repo instanceof PartitionRepository);
            assertTrue(repo instanceof PersistentRepository);
            assertTrue(repo instanceof TextualFormatRepository);
            assertTrue(repo instanceof HumanReadableFormatRepository);
            assertFalse(repo instanceof BinaryFormatRepository);
        } finally {
            reset(repo);
        }
    }

    public void repositoryPartitionShouldInheritRepositoryClassFromTargetRepositoryClass() {
        Repository<Entity> repo = new ConcurrentHashSetRepository<Entity>();
        try {
            repo = ((AbstractRepository<Entity>) repo).makePartition();
            Repository<VipCustomer> vipCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(vipCustomers, new ConcurrentHashSetRepository<VipCustomer>());
            assertTrue(vipCustomerPartitionRepository instanceof ConcurrentHashSetRepository);
            assertTrue(repo instanceof ConcurrentHashSetRepository);
            Repository<VipCustomer> femaleVipCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(femaleVipCustomers, new XStreamXmlFileRepository<VipCustomer>("female-vip-customers"));
            assertTrue(femaleVipCustomerPartitionRepository instanceof XStreamXmlFileRepository);
            assertTrue(vipCustomerPartitionRepository instanceof ConcurrentHashSetRepository);
            assertTrue(repo instanceof ConcurrentHashSetRepository);
        } finally {
            reset(repo);
        }
    }

    public void repositoryPartitionShouldInheritAllRepositoryInterfacesForEachSubPartitionAdded() {
        Repository<Entity> repo = new ConcurrentHashSetRepository<Entity>();
        try {
            repo = ((AbstractRepository<Entity>) repo).makePartition();
            Repository<VipCustomer> vipCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(vipCustomers, new ConcurrentHashSetRepository<VipCustomer>());
            assertTrue(vipCustomerPartitionRepository instanceof Repository);
            assertTrue(vipCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(vipCustomerPartitionRepository instanceof PartitionRepository);
            assertFalse(vipCustomerPartitionRepository instanceof PersistentRepository);
            assertFalse(vipCustomerPartitionRepository instanceof TextualFormatRepository);
            assertFalse(vipCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(vipCustomerPartitionRepository instanceof BinaryFormatRepository);
            assertTrue(repo instanceof Repository);
            assertTrue(repo instanceof VolatileRepository);
            assertTrue(repo instanceof PartitionRepository);
            assertFalse(repo instanceof PersistentRepository);
            assertFalse(repo instanceof TextualFormatRepository);
            assertFalse(repo instanceof HumanReadableFormatRepository);
            assertFalse(repo instanceof BinaryFormatRepository);
            Repository<VipCustomer> femaleVipCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(femaleVipCustomers, new XStreamXmlFileRepository<VipCustomer>("female-vip-customers"));
            assertTrue(femaleVipCustomerPartitionRepository instanceof Repository);
            assertFalse(femaleVipCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(femaleVipCustomerPartitionRepository instanceof PartitionRepository);
            assertTrue(femaleVipCustomerPartitionRepository instanceof PersistentRepository);
            assertTrue(femaleVipCustomerPartitionRepository instanceof TextualFormatRepository);
            assertTrue(femaleVipCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(femaleVipCustomerPartitionRepository instanceof BinaryFormatRepository);
            assertTrue(vipCustomerPartitionRepository instanceof Repository);
            assertTrue(vipCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(vipCustomerPartitionRepository instanceof PartitionRepository);
            assertTrue(vipCustomerPartitionRepository instanceof PersistentRepository);
            assertTrue(vipCustomerPartitionRepository instanceof TextualFormatRepository);
            assertTrue(vipCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(vipCustomerPartitionRepository instanceof BinaryFormatRepository);
            assertTrue(repo instanceof Repository);
            assertTrue(repo instanceof VolatileRepository);
            assertTrue(repo instanceof PartitionRepository);
            assertTrue(repo instanceof PersistentRepository);
            assertTrue(repo instanceof TextualFormatRepository);
            assertTrue(repo instanceof HumanReadableFormatRepository);
            assertFalse(repo instanceof BinaryFormatRepository);
        } finally {
            reset(repo);
        }
    }

    public void repositoryPartitionShouldInheritAllRepositoryInterfacesForEachSubPartitionAddedForAllPossibleGraphTraversalsUpwards() {
        Repository<Entity> repo = new ConcurrentHashSetRepository<Entity>();
        try {
            repo = ((AbstractRepository<Entity>) repo).makePartition();
            Repository<Customer> pioneerCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(pioneerCustomers, new ConcurrentHashSetRepository<Customer>());
            assertTrue(pioneerCustomerPartitionRepository instanceof Repository);
            assertTrue(pioneerCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof PartitionRepository);
            assertFalse(pioneerCustomerPartitionRepository instanceof PersistentRepository);
            assertFalse(pioneerCustomerPartitionRepository instanceof TextualFormatRepository);
            assertFalse(pioneerCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(pioneerCustomerPartitionRepository instanceof BinaryFormatRepository);
            Repository<Customer> adultCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(adultCustomers, new ConcurrentHashSetRepository<Customer>());
            assertTrue(adultCustomerPartitionRepository instanceof Repository);
            assertTrue(adultCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(adultCustomerPartitionRepository instanceof PartitionRepository);
            assertFalse(adultCustomerPartitionRepository instanceof PersistentRepository);
            assertFalse(adultCustomerPartitionRepository instanceof TextualFormatRepository);
            assertFalse(adultCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(adultCustomerPartitionRepository instanceof BinaryFormatRepository);
            Repository<Customer> adultPioneerCustomerPartitionRepository = ((PartitionRepository<Entity>) repo).addPartitionFor(adultPioneerCustomers, new XStreamXmlFileInMemoryPartitionedRepository<Customer>("adult-pioneer-customers"));
            assertTrue(adultPioneerCustomerPartitionRepository instanceof Repository);
            assertFalse(adultPioneerCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(adultPioneerCustomerPartitionRepository instanceof PartitionRepository);
            assertTrue(adultPioneerCustomerPartitionRepository instanceof PersistentRepository);
            assertTrue(adultPioneerCustomerPartitionRepository instanceof TextualFormatRepository);
            assertTrue(adultPioneerCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(adultPioneerCustomerPartitionRepository instanceof BinaryFormatRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof Repository);
            assertTrue(pioneerCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof PartitionRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof PersistentRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof TextualFormatRepository);
            assertTrue(pioneerCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(pioneerCustomerPartitionRepository instanceof BinaryFormatRepository);
            assertTrue(adultCustomerPartitionRepository instanceof Repository);
            assertTrue(adultCustomerPartitionRepository instanceof VolatileRepository);
            assertTrue(adultCustomerPartitionRepository instanceof PartitionRepository);
            assertTrue(adultCustomerPartitionRepository instanceof PersistentRepository);
            assertTrue(adultCustomerPartitionRepository instanceof TextualFormatRepository);
            assertTrue(adultCustomerPartitionRepository instanceof HumanReadableFormatRepository);
            assertFalse(adultCustomerPartitionRepository instanceof BinaryFormatRepository);
        } finally {
            reset(repo);
        }
    }

    public void shouldManagePartitionRepositoriesConsistingOfMultipleRepositoryImplementations() {
        PartitionRepository<Entity> repo = new ConcurrentHashSetRepository<Entity>().makePartition();
        try {
            repo.addPartitionFor(vipCustomers, new XStreamXmlFileInMemoryPartitionedRepository<VipCustomer>("vip-customers").makePartition());
            repo.put(maleCustomer);
            repo.put(femaleVipCustomer);
            repo.put(order22);
            repo.put(new OrderLine());
            assertEquals((Long) 4L, repo.count(allEntities()));
            ((PersistentRepository) repo).persist();
        } finally {
            reset(repo);
        }
    }
}
