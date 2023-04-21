package org.dcm4chee.docstore.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import junit.framework.TestCase;
import org.dcm4chee.docstore.DocumentStorageRegistry;
import org.dcm4chee.docstore.DocumentStore;
import org.dcm4chee.docstore.spi.DocumentStorage;
import org.dcm4chee.docstore.spi.file.DocumentFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocStoreTestBase extends TestCase {

    protected static DocumentStorageRegistry registry;

    protected static DocumentStore docStore;

    private static Logger log = LoggerFactory.getLogger(DocStoreTestBase.class);

    protected DocStoreTestBase() {
        init();
    }

    private static void init() {
        if (registry == null) {
            MBeanServer mbServer = MBeanServerFactory.createMBeanServer();
            try {
                mbServer.createMBean("org.dcm4chee.docstore.test.DummyDFCommandMBean", new ObjectName("dcm4chee.archive:service=dfcmd"));
            } catch (Exception ignore) {
                log.error("Can't create TestDFCommandMBean!", ignore);
            }
            registry = new DocumentStorageRegistry();
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource("test_docstore_cfg.xml");
            log.info("################## Test docstore cfg file:" + url);
            registry.config(url.toExternalForm());
            purgeDocumentDirs(registry.getDocumentStorages("POOL"));
            purgeDocumentDirs(registry.getDocumentStorages("TEST"));
        }
    }

    private static void purgeDocumentDirs(Collection<DocumentStorage> stores) {
        if (stores == null) return;
        File f;
        for (DocumentStorage st : stores) {
            f = ((DocumentFileStorage) st).getBaseDir();
            log.info("DELETE document storage directory initially! baseDir: " + f);
            TestUtil.deleteDir(f, false);
        }
    }

    protected void initDocStore() throws IOException {
        if (docStore == null) {
            DocumentStore.setDocumentStorageRegistry(registry);
            docStore = DocumentStore.getInstance("TestDocStore_TEST", TestUtil.DOMAIN_TEST);
        }
    }
}
