package halo.datasource.c3p0ex;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DataSourceCnf {

    private String dsName;

    private final Map<String, String> cnfMap = new HashMap<String, String>();

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public Map<String, String> getCnfMap() {
        return cnfMap;
    }

    public void setKeyAndValue(String key, String value) {
        cnfMap.put(key, value);
    }

    public String getValueForKey(String key) {
        return cnfMap.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dsName).append("|");
        Set<Entry<String, String>> set = cnfMap.entrySet();
        for (Entry<String, String> e : set) {
            sb.append(e.getKey()).append(":").append(e.getValue()).append("|");
        }
        return sb.toString();
    }
}
