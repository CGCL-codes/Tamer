package org.kaleidofoundry.core.store;

import java.net.URI;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.io.FileHelper;
import org.kaleidofoundry.core.persistence.UnmanagedEntityManagerFactory;
import org.kaleidofoundry.core.store.entity.ResourceHandlerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerome RADUGET
 */
public class JpaFileStoreTest extends AbstractFileStoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaFileStoreTest.class);

    private EntityManagerFactory emf;

    private EntityManager em;

    private EntityTransaction transaction;

    @Before
    @Override
    public void setup() throws Throwable {
        try {
            emf = UnmanagedEntityManagerFactory.getEntityManagerFactory();
            em = UnmanagedEntityManagerFactory.currentEntityManager();
            final RuntimeContext<FileStore> context = new FileStoreContextBuilder().withBaseUri("jpa:/").build();
            fileStore = new JpaFileStore(context);
            transaction = em.getTransaction();
            transaction.begin();
            final ResourceHandlerEntity entityToGet = new ResourceHandlerEntity();
            final URI resourceUriToGet = URI.create("jpa:/tmp/foo.txt");
            final String filenameToGet = resourceUriToGet.getPath().substring(1);
            entityToGet.setUri(resourceUriToGet.toString());
            entityToGet.setName(FileHelper.getFileName(filenameToGet));
            entityToGet.setCreationDate(Calendar.getInstance().getTime());
            entityToGet.setPath(filenameToGet);
            entityToGet.setContent(DEFAULT_RESOURCE_MOCK_TEST.getBytes());
            em.persist(entityToGet);
            em.flush();
            existingResources.put(resourceUriToGet.getPath(), DEFAULT_RESOURCE_MOCK_TEST);
            nonExistingResources.add("foo");
            final String filenameToStore = "tmp/fooToStore.txt";
            existingResourcesForStore.put(filenameToStore, DEFAULT_RESOURCE_MOCK_TEST);
            final ResourceHandlerEntity entityToRemove = new ResourceHandlerEntity();
            final URI resourceUriToRemove = URI.create("jpa:/tmp/fooToRemove.txt");
            final String filenameToRemove = resourceUriToRemove.getPath().substring(1);
            entityToRemove.setUri(resourceUriToRemove.toString());
            entityToRemove.setName(FileHelper.getFileName(filenameToRemove));
            entityToRemove.setCreationDate(Calendar.getInstance().getTime());
            entityToRemove.setPath(filenameToRemove);
            entityToRemove.setContent(DEFAULT_RESOURCE_MOCK_TEST.getBytes());
            em.persist(entityToRemove);
            em.flush();
            existingResourcesForRemove.put(filenameToRemove, DEFAULT_RESOURCE_MOCK_TEST);
            final ResourceHandlerEntity entityToMove = new ResourceHandlerEntity();
            final URI resourceUriToMove = URI.create("jpa:/tmp/fooToMove.txt");
            final String filenameToMove = resourceUriToMove.getPath().substring(1);
            entityToMove.setUri(resourceUriToMove.toString());
            entityToMove.setName(FileHelper.getFileName(filenameToMove));
            entityToMove.setCreationDate(Calendar.getInstance().getTime());
            entityToMove.setPath(filenameToMove);
            entityToMove.setContent(DEFAULT_RESOURCE_MOCK_TEST.getBytes());
            em.persist(entityToMove);
            em.flush();
            existingResourcesForMove.put(filenameToMove, filenameToMove + ".move");
        } catch (final RuntimeException rte) {
            LOGGER.error("setup error", rte);
            throw rte;
        }
    }

    @After
    @Override
    public void cleanup() {
        try {
            final URI resourceUri = URI.create("jpa:/tmp/foo.txt");
            final ResourceHandlerEntity resource = em.find(ResourceHandlerEntity.class, resourceUri.toString());
            if (resource != null) {
                em.remove(resource);
                transaction.commit();
            }
        } finally {
            UnmanagedEntityManagerFactory.closeItSilently(em);
            UnmanagedEntityManagerFactory.closeItSilently(emf);
        }
    }
}
