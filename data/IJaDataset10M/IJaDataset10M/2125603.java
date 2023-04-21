package cz.cvut.fel.mvod.persistence.regsys.commonInterface.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Lahvi
 */
public class Additional {

    private Map<String, String> additionals;

    public Additional(Map<String, String> params) {
        additionals = new HashMap<String, String>(params);
    }

    public Additional() {
        additionals = new HashMap<String, String>();
    }

    public String getAdditionalParam(String param) {
        return additionals.get(param);
    }

    public void removeAddtionalParam(String param) {
        additionals.remove(param);
    }

    public void removeAll() {
        Collection<String> keys = new TreeSet<String>(additionals.keySet());
        for (String key : keys) {
            removeAddtionalParam(key);
        }
    }

    public void addAdditonalParam(String name, String value) {
        additionals.put(name, value);
    }

    public Map<String, String> getAdditionalParams() {
        return additionals;
    }

    public void setAdditionalParams(Map<String, String> params) {
        this.additionals = params;
    }

    @Override
    public String toString() {
        return "Dobrovlné údaje mají " + additionals.size() + "údajů";
    }
}
