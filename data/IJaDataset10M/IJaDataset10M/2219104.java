package org.mga.common.fields.postgres;

public class One2Many extends org.mga.common.fields.One2Many {

    public One2Many() {
        super();
    }

    /**
	 * @param name
	 * @param size
	 * @param label
	 * @param required
	 * @param relation
	 */
    public One2Many(String label, boolean required, String relation) {
        super(label, required, relation);
    }

    @Override
    public String getDBTypeName() {
        return "";
    }

    /**
	 * @return the javaType
	 */
    public String getJavaType() {
        return "int";
    }

    /**
	 * @return the type
	 */
    public Class getType() {
        return int.class;
    }
}
