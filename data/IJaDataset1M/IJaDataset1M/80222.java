package org.eclipsetrader.core.internal.repositories;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipsetrader.core.feed.FeedIdentifier;
import org.eclipsetrader.core.instruments.ISecurity;
import org.eclipsetrader.core.instruments.Security;
import org.eclipsetrader.core.internal.views.WatchList;
import org.eclipsetrader.core.repositories.IRepository;
import org.eclipsetrader.core.repositories.IRepositoryRunnable;
import org.eclipsetrader.core.repositories.IStore;
import org.eclipsetrader.core.repositories.IStoreProperties;
import org.eclipsetrader.core.repositories.RepositoryResourceDelta;
import org.eclipsetrader.core.views.IWatchListColumn;

public class RepositoryServiceTest extends TestCase {

    private Map<String, TestRepository> repositories;

    @Override
    protected void setUp() throws Exception {
        repositories = new HashMap<String, TestRepository>();
        repositories.put("local", new TestRepository("local"));
        repositories.put("remote", new TestRepository("remote"));
    }

    public void testSaveSecurity() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(1, repositories.get("local").stores.size());
        assertEquals(0, repositories.get("remote").stores.size());
        assertSame(repositories.get("local").stores.get(0), security.getStore());
    }

    public void testMoveSecurity() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(1, repositories.get("local").stores.size());
        assertEquals(0, repositories.get("remote").stores.size());
        service.moveAdaptable(new ISecurity[] { security }, repositories.get("remote"));
        assertEquals(0, repositories.get("local").stores.size());
        assertEquals(1, repositories.get("remote").stores.size());
        assertSame(repositories.get("remote").stores.get(0), security.getStore());
    }

    public void testDeleteSecurity() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(1, repositories.get("local").stores.size());
        service.deleteAdaptable(new ISecurity[] { security });
        assertEquals(0, repositories.get("local").stores.size());
        assertNull(security.getStore());
    }

    public void testSaveSecurityAndAddToCollection() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        assertEquals(0, service.getSecurities().length);
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(1, service.getSecurities().length);
        assertSame(security, service.getSecurities()[0]);
    }

    public void testDeleteSecurityAndRemoveFromCollection() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(1, service.getSecurities().length);
        service.deleteAdaptable(new ISecurity[] { security });
        assertEquals(0, service.getSecurities().length);
    }

    public void testMoveSecurityChangesURI() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        assertEquals(new URI("local", "object", "1"), security.getStore().toURI());
        service.moveAdaptable(new ISecurity[] { security }, repositories.get("remote"));
        assertEquals(new URI("remote", "object", "1"), security.getStore().toURI());
    }

    public void testSaveSecurityEvent() throws Exception {
        final Security security = new Security("Security", new FeedIdentifier("ID", null));
        final RepositoryService service = new TestRepositoryService();
        service.runInService(new IRepositoryRunnable() {

            public IStatus run(IProgressMonitor monitor) throws Exception {
                service.saveAdaptable(new ISecurity[] { security });
                return Status.OK_STATUS;
            }
        }, null);
        RepositoryResourceDelta[] deltas = service.getDeltas();
        assertEquals(1, deltas.length);
        assertSame(security, deltas[0].getResource());
        assertSame(repositories.get("local"), deltas[0].getMovedTo());
    }

    public void testMoveSecurityEvent() throws Exception {
        final Security security = new Security("Security", new FeedIdentifier("ID", null));
        final RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        service.runInService(new IRepositoryRunnable() {

            public IStatus run(IProgressMonitor monitor) throws Exception {
                service.moveAdaptable(new ISecurity[] { security }, repositories.get("remote"));
                return Status.OK_STATUS;
            }
        }, null);
        RepositoryResourceDelta[] deltas = service.getDeltas();
        assertEquals(1, deltas.length);
        assertSame(security, deltas[0].getResource());
        assertSame(repositories.get("local"), deltas[0].getMovedFrom());
        assertSame(repositories.get("remote"), deltas[0].getMovedTo());
    }

    public void testDeleteSecurityEvent() throws Exception {
        final Security security = new Security("Security", new FeedIdentifier("ID", null));
        final RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new ISecurity[] { security });
        service.runInService(new IRepositoryRunnable() {

            public IStatus run(IProgressMonitor monitor) throws Exception {
                service.deleteAdaptable(new ISecurity[] { security });
                return Status.OK_STATUS;
            }
        }, null);
        RepositoryResourceDelta[] deltas = service.getDeltas();
        assertEquals(1, deltas.length);
        assertSame(security, deltas[0].getResource());
        assertSame(repositories.get("local"), deltas[0].getMovedFrom());
        assertNull(deltas[0].getMovedTo());
    }

    public void testRemoveSecurityFromWatchList() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        WatchList list = new WatchList("List", new IWatchListColumn[0]);
        list.addSecurity(security);
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new IAdaptable[] { security, list });
        service.removeSecurityFromContainers(security);
        assertEquals(0, list.getItemCount());
    }

    public void testDeleteSecurityRemovesFromWatchList() throws Exception {
        Security security = new Security("Security", new FeedIdentifier("ID", null));
        WatchList list = new WatchList("List", new IWatchListColumn[0]);
        list.addSecurity(security);
        RepositoryService service = new TestRepositoryService();
        service.saveAdaptable(new IAdaptable[] { security, list });
        assertEquals(2, repositories.get("local").stores.size());
        service.deleteAdaptable(new IAdaptable[] { security });
        assertEquals(1, repositories.get("local").stores.size());
        assertEquals(0, list.getItemCount());
    }

    public class TestRepositoryService extends RepositoryService {

        public TestRepositoryService() {
        }

        @Override
        public IRepository getRepository(String scheme) {
            return repositories.get(scheme);
        }
    }

    public class TestRepository implements IRepository {

        private String scheme;

        public List<TestStore> stores;

        public TestRepository(String scheme) {
            this.scheme = scheme;
            this.stores = new ArrayList<TestStore>();
        }

        @SuppressWarnings("unchecked")
        public Object getAdapter(Class adapter) {
            return null;
        }

        public boolean canDelete() {
            return true;
        }

        public boolean canWrite() {
            return true;
        }

        public IStore createObject() {
            try {
                TestStore store = new TestStore(this, new URI(scheme, "object", String.valueOf(stores.size() + 1)));
                stores.add(store);
                return store;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        public IStore[] fetchObjects(IProgressMonitor monitor) {
            return null;
        }

        public IStore getObject(URI uri) {
            return null;
        }

        public IStatus runInRepository(IRepositoryRunnable runnable, IProgressMonitor monitor) {
            try {
                runnable.run(monitor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Status.OK_STATUS;
        }

        public IStatus runInRepository(IRepositoryRunnable runnable, ISchedulingRule rule, IProgressMonitor monitor) {
            try {
                runnable.run(monitor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Status.OK_STATUS;
        }
    }

    public class TestStore implements IStore {

        private URI uri;

        private TestRepository repository;

        private IStoreProperties properties;

        public TestStore(TestRepository repository, URI uri) {
            this.repository = repository;
            this.uri = uri;
        }

        public void delete(IProgressMonitor monitor) throws CoreException {
            repository.stores.remove(this);
        }

        public IStoreProperties fetchProperties(IProgressMonitor monitor) {
            return properties;
        }

        public IStore[] fetchChilds(IProgressMonitor monitor) {
            return null;
        }

        public IStore createChild() {
            return null;
        }

        public IRepository getRepository() {
            return repository;
        }

        public void putProperties(IStoreProperties properties, IProgressMonitor monitor) {
            this.properties = properties;
        }

        public URI toURI() {
            return uri;
        }
    }
}
