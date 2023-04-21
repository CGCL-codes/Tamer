package org.gvsig.fmap.drivers.gpe.model;

import java.util.LinkedHashMap;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPEFeature {

    private static int idFeature = 0;

    private GPEGeometry geometry = null;

    private LinkedHashMap elements = null;

    private Value id = null;

    private String name = null;

    private String typeName = null;

    public GPEFeature(Value id, String name, String typeName) {
        this();
        if ((id == null) || (id.toString() == null)) {
            this.id = ValueFactory.createValue(String.valueOf(idFeature));
            idFeature++;
        } else {
            this.id = id;
        }
        this.name = name;
        this.typeName = typeName;
    }

    public GPEFeature() {
        elements = new LinkedHashMap();
    }

    /**
	 * @return the id
	 */
    public Value getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Value id) {
        this.id = id;
    }

    /**
	 * @return the geometry
	 */
    public GPEGeometry getGeometry() {
        return geometry;
    }

    /**
	 * @param geometry the geometry to set
	 */
    public void setGeometry(GPEGeometry geometry) {
        this.geometry = geometry;
    }

    /**
	 * @return the elements
	 */
    public LinkedHashMap getelements() {
        return elements;
    }

    /**
	 * @param elements the elements to set
	 */
    public void setElements(LinkedHashMap elements) {
        this.elements = elements;
    }

    /**
	 * It adds a new element
	 * @param element
	 * The element to add
	 */
    public void addElement(GPEElement element) {
        elements.put(element.getName(), element);
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the typeName
	 */
    public String getTypeName() {
        return typeName;
    }

    /**
	 * Initialize the feature id
	 */
    public static void initIdFeature() {
        idFeature = 0;
    }
}
