package org.ssa4j.enterprise;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSessionManager;
import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;
import com.screenscraper.soapclient.SOAPInterface;
import com.screenscraper.soapclient.SOAPInterfaceService;
import com.screenscraper.soapclient.SOAPInterfaceServiceLocator;

public class EnterpriseScrapeSessionManager extends ScrapeSessionManager {

    private SOAPInterface soap;

    private int timeout = 1;

    private static final ThreadLocal<String> soapSessionId = new ThreadLocal<String>();

    private static final ThreadLocal<Map<String, String>> sessionVariables = new ThreadLocal<Map<String, String>>() {

        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<String, String>();
        }
    };

    private static final ThreadLocal<Map<String, DataSet>> dataSetCache = new ThreadLocal<Map<String, DataSet>>() {

        @Override
        protected Map<String, DataSet> initialValue() {
            return new HashMap<String, DataSet>();
        }
    };

    public EnterpriseScrapeSessionManager() throws ScrapeException {
        String host = System.getProperty(ScrapeConstants.SSA4J_HOST_KEY, "localhost");
        int port = Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_PORT_KEY, "8779"));
        int timeout = Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_TIMEOUT_KEY, "1"));
        String endpoint = String.format("http://%s:%d/axis/services/SOAPInterface", host, port);
        this.initialize(endpoint, timeout);
    }

    public EnterpriseScrapeSessionManager(String endpoint, int timeout) throws ScrapeException {
        this.initialize(endpoint, timeout);
    }

    private void initialize(String endpoint, int timeout) throws ScrapeException {
        SOAPInterfaceService service = new SOAPInterfaceServiceLocator();
        try {
            log.info(String.format("Endpoint: %s, timeout: %d", endpoint, timeout));
            this.soap = service.getSOAPInterface(new URL(endpoint));
            this.timeout = timeout;
        } catch (Exception e) {
            throw new ScrapeException("Error creating SoapScrapeSessionManager", e);
        }
    }

    @Override
    public void close() throws ScrapeException {
        try {
            soap.removeCompletedScrapingSession(soapSessionId.get());
            soapSessionId.remove();
            sessionVariables.remove();
            dataSetCache.remove();
        } catch (RemoteException e) {
            throw new ScrapeException("Error closing session", e);
        }
    }

    @Override
    protected void execute(Object source, Map<String, String> cookiejar) throws ScrapeException {
        String sessionId = getSessionId(source);
        try {
            soapSessionId.set(soap.initializeScrapingSession(sessionId));
            soap.setTimeout(soapSessionId.get(), timeout);
            for (Entry<String, String> entry : sessionVariables.get().entrySet()) {
                soap.setVariable(soapSessionId.get(), entry.getKey(), entry.getValue());
            }
            soap.scrape(soapSessionId.get());
            while (soap.isFinished(soapSessionId.get()) != 1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (RemoteException e) {
            throw new ScrapeException("Error executing session", e);
        }
    }

    @Override
    protected DataSet getDataSet(String name) throws ScrapeException {
        return loadDataSet(name);
    }

    @Override
    protected String getVariable(String name) throws ScrapeException {
        try {
            return soap.getVariable(soapSessionId.get(), name);
        } catch (RemoteException e) {
            throw new ScrapeException("Error getting variable " + name, e);
        }
    }

    @Override
    protected void setVariable(String name, String value) throws ScrapeException {
        sessionVariables.get().put(name, value);
    }

    protected DataSet loadDataSet(String dataSetId) throws ScrapeException {
        DataSet ds = null;
        try {
            if (dataSetCache.get().containsKey(dataSetId)) {
                ds = dataSetCache.get().get(dataSetId);
            } else {
                String[][] rawDataSet = soap.getDataSet(soapSessionId.get(), dataSetId);
                ds = createDataSet(rawDataSet);
                dataSetCache.get().put(dataSetId, ds);
            }
        } catch (RemoteException e) {
            throw new ScrapeException("Error loading DataSet " + dataSetId, e);
        }
        return ds;
    }

    protected DataSet createDataSet(String[][] rawDataSet) {
        DataSet ds = new DataSet();
        for (String[] rawRec : rawDataSet) {
            ds.addDataRecord(createDataRecord(rawRec));
        }
        return ds;
    }

    protected DataRecord createDataRecord(String[] rawRec) {
        DataRecord rec = new DataRecord();
        for (String rawField : rawRec) {
            String[] keyval = rawField.split("=");
            rec.put(keyval[0], keyval[1]);
        }
        return rec;
    }
}
