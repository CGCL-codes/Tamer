package jadacommon.model.attribute;

/**
 *
 * @author Cris
 */
public interface Attribute {

    /**
     * Get the univoche ID of this attribute
     * @return 
     */
    public int getID();

    /**
     * Get the name of this attribute
     * @return 
     */
    public String getName();

    /**
     * Get the Type of this attribute 
     * @see AttributeController#CHOICEATTRIBUTE 
     * @see AttributeController#CONSUMABLESTAT
     * @see AttributeController#STATISTIC
     * @return A string tha rappresent a specific type
     */
    public String getType();

    /**
     * The method to know if an attribute is viisble or not
     * 
     * @return treu if is visibile false otherwise
     */
    public boolean isVisible();

    /**
     * Set if an attribute is visible or not
     * @param visibile 
     */
    public void setVisibile(boolean visibile);

    /**
     * Get the current value of attribute
     * @return the value
     */
    public int getValue();

    /**
     * Set the intial value of attribute
     * @param _value 
     */
    public void setValue(int _value);
}
