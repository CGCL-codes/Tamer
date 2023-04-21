package jfs.sync.external;

import java.util.HashMap;
import jfs.conf.JFSConfig;
import jfs.server.JFSServerAccess;
import jfs.sync.JFSFileProducer;
import jfs.sync.JFSFileProducerFactory;

/**
 * This class produces factories for local JFS files.
 * 
 * @author Jens Heidrich
 * @version $Id: JFSExternalFileProducerFactory.java,v 1.1 2005/05/06 11:06:56
 *          heidrich Exp $
 */
public class JFSExternalFileProducerFactory extends JFSFileProducerFactory {

    /** The map of file producers. */
    private HashMap<String, JFSExternalFileProducer> producers = new HashMap<String, JFSExternalFileProducer>();

    /**
	 * @see JFSFileProducerFactory#resetProducers()
	 */
    public final void resetProducers() {
        producers.clear();
    }

    /**
	 * @see JFSFileProducerFactory#createProducer(String)
	 */
    public final JFSFileProducer createProducer(String uri) {
        JFSExternalFileProducer p = new JFSExternalFileProducer(uri);
        producers.put(uri, p);
        return p;
    }

    /**
	 * @see JFSFileProducerFactory#shutDownProducer(String)
	 */
    public final void shutDownProducer(String uri) {
        JFSExternalFileProducer p = producers.get(uri);
        if (p != null && JFSConfig.getInstance().getServerShutDown()) {
            JFSServerAccess sa = JFSServerAccess.getInstance(p.getHost(), p.getPort(), p.getRootPath());
            sa.shutDown();
        }
    }

    /**
	 * @see JFSFileProducerFactory#cancelProducer(String)
	 */
    public final void cancelProducer(String uri) {
        JFSExternalFileProducer p = producers.get(uri);
        if (p != null) {
            JFSServerAccess sa = JFSServerAccess.getInstance(p.getHost(), p.getPort(), p.getRootPath());
            sa.cancel();
        }
    }
}
