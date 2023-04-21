package ch.olsen.routes.storage;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.routes.atom.AtomException;
import ch.olsen.routes.cell.service.CellServiceImpl.RemoteInputInCell;
import ch.olsen.routes.cell.service.CellServiceImpl.RemoteOutputInCell;
import ch.olsen.routes.data.DataElement;
import ch.olsen.routes.data.AggregatedDataElementImpl.AggregatedType;
import ch.olsen.routes.storage.enginegwt.client.StorageEngineWebIfc;
import ch.olsen.servicecontainer.commongwt.client.SessionException;
import ch.olsen.servicecontainer.domain.SCEntryPoint;
import ch.olsen.servicecontainer.internalservice.auth.AuthInterface;
import ch.olsen.servicecontainer.internalservice.http.JettyService;
import ch.olsen.servicecontainer.service.EntryPoint;
import ch.olsen.servicecontainer.service.HttpFiles;
import ch.olsen.servicecontainer.service.HttpServlet;
import ch.olsen.servicecontainer.service.Inject;
import ch.olsen.servicecontainer.service.Logging;
import com.db4o.ObjectContainer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@HttpFiles(path = "www/ch.olsen.routes.storage.enginegwt.StorageEngine/", index = "StorageEngine.html")
public class QueryService implements QueryInterface {

    @EntryPoint
    public SCEntryPoint sc;

    @Logging
    public Logger log;

    @Inject(uri = "osn://auth/")
    public AuthInterface authService;

    EngineGwtServlet gwtServlet;

    StorageEngine engine;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public void openFile(String fileName) {
        lock.writeLock().lock();
        try {
            if (engine != null) throw new BusyException();
            engine = new StorageEngine(log);
        } finally {
            lock.readLock().lock();
            lock.writeLock().unlock();
        }
        try {
            engine.open(fileName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void close() {
        StorageEngine toClose;
        lock.writeLock().lock();
        toClose = engine;
        engine = null;
        lock.writeLock().unlock();
        toClose.close();
    }

    public void dispose(String session) throws SessionException {
        sc.shutdownService(session);
    }

    @HttpServlet(url = "engineServer")
    public void handleServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        synchronized (this) {
            if (gwtServlet == null) {
                gwtServlet = new EngineGwtServlet();
                JettyService.initializeServlet(gwtServlet, log, "www/ch.olsen.routes.storage.enginegwt.StorageEngine", getClass().getClassLoader());
            }
        }
        gwtServlet.service(request, response);
        response.getWriter().flush();
    }

    public class EngineGwtServlet extends RemoteServiceServlet implements StorageEngineWebIfc {

        private static final long serialVersionUID = 1L;

        public void open(String fileName) {
            QueryService.this.openFile(fileName);
        }

        public void close() {
            QueryService.this.close();
        }

        public StorageInfos getInfos() {
            StorageInfos ret = new StorageInfos();
            ret.isStorage = false;
            if (engine == null) {
                ret.fileName = "no database opened";
                return ret;
            }
            ret.fileName = engine.currentName + ".yap";
            try {
                ObjectContainer db = engine.dbServer.openClient();
                ret.objects = db.ext().storedClass(Object.class).getIDs().length;
            } catch (Exception e) {
            }
            ;
            return ret;
        }

        public String simpleQuery(String query) {
            return null;
        }

        public void discardAndStartFresh() {
        }

        public void saveAndStartFresh(String fileName) {
        }
    }

    public void fireEvent(String inputName, DataElement data) throws AtomException {
        engine.fireEvent(inputName, data);
    }

    public AggregatedType getInputs() throws AtomException {
        return engine.getInputs();
    }

    public AggregatedType getOutputs() throws AtomException {
        return engine.getOutputs();
    }

    public void linkInput(String inputName, RemoteOutputInCell link) throws AtomException {
        engine.linkInput(inputName, link);
    }

    public String linkOutput(String outputName, RemoteInputInCell link) throws AtomException {
        return engine.linkOutput(outputName, link);
    }

    public DataElement pollOutput(String outputName) throws AtomException {
        return engine.pollOutput(outputName);
    }

    public boolean unLinkOutput(String outputName, String linkName) throws AtomException {
        return engine.unLinkOutput(outputName, linkName);
    }
}
