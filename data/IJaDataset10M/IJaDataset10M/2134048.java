package br.ufmg.lcc.pcollecta.dto;

import java.util.Map;

public class MapInfo {

    private Map<String, Object> keys;

    private String additionalInfo;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setKeys(Map<String, Object> keys) {
        this.keys = keys;
    }

    public Map<String, Object> getKeys() {
        return keys;
    }
}
