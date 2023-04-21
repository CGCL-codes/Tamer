package net.sourceforge.domian.repository.xstream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.apache.commons.lang.NotImplementedException;
import net.sourceforge.domian.entity.Entity;
import net.sourceforge.domian.repository.AbstractPartitionedRepositoryTest;
import net.sourceforge.domian.repository.PartitionRepository;
import net.sourceforge.domian.repository.PersistenceDefinition;
import net.sourceforge.domian.repository.PersistentRepository;
import net.sourceforge.domian.repository.Repository;
import net.sourceforge.domian.specification.Specification;
import static net.sourceforge.domian.util.InstrumentationUtils.REDEFINED_ELSEWHERE;
import static net.sourceforge.domian.util.InstrumentationUtils.SKIPPED;
import static net.sourceforge.domian.util.InstrumentationUtils.buildTestingOfMethodString;

public class XStreamXmlFileRepository_PartitionedRepositoryTest extends AbstractPartitionedRepositoryTest {

    protected <T extends Entity> Repository<T> getRepository() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    protected <T extends Entity> Repository<T> getRepository(Class<T> entityType, String repositoryId) {
        return new XStreamXmlFileRepository<T>(repositoryId);
    }

    protected <T extends Entity> PersistentRepository<T> getPersistentRepository(String repositoryId, PersistenceDefinition persistenceDefinition) {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    protected <T extends Entity> PartitionRepository<T> getPartitionRepository() {
        return new XStreamXmlFileRepository<T>(DEFAULT_TEST_REPOSITORY_NAME).makePartition();
    }

    protected <T extends Entity> PartitionRepository<T> getPartitionRepository(Class<T> entityType, String repositoryId) {
        return new XStreamXmlFileRepository<T>(repositoryId).makePartition();
    }

    protected <R extends Repository<? extends Entity>> void reset(R repository, int maxNumberOfEntitiesToPurge) {
        if (repository instanceof PartitionRepository) {
            Map<Specification<? extends Entity>, PartitionRepository> partitionMap = new HashMap<Specification<? extends Entity>, PartitionRepository>();
            ((PartitionRepository) repository).collectAllPartitions(partitionMap);
            for (PartitionRepository partitionRepository : partitionMap.values()) {
                if (partitionRepository instanceof PersistentRepository) {
                    purgeDir(((PersistentRepository) partitionRepository).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
                }
            }
            if (repository instanceof PersistentRepository) {
                purgeDir(((PersistentRepository) repository).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
            }
        } else if (repository instanceof PersistentRepository) {
            purgeDir(((PersistentRepository) repository).getRepositoryDirectory(), log, maxNumberOfEntitiesToPurge);
        } else {
            throw new NotImplementedException();
        }
    }

    protected List<PersistenceDefinition> getSupportedPersistenceDefinitions() {
        throw new UnsupportedOperationException(NOT_APPLICABLE_METHOD_MESSAGE);
    }

    /** Redefined in {@link net.sourceforge.domian.repository.AbstractPersistentRepositoryTest_GranularityEntity_TriggerPutFindRemove} */
    @Override
    @Test
    public void shouldRepartitionEntities() {
        log.info(buildTestingOfMethodString("shouldRepartitionEntities", REDEFINED_ELSEWHERE));
    }

    /** Redefined in {@link net.sourceforge.domian.repository.AbstractPersistentRepositoryTest_GranularityEntity_TriggerPutFindRemove} */
    @Override
    @Test
    public void shouldRepartitionEntireRepository() {
        log.info(buildTestingOfMethodString("shouldRepartitionEntireRepository", REDEFINED_ELSEWHERE));
    }

    /** Not particular interesting for {@link net.sourceforge.domian.repository.AbstractPersistentRepositoryTest_GranularityEntity_TriggerPutFindRemove} repositories. */
    @Override
    @Test
    public void shouldHandleLargeNumbersOfIteratorsWithoutAnyOutOfMemoryExceptions() {
        log.info(buildTestingOfMethodString("shouldHandleLargeNumbersOfIteratorsWithoutAnyOutOfMemoryExceptions", SKIPPED));
    }
}
