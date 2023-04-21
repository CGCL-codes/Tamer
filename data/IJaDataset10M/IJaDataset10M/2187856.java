package org.jaffa.components.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SeanZ
 */
public class SavedQueryBean {

    private String componentName;

    private Map savedQueryUrls = new HashMap();

    /** Creates a new instance of SavedQueryList */
    public SavedQueryBean(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }

    public List<SavedQuery> getSavedQueries() {
        List out = new ArrayList();
        String[] qns = (String[]) savedQueryUrls.keySet().toArray(new String[0]);
        Arrays.sort(qns);
        for (int i = 0; i < qns.length; i++) {
            out.add(savedQueryUrls.get(qns[i]));
        }
        return out;
    }

    public void setSavedQueryUrl(String id, String name, String url) {
        savedQueryUrls.put(name, new SavedQuery(id, name, url));
    }
}
