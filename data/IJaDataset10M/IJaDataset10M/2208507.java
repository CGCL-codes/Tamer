package com.jedox.etl.core.config.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

/**
 * Helper class for the configuration of output fields used in transforms 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class ColumnConfigurator {

    private static final Log log = LogFactory.getLog(ColumnConfigurator.class);

    private String name;

    public ColumnConfigurator(String name) {
        this.name = name;
    }

    /**
	 * gets the name of the transform
	 * @return the transform name
	 */
    public String getName() {
        return name;
    }

    /**
	 * gets the name of the column
	 * @param column the XML defining the column
	 * @param inputName the input name used as fallback, if no column name is specified
	 * @return the column name
	 */
    public String getColumnName(Element column, String inputName) {
        return column.getAttributeValue("name", inputName);
    }

    private String getInputNameInternal(Element column) {
        Element input = column.getChild("input");
        if (input == null) return column.getAttributeValue("name");
        String name = input.getAttributeValue("nameref");
        if (name != null) return name;
        return "constant";
    }

    /**
	 * gets the name of the input field for this output column
	 * @param column the XML defining the output column
	 * @return the input field name
	 */
    public String getInputName(Element column) {
        String name = getInputNameInternal(column);
        if (name == null) log.error("Target " + getName() + " has column without name!");
        return name;
    }

    private String getText(Element element) {
        String text = element.getTextNormalize();
        if (text.equals("")) return null;
        return text;
    }

    /**
	 * gets the input value for a constant field used as input for this output column 
	 * @param column the XML defining the output column
	 * @return the input field value
	 */
    public String getInputValue(Element column) {
        Element input = column.getChild("input");
        if (input == null) return getText(column);
        return input.getAttributeValue("constant");
    }
}
