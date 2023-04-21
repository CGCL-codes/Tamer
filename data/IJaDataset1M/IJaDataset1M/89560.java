package org.sensorweb.model.gml.basic;

import java.io.Serializable;
import org.jdom.Element;
import org.sensorweb.service.scs.NamespaceConstant;

/**
 * @author Xingchen Chu
 * @version 0.1
 *
 * <code> CoordinateType </code> stands for the  
 * Tables or arrays of tuples. May be used for text-encoding of 
 * values from a table. Actually just a string, but allows the 
 * user to indicate which characters are used as separators. The 
 * value of the 'cs' attribute is the separator for coordinate 
 * values, and the value of the 'ts' attribute gives the tuple 
 * separator (a single space by default); the default values may 
 * be changed to reflect local usage. Defaults to CSV within a tuple, 
 * space between tuples. However, any string content will be 
 * schema-valid
 * 
 */
public class CoordinatesType implements Serializable {

    private String decimal = ".";

    private String cs = ",";

    private String ts = " ";

    private String value;

    public CoordinatesType() {
        this("default");
    }

    public CoordinatesType(String value) {
        this.value = value;
    }

    /**
	 * @return Returns the cs.
	 */
    public String getCs() {
        return cs;
    }

    /**
	 * @param cs The cs to set.
	 */
    public void setCs(String cs) {
        this.cs = cs;
    }

    /**
	 * @return Returns the decimal.
	 */
    public String getDecimal() {
        return decimal;
    }

    /**
	 * @param decimal The decimal to set.
	 */
    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    /**
	 * @return Returns the ts.
	 */
    public String getTs() {
        return ts;
    }

    /**
	 * @param ts The ts to set.
	 */
    public void setTs(String ts) {
        this.ts = ts;
    }

    /**
	 * @return Returns the value.
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value The value to set.
	 */
    public void setValue(String value) {
        this.value = value;
    }

    public Element buildCoordinatesType(String name) {
        Element elem = new Element(name, NamespaceConstant.GML_NAMESPACE);
        if (!"".equals(getDecimal())) {
            elem.setAttribute("decimal", getDecimal());
        }
        if (!"".equals(getCs())) {
            elem.setAttribute("cs", getCs());
        }
        if (!"".equals(getTs())) {
            elem.setAttribute("ts", getTs());
        }
        elem.addContent(getValue());
        return elem;
    }
}
