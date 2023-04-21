package org.rapla;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.rapla.client.ClientService;
import org.rapla.components.util.IOUtil;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.Container;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.gui.toolkit.ErrorDialog;

public abstract class RaplaTestCase extends TestCase {

    protected Container raplaContainer;

    private Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_WARN).getChildLogger("test");

    public static String TEST_FOLDER_NAME = "temp/test";

    RaplaStartupEnvironment env = new RaplaStartupEnvironment();

    public RaplaTestCase(String name) {
        super(name);
        try {
            new File("temp").mkdir();
            File testFolder = new File(TEST_FOLDER_NAME);
            testFolder.mkdir();
            IOUtil.copy("test-src/test.xconf", TEST_FOLDER_NAME + "/test.xconf");
            IOUtil.copy("test-src/test.xlog", TEST_FOLDER_NAME + "/test.xlog");
        } catch (IOException ex) {
            throw new RuntimeException("Can't initialize config-files: " + ex.getMessage());
        }
    }

    public void copyDataFile(String testFile) throws IOException {
        try {
            IOUtil.copy(testFile, TEST_FOLDER_NAME + "/test.xml");
        } catch (IOException ex) {
            throw new IOException("Failed to copy TestFile '" + testFile + "': " + ex.getMessage());
        }
    }

    protected <T> T getService(Class<T> role) throws RaplaException {
        return (T) getContext().lookup(role.getName());
    }

    protected RaplaContext getContext() {
        return raplaContainer.getContext();
    }

    protected SerializableDateTimeFormat formater() {
        return new SerializableDateTimeFormat();
    }

    protected Logger getLogger() {
        return logger;
    }

    protected void setUp(String testFile) throws Exception {
        ErrorDialog.THROW_ERROR_DIALOG_EXCEPTION = true;
        URL configURL = new URL("file:./" + TEST_FOLDER_NAME + "/test.xconf");
        URL logConfigURL = new URL(IOUtil.getBase(configURL), "test.xlog");
        env.setConfigURL(configURL);
        env.setLogConfigURL(logConfigURL);
        copyDataFile("test-src/" + testFile);
        raplaContainer = new RaplaMainContainer(env);
        assertNotNull("Container not initialized.", raplaContainer);
    }

    protected void setUp() throws Exception {
        setUp("testdefault.xml");
    }

    protected ClientService getClientService() throws Exception {
        return (ClientService) getContext().lookup(ClientService.class.getName());
    }

    protected ClientFacade getFacade() throws Exception {
        return (ClientFacade) getContext().lookup(ClientFacade.ROLE);
    }

    protected RaplaLocale getRaplaLocale() throws Exception {
        return (RaplaLocale) getContext().lookup(RaplaLocale.ROLE);
    }

    protected void tearDown() throws Exception {
        if (raplaContainer != null) ContainerUtil.dispose(raplaContainer);
    }
}
