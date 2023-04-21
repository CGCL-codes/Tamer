package org.stateengine.tags;

public class Pair implements IPair {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getPairKey() {
        return key;
    }

    public String getPairValue() {
        return value;
    }
}
