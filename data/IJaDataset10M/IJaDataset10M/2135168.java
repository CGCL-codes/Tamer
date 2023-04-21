package net.code4j.dolon.service.indexer;

import java.util.Collection;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.code4j.dolon.service.catalog.Artifact;
import net.code4j.dolon.service.catalog.CatalogDao;
import net.code4j.dolon.service.catalog.IndexedArtifact;
import net.code4j.dolon.service.repsitory.Repository;
import net.code4j.dolon.service.repsitory.RepositoryReadException;
import net.code4j.dolon.service.repsitory.RepositoryService;
import net.code4j.dolon.service.repsitory.RepositoryWalker;

/**
 * Indiziert Repositorys.
 * @author xandro
 */
public final class IndexerServiceImpl {

    private static final Log LOG = LogFactory.getLog(IndexerServiceImpl.class);

    private RepositoryService repositoryService;

    private CatalogDao catalogDao;

    /**
	 * indiziert alle konfigurierten repositorys.
	 */
    public void indexRepositorys() {
        Map<String, Repository> repos = repositoryService.getRepos();
        Collection<Repository> repcol = repos.values();
        for (Repository repository : repcol) {
            try {
                LOG.info("try to index repository: " + repository.getRepositoryId());
                indexRepo(repository);
            } catch (RepositoryReadException e) {
                LOG.warn("could not index repository: " + e);
            }
        }
    }

    private void indexRepo(Repository r) throws RepositoryReadException {
        RepositoryWalker rw = r.createRepositoryWalker();
        Artifact a;
        while ((a = rw.nextArtifact()) != null) {
            LOG.info("try to index artifact: " + a);
            IndexedArtifact artifactModel = repositoryService.readArtifactModel(a);
            catalogDao.update(artifactModel);
        }
    }

    /**
	 * @param repositoryService the repositoryService to set
	 */
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
	 * @param catalogDao the catalogDao to set
	 */
    public void setCatalogDao(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }
}
