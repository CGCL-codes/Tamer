package org.biomage.BioAssayData;

import java.io.Serializable;
import java.util.*;
import org.xml.sax.Attributes;
import java.io.Writer;
import java.io.IOException;

/**
 *  A three-dimensional cube representation of the data.
 *  
 */
public class BioDataCube extends BioDataValues implements Serializable {

    /**
     *  Inner class for the enumeration values that the attribute order 
     *  can assume.
     *  
     */
    public class Order {

        int value = -1;

        String name = null;

        private HashMap nameToValue = new HashMap(6);

        private HashMap valueToName = new HashMap(6);

        public final int BDQ = 0;

        public final int BQD = 1;

        public final int DBQ = 2;

        public final int DQB = 3;

        public final int QBD = 4;

        public final int QDB = 5;

        Order() {
            nameToValue.put("BDQ", new Integer(0));
            valueToName.put(new Integer(0), "BDQ");
            nameToValue.put("BQD", new Integer(1));
            valueToName.put(new Integer(1), "BQD");
            nameToValue.put("DBQ", new Integer(2));
            valueToName.put(new Integer(2), "DBQ");
            nameToValue.put("DQB", new Integer(3));
            valueToName.put(new Integer(3), "DQB");
            nameToValue.put("QBD", new Integer(4));
            valueToName.put(new Integer(4), "QBD");
            nameToValue.put("QDB", new Integer(5));
            valueToName.put(new Integer(5), "QDB");
        }

        public int setValueByName(String name) {
            value = ((Integer) nameToValue.get(name)).intValue();
            return value;
        }

        public int getValueByName(String name) {
            return ((Integer) nameToValue.get(name)).intValue();
        }

        public String setNameByValue(int val) {
            name = ((String) valueToName.get(new Integer(val)));
            return name;
        }

        public String getNameByValue(int val) {
            return ((String) valueToName.get(new Integer(val)));
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return getNameByValue(getValue());
        }
    }

    /**
     *  The order to expect the dimension.  The enumeration uses the 
     *  first letter of the three dimensions to represent the six possible 
     *  orderings.
     *  
     */
    Order order = new Order();

    /**
     *  Transformed class to associate white spaced delimited data to the 
     *  BioAssayDataCube
     *  
     */
    private DataInternal dataInternal;

    /**
     *  Transformed class to associate external data to the 
     *  BioAssayDataCube
     *  
     */
    private DataExternal dataExternal;

    /**
     *  Default constructor.
     *  
     */
    public BioDataCube() {
        super();
    }

    public BioDataCube(Attributes atts) {
        super(atts);
        {
            int nIndex = atts.getIndex("", "order");
            if (nIndex != -1) {
                order.setValueByName(atts.getValue(nIndex));
            }
        }
    }

    /**
     *  writeMAGEML
     *  
     *  This method is responsible for assembling the attribute and 
     *  association data into XML. It creates the object tag and then calls 
     *  the writeAttributes and writeAssociation methods.
     *  
     *  
     */
    public void writeMAGEML(Writer out) throws IOException {
        out.write("<BioDataCube");
        writeAttributes(out);
        out.write(">");
        writeAssociations(out);
        out.write("</BioDataCube>");
    }

    /**
     *  writeAttributes
     *  
     *  This method is responsible for assembling the attribute data into 
     *  XML. It calls the super method to write out all attributes of this 
     *  class and it's ancestors.
     *  
     *  
     */
    public void writeAttributes(Writer out) throws IOException {
        super.writeAttributes(out);
        out.write(" order=\"" + order + "\"");
    }

    /**
     *  writeAssociations
     *  
     *  This method is responsible for assembling the association data 
     *  into XML. It calls the super method to write out all associations of 
     *  this class's ancestors.
     *  
     *  
     */
    public void writeAssociations(Writer out) throws IOException {
        super.writeAssociations(out);
        if (dataInternal != null) {
            out.write("<DataInternal_assn>");
            dataInternal.writeMAGEML(out);
            out.write("</DataInternal_assn>");
        }
        if (dataExternal != null) {
            out.write("<DataExternal_assn>");
            dataExternal.writeMAGEML(out);
            out.write("</DataExternal_assn>");
        }
    }

    /**
     *  Set method for order
     *  
     *  @param value to set
     *  
     *  
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     *  Get method for order
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Order getOrder() {
        return order;
    }

    /**
* For Order set the Name of the Enumeration type by passing a value (int)
* @param int value The value to be mapped to the name */
    public String setNameByValueOrder(int val) {
        return getOrder().setNameByValue(val);
    }

    /**
* For Order set the Value of the Enumeration type by passing a Name to it.
* @param String name The name to be mapped to a value */
    public int setValueByNameOrder(String name) {
        return getOrder().setValueByName(name);
    }

    /**
* For Order get the Name of the Enumeration type by passing a Value to it.
* @param int val The value for which the Mapped String will be returned. */
    public String getNameByValueOrder(int val) {
        return getOrder().getNameByValue(val);
    }

    /**
* For Order get the Value of the Enumeration type by passing a Name to it.
* @param String name The name to be mapped to a value. */
    public int getValueByNameOrder(String name) {
        return getOrder().getValueByName(name);
    }

    /**
* Return the current name of the Enumeration type of Order.
*/
    public String getNameOrder() {
        return getOrder().getName();
    }

    /**
* Return the currrent value of the Enumeration type of Order.
*/
    public int getValueOrder() {
        return getOrder().getValue();
    }

    public String getModelClassName() {
        return new String("BioDataCube");
    }

    /**
     *  Set method for dataInternal
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDataInternal(DataInternal dataInternal) {
        this.dataInternal = dataInternal;
    }

    /**
     *  Get method for dataInternal
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public DataInternal getDataInternal() {
        return dataInternal;
    }

    /**
     *  Set method for dataExternal
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDataExternal(DataExternal dataExternal) {
        this.dataExternal = dataExternal;
    }

    /**
     *  Get method for dataExternal
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public DataExternal getDataExternal() {
        return dataExternal;
    }
}
