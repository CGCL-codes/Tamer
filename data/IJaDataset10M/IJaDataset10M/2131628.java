package org.mga.common;

public class SearchKey {

    private String key;

    private String operator;

    private String value;

    private String mixter;

    /**
	 *
	 */
    public SearchKey(String key, String operator, String value, String mixter) {
        this.key = key;
        this.operator = operator;
        this.mixter = mixter;
        this.value = value;
    }

    /**
	 *
	 */
    public SearchKey(String key, String operator, String value) {
        this.key = key;
        this.operator = operator;
        this.mixter = "AND";
        this.value = value;
    }

    /**
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return the mixter
	 */
    public String getMixter() {
        return mixter;
    }

    /**
	 * @param mixter the mixter to set
	 */
    public void setMixter(String mixter) {
        this.mixter = mixter;
    }

    /**
	 * @return the operator
	 */
    public String getOperator() {
        return operator;
    }

    /**
	 * @param operator the operator to set
	 */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
