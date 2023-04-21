package de.ibk.ods.atf.helper;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class ApplRelHelper {

    /**
	 * 
	 */
    private String name = "";

    /**
	 * 
	 */
    private String ref_to = "";

    /**
	 * 
	 */
    private String base_relation = "";

    /**
	 * 
	 */
    private long min_occurs = -1;

    /**
	 * 
	 */
    private long max_occurs = -1;

    /**
	 * 
	 */
    private String inverse_name = "";

    /**
	 * @return Returns the base_relation.
	 */
    public String getBase_relation() {
        return base_relation;
    }

    /**
	 * @param base_relation The base_relation to set.
	 */
    public void setBase_relation(String base_relation) {
        this.base_relation = base_relation;
    }

    /**
	 * @return Returns the inverse_name.
	 */
    public String getInverse_name() {
        return inverse_name;
    }

    /**
	 * @param inverse_name The inverse_name to set.
	 */
    public void setInverse_name(String inverse_name) {
        this.inverse_name = inverse_name;
    }

    /**
	 * @return Returns the max_occurs.
	 */
    public long getMax_occurs() {
        return max_occurs;
    }

    /**
	 * @param max_occurs The max_occurs to set.
	 */
    public void setMax_occurs(long max_occurs) {
        this.max_occurs = max_occurs;
    }

    /**
	 * @return Returns the min_occurs.
	 */
    public long getMin_occurs() {
        return min_occurs;
    }

    /**
	 * @param min_occurs The min_occurs to set.
	 */
    public void setMin_occurs(long min_occurs) {
        this.min_occurs = min_occurs;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the ref_to.
	 */
    public String getRef_to() {
        return ref_to;
    }

    /**
	 * @param ref_to The ref_to to set.
	 */
    public void setRef_to(String ref_to) {
        this.ref_to = ref_to;
    }
}
