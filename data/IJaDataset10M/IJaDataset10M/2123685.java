package com.quikj.server.app;

import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.quikj.server.framework.AceLogger;

public class AccountElement {

    private String name;

    private String domain;

    private int level;

    private String additionalInfo;

    private HashSet featureList = new HashSet();

    public AccountElement() {
    }

    public synchronized boolean addFeature(String featurename) {
        return featureList.add(new String(featurename));
    }

    public String featureListToString() {
        StringBuffer strbuf = new StringBuffer();
        int count = 0;
        for (Iterator i = featureList.iterator(); i.hasNext(); count++) {
            strbuf.append((String) i.next());
            if (count < (featureList.size() - 1)) {
                strbuf.append(',');
            }
        }
        return strbuf.toString();
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getDomain() {
        return domain;
    }

    public synchronized String[] getFeatureList() {
        String[] list = new String[featureList.size()];
        int count = 0;
        for (Iterator i = featureList.iterator(); i.hasNext(); count++) {
            list[count] = new String((String) i.next());
        }
        return list;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public synchronized boolean hasFeature(String featurename) {
        return featureList.contains(featurename);
    }

    public synchronized int numFeatures() {
        return featureList.size();
    }

    public synchronized boolean removeFeature(String featurename) {
        return featureList.remove(featurename);
    }

    public void setAdditionalInfo(String info) {
        additionalInfo = info;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void stringToFeatureList(String feature_list_string) {
        if (feature_list_string.length() > 0) {
            StringTokenizer strtok = new StringTokenizer(feature_list_string, ",");
            int num_tokens = strtok.countTokens();
            for (int i = 0; i < num_tokens; i++) {
                String feature = strtok.nextToken();
                if (addFeature(feature) == false) {
                    AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, name + "- AccountElement.stringToFeatureList() -- Couldn't add feature " + feature + ", probably duplicate error. Feature list string = " + feature_list_string);
                }
            }
        }
    }
}
