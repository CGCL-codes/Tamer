package com.google.code.configprocessor;

/**
 * A parser "feature" which can be used to set features on the underlying parser.
 * See {@link http://xerces.apache.org/xerces2-j/features.html} for examples.
 * 
 * @author Leandro Aparecido
 * @see javax.xml.parsers.DocumentBuilderFactory#setFeature(String, boolean)
 */
public class ParserFeature {

    /**
	 * Name of the parser feature.
	 * 
	 * @parameter
	 * @required
	 */
    private String name;

    /**
	 * Value of the parser feature.
	 * 
	 * @parameter
	 * @required
	 */
    private boolean value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
