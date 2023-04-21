package edu.drexel.sd0910.ece01.aqmon.data.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.json.simple.JSONObject;
import edu.drexel.sd0910.ece01.aqmon.data.JSONFile;
import edu.drexel.sd0910.ece01.aqmon.data.calc.CalculationResult;

/**
 * Datastore using a thread-safe synchronized hashmap to store data.
 * 
 * @author Kyle O'Connor
 * 
 */
public class DataStore {

    protected Map<String, Vector<CalculationResult>> dataMap;

    protected Properties props;

    /**
	 * Create a new synchronized Hash Map to be used as a DataStore.
	 */
    public DataStore(Properties p) {
        dataMap = Collections.synchronizedMap(new HashMap<String, Vector<CalculationResult>>());
        this.props = p;
    }

    public Vector<CalculationResult> getData(String key) {
        return dataMap.get(key);
    }

    public void storeData(CalculationResult data) {
        String key = data.getNodeID();
        if (dataMap.containsKey(key)) {
            Vector<CalculationResult> temp = dataMap.get(key);
            temp.add(data);
            dataMap.put(key, temp);
        } else {
            Vector<CalculationResult> temp = new Vector<CalculationResult>();
            temp.add(data);
            dataMap.put(key, temp);
        }
    }

    @Override
    public String toString() {
        return "DataStore [dataMap=" + dataMap + "]";
    }

    /**
	 * Utilizes a data formatting class such a FlotData whose toString method
	 * outputs data given to it in a flot displayable manner. Returns an array
	 * of InputStreams for use in uploading data. Supposed to be thread-safe.
	 * 
	 * @return an array of JSONFile
	 */
    @SuppressWarnings("unchecked")
    public List<JSONFile> exportJSONData() {
        Set<String> nodeIDs = dataMap.keySet();
        List<JSONFile> outputFiles = new ArrayList<JSONFile>(nodeIDs.size() * 2);
        synchronized (dataMap) {
            for (String id : nodeIDs) {
                int index = Integer.parseInt(id.substring(id.indexOf('N') + 1));
                JSONObject obj25 = new JSONObject();
                JSONObject obj10 = new JSONObject();
                obj25.put("label", id + " - PM2.5");
                obj10.put("label", id + " - PM10");
                obj25.put("color", index);
                obj10.put("color", index);
                List data25 = new LinkedList();
                List data10 = new LinkedList();
                for (CalculationResult d : dataMap.get(id)) {
                    List ll10 = new LinkedList();
                    List ll25 = new LinkedList();
                    ll25.add(d.getDate());
                    ll25.add(d.getPM25concentation());
                    data25.add(ll25);
                    ll10.add(d.getDate());
                    ll10.add(d.getPM10concentration());
                    data10.add(ll10);
                }
                obj25.put("data", data25);
                obj10.put("data", data10);
                outputFiles.add(new JSONFile(obj25, id + "_data" + "25.json"));
                outputFiles.add(new JSONFile(obj10, id + "_data" + "10.json"));
            }
        }
        return outputFiles;
    }
}
