package org.fao.fenix.web.client.util.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gwt.user.client.ui.ListBox;

public class DataSharingPolicyList {

    private Map dataSharingMap;

    public DataSharingPolicyList() {
        dataSharingMap = new HashMap();
        initializeDataSharingPolicyMap();
    }

    private Map getDataSharingPolicyList() {
        return dataSharingMap;
    }

    private void setDataSharingPolicyList(Map dataSharingMap) {
        this.dataSharingMap = dataSharingMap;
    }

    public String getDataSharingPolicyLabel(String key) {
        String label = (String) dataSharingMap.get(key);
        return label;
    }

    private void initializeDataSharingPolicyMap() {
        dataSharingMap.put("public", "Public");
        dataSharingMap.put("private", "Private");
        dataSharingMap.put("shared", "Shared");
        setDataSharingPolicyList(dataSharingMap);
    }

    public ListBox getDataSharingPolicyListBox() {
        ListBox list = new ListBox();
        Map map = getDataSharingPolicyList();
        list.setWidth("95%");
        list.setName("dataSharing");
        list.addItem("", "");
        Iterator keyValuePairs = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry entry = (Entry) keyValuePairs.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            list.addItem(value, key);
        }
        return list;
    }
}
