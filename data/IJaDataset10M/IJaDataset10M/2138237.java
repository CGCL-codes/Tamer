package org.ogce.schemas.gfac.wsdl;

public class ParamValueObj {

    private String[] values = null;

    private String[] descriptions = null;

    /**
     * Sets Descriptions
     *
     * @param    Descriptions        a  String[]
     */
    public void setDescriptions(String[] descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns Descriptions
     *
     * @return    a  String[]
     */
    public String[] getDescriptions() {
        return descriptions;
    }

    /**
     * Sets Values
     *
     * @param    Values              a  String[]
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * Returns Values
     *
     * @return    a  String[]
     */
    public String[] getValues() {
        return values;
    }
}
