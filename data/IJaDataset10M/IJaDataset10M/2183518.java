package org.fao.fenix.domain4.contentdefinition;

public abstract class QualitativeContent extends Content {

    private String value;

    /**
     * 
     */
    public QualitativeContent() {
        super();
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
