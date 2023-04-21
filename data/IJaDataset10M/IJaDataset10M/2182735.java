package org.codavaj.type;

/**
 * DOCUMENT ME!
 */
public class Parameter extends Modifiable {

    private String name;

    private String type;

    private String typeArgumentList;

    private boolean array;

    private int degree;

    /**
     * Creates a new Parameter object.
     */
    public Parameter() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isArray() {
        return array;
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDegree() {
        return degree;
    }

    /**
     * DOCUMENT ME!
     *
     * @param degree DOCUMENT ME!
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    /**
	 * @return the typeArgumentList
	 */
    public String getTypeArgumentList() {
        return typeArgumentList;
    }

    /**
	 * @param typeArgumentList the typeArgumentList to set
	 */
    public void setTypeArgumentList(String typeArgumentList) {
        this.typeArgumentList = typeArgumentList;
    }
}
