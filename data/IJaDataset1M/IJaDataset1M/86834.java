package org.slasoi.infrastructure.monitoring.monitors.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostData {

    private static final String type = "host";

    private String fqdn;

    private String name;

    private Map<RawMetric, MetricData> metrics;

    private List<String> vmList;

    public HostData() {
        metrics = new HashMap<RawMetric, MetricData>();
        vmList = new ArrayList<String>();
    }

    public MetricData getMetricData(RawMetric rawMetric) {
        return metrics.get(rawMetric);
    }

    public void putMetricData(MetricData metricData) {
        metrics.put(metricData.getMetric(), metricData);
    }

    public List<String> getVmList() {
        return vmList;
    }

    public void putVm(String fqdn) {
        vmList.add(fqdn);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("type", type);
        o.put("fqdn", fqdn);
        o.put("name", name);
        JSONArray array = new JSONArray();
        for (MetricData metricData : metrics.values()) {
            array.put(metricData.toJson());
        }
        o.put("metrics", array);
        return o;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
