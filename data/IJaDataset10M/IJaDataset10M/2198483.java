package br.ufmg.catustec.arangi.dto;

import java.io.Serializable;
import java.util.List;

public class ReleasedVersion implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String version;

    private List<String> keys;

    private List<String> notes;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
