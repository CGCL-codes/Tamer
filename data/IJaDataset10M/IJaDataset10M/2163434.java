package net.sourceforge.domian.repository.xstream;

import java.io.File;
import static java.lang.Boolean.FALSE;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import static org.apache.commons.lang.SystemUtils.FILE_SEPARATOR;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.NotImplementedException;
import net.sourceforge.domian.entity.Entity;
import net.sourceforge.domian.repository.EntityPersistenceMetaData;
import net.sourceforge.domian.repository.PersistenceDefinition;
import static net.sourceforge.domian.repository.PersistenceDefinition.EXPLICIT_PERSISTENCE_DEFINITION;
import net.sourceforge.domian.repository.PersistentEntity;
import net.sourceforge.domian.specification.Specification;
import net.sourceforge.domian.util.concurrent.locks.SemaphoreSynchronizer;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlSet;

public class XStreamXmlFileRepository<T extends Entity> extends AbstractXStreamXmlFilePerEntityRepository<T> {

    protected static final String DEFAULT_REPOSITORY_ROOT_DIR_NAME = ".xstream-xml-file-repository";

    protected static final String DEFAULT_REPOSITORY_ROOT_PATH = DEFAULT_DOMIAN_ROOT_PATH + FILE_SEPARATOR + DEFAULT_REPOSITORY_ROOT_DIR_NAME;

    public XStreamXmlFileRepository(final String repositoryId) {
        this(DEFAULT_REPOSITORY_ROOT_PATH, repositoryId);
    }

    public XStreamXmlFileRepository(final String repositoryRootPath, final String repositoryId) {
        Validate.notEmpty(repositoryRootPath, "The repository root path cannot be empty");
        Validate.notEmpty(repositoryId, "The repository ID cannot be empty");
        this.synchronizer = new SemaphoreSynchronizer();
        this.repositoryRootPath = repositoryRootPath;
        this.repositoryId = repositoryId;
        createRepositoryFilesIfNotExist();
    }

    /** @return the total number of entities residing in this particular repository (partition) */
    public Long countAllEntities() {
        return (long) new File(this.repositoryRootPath).list().length;
    }

    public <V extends T> Iterator<V> iterateAllEntitiesSpecifiedBy(final Specification<V> specification) {
        Validate.notNull(specification, "Specification parameter cannot be null");
        return runExclusivelyWithRetry(new IterateAllEntitiesSpecifiedBy<V>(specification));
    }

    public <V extends T> Collection<V> findAllEntitiesSpecifiedBy(final Specification<V> specification) {
        Validate.notNull(specification, "Specification parameter cannot be null");
        return runExclusivelyWithRetry(new FindAllEntitiesSpecifiedBy<V>(specification));
    }

    public <V extends T> void put(final V entity) {
        runExclusivelyWithRetry(new Put<V>(entity));
    }

    public <V extends T> Long removeAllEntitiesSpecifiedBy(final Specification<V> specification) {
        Validate.notNull(specification, "Specification parameter cannot be null");
        return runExclusivelyWithRetry(new RemoveAllEntitiesSpecifiedBy<V>(specification));
    }

    public <V extends T> Boolean remove(final V entity) {
        if (entity == null) {
            return FALSE;
        }
        return runExclusivelyWithRetry(new Remove<V>(entity));
    }

    public PersistenceDefinition getPersistenceDefinition() {
        return EXPLICIT_PERSISTENCE_DEFINITION;
    }

    public String getFormat() {
        return "XStream XML";
    }

    public void load() {
        log.warn("load() is not applicable for PersistenceTriggerDefinition.PUT_FIND_REMOVE repositories");
    }

    public void persist() {
        log.warn("persist() is not applicable for PersistenceTriggerDefinition.PUT_FIND_REMOVE repositories");
    }

    public EntityPersistenceMetaData getMetaDataFor(final T entity) {
        PersistentEntity<T> persistentEntity = null;
        final PersistenceStrategy persistenceStrategy = new EntityId_NamedXStreamFilePersistenceStrategy(getRepositoryDirectory());
        final Set<PersistentEntity<T>> persistedEntitySet = new XmlSet(persistenceStrategy);
        for (final PersistentEntity<T> persistentEntityFound : persistedEntitySet) {
            final Entity entityFound = persistentEntityFound.getEntity();
            if (entity.equals(entityFound)) {
                persistentEntity = persistentEntityFound;
                break;
            }
        }
        return persistentEntity == null ? null : persistentEntity.getMetaData();
    }

    public void close() {
    }
}
