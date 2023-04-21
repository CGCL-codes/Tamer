package net.sf.dropboxmq.workflow.adapters.ejb;

import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.dropboxmq.workflow.WorkflowProcessor;
import net.sf.dropboxmq.workflow.adapters.CollectionAdapter;
import net.sf.dropboxmq.workflow.adapters.http.HTTPAdapter;
import net.sf.dropboxmq.workflow.adapters.jms.JMSAdapter;
import net.sf.dropboxmq.workflow.data.EventPackage;
import net.sf.dropboxmq.workflow.persistence.jdbc.PersistenceFactoryImpl;
import net.sf.dropboxmq.workflow.xml.WorkflowTransformerFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * Created: 21 Mar 2011
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision$, $Date$
 */
@Stateless
public class WorkflowEJB implements WorkflowLocal {

    private static final Log log = LogFactory.getLog(WorkflowEJB.class);

    private final PersistenceFactoryImpl persistenceFactory = new PersistenceFactoryImpl();

    private final WorkflowProcessor processor = new WorkflowProcessor("main", persistenceFactory, new WorkflowTransformerFactoryImpl());

    private SessionContext sessionContext = null;

    @Override
    public EventPackage onEvent(final String protocol, final String content, final Map<String, String> properties) {
        log.info("=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v=v= START");
        assert sessionContext != null;
        final HTTPAdapter httpAdapter = new HTTPAdapter();
        final CollectionAdapter adapter = newCollectionAdapter(httpAdapter);
        try {
            processor.onEvent(protocol, content, properties, adapter);
        } finally {
            adapter.close();
            log.info("=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=^=   END");
        }
        return httpAdapter.getResponseEvent();
    }

    private CollectionAdapter newCollectionAdapter(final HTTPAdapter httpAdapter) {
        final CollectionAdapter collectionAdapter = new CollectionAdapter();
        final JMSAdapter jmsAdapter = new JMSAdapter("java:/dropboxmq/XAConnectionFactory", sessionContext);
        collectionAdapter.addAdapter(JMSAdapter.JMS, jmsAdapter);
        collectionAdapter.addAdapter(HTTPAdapter.HTTP, httpAdapter);
        return collectionAdapter;
    }

    @PostConstruct
    public void postConstruct() throws IOException, SAXException, ParserConfigurationException {
        log.info("postConstruct()");
        persistenceFactory.setDataSource((DataSource) sessionContext.lookup("java:/DropboxMQESBDS"));
        processor.initialize();
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    @Resource
    private void setSessionContext(final SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
