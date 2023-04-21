package net.sourceforge.domian.repository;

import java.util.List;
import org.junit.Test;
import org.apache.commons.lang.NotImplementedException;
import net.sourceforge.domian.entity.Entity;

public class PartitionedRepositoryDelegator_InMemoryRepository_ConcurrentRepositoryTest extends AbstractConcurrentRepositoryTest {

    @Override
    protected String getTestRepoRootPath() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> Repository<T> getRepository() {
        return new InMemoryRepository<T>().makePartition();
    }

    @Override
    protected <T extends Entity> Repository<T> getRepository(Class<T> entityType, String repositoryId) {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> PersistentRepository<T> getPersistentRepository(String repositoryId, PersistenceDefinition persistenceDefinition) {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> PartitionRepository<T> getPartitionRepository() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <T extends Entity> PartitionRepository<T> getPartitionRepository(Class<T> entityType, String repositoryId) {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Override
    protected <R extends Repository<? extends Entity>> void reset(R repository, int maxNumberOfEntitiesToPurge) {
    }

    @Override
    protected List<PersistenceDefinition> getSupportedPersistenceDefinitions() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    @Test
    public void dummyTest() {
    }
}
