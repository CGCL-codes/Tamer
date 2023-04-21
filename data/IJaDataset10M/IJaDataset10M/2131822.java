package uk.ac.rothamsted.ovtk.Graph;

import java.io.Serializable;

/**
 * @author baumbac
 * 
 */
public class Attribute_Name implements Serializable {

    /**
	 * 
	 * @uml.property name="datatype" multiplicity="(0 1)"
	 */
    private String datatype;

    /**
	 * 
	 * @uml.property name="id" multiplicity="(0 1)"
	 */
    private String id;

    /**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
    private String name;

    /**
	 * 
	 * @uml.property name="unit" multiplicity="(0 1)"
	 */
    private String unit;

    /**
	 *  
	 */
    public Attribute_Name() {
        super();
    }

    /**
	 * @return Returns the datatype.
	 */
    public String getDatatype() {
        return datatype;
    }

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the unit.
	 */
    public String getUnit() {
        return unit;
    }

    /**
	 * @param datatype
	 *            The datatype to set.
	 */
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param unit
	 *            The unit to set.
	 */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
