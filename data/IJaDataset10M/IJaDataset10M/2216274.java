package javax.jcr.tools.backup.impl.systemview;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;
import javax.jcr.Session;
import javax.jcr.tools.backup.Context;
import javax.jcr.tools.backup.RestoreWorker;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * @author Alex Karshakevich
 * Date:    Aug 13, 2007
 * Time:    4:24:20 PM
 */
public class SystemViewRestoreWorkerImpl implements RestoreWorker {

    /**
   * Default is to import the root node
   */
    public static final String DEFAULT_IMPORT_PATH = "/";

    /**
   * Default constructor with required configuration
   */
    public SystemViewRestoreWorkerImpl() {
    }

    /**
   * Restore repository data from a system view backup
   * @param context execution context
   * @throws Exception when unable to perform an operation.
   */
    public void execute(Context context) throws Exception {
        InputStream input = null;
        try {
            input = context.getPersistenceManager().getInResource(SystemViewBackupWorkerImpl.REPOSITORY_DATA_RESOURCENAME, true);
            final Session session = context.getSession();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            XMLFilter filter = new SplitImportXMLFilterImpl(reader, session);
            filter.parse(new InputSource(input));
            session.logout();
        } finally {
            if (null != input) input.close();
        }
    }
}
