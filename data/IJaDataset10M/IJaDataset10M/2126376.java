package phsperformance.util.data;

import java.io.FileReader;
import java.util.Properties;
import java.util.Vector;

public class RequestCollection {

    private static final String OBSERVATIONS_KEY = "collection.observations";

    private static final String OBSERVATIONS_SEP = ",";

    private Vector<RequestInfo> m_collection;

    public RequestCollection() {
        m_collection = new Vector<RequestInfo>();
    }

    public void add(RequestInfo reqInfo) {
        m_collection.add(reqInfo);
    }

    public Vector<RequestInfo> getRequests() {
        return m_collection;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        StringBuffer obsIds = new StringBuffer();
        int i = 0;
        for (RequestInfo reqInfo : m_collection) {
            String obsId = "obs" + i;
            if (i > 0) {
                obsIds.append(OBSERVATIONS_SEP);
            }
            obsIds.append(obsId);
            props.putAll(reqInfo.getProperties(obsId));
            i++;
        }
        props.put(OBSERVATIONS_KEY, obsIds.toString());
        return props;
    }

    public static RequestCollection recoverFromProperties(Properties props) {
        RequestCollection collection = new RequestCollection();
        String[] observations = props.getProperty(OBSERVATIONS_KEY).split(OBSERVATIONS_SEP);
        for (String obsId : observations) {
            RequestInfo reqInfo = RequestInfo.recoverFromProperties(obsId, props);
            collection.add(reqInfo);
        }
        return collection;
    }

    public static RequestCollection recoverFromProperties(String filename) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileReader(filename));
        return RequestCollection.recoverFromProperties(prop);
    }
}
