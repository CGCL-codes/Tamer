package wmi;

import com4j.*;

/**
 * A collection of Properties
 */
@IID("{DEA0A7B2-D4BA-11D1-8B09-00600806D9B6}")
public interface ISWbemPropertySet extends Com4jObject, Iterable<Com4jObject> {

    /**
     * <p>
     * Getter method for the COM property "_NewEnum"
     * </p>
     */
    @DISPID(-4)
    @VTID(7)
    java.util.Iterator<Com4jObject> iterator();

    /**
     * <p>
     * Get a named Property from this collection
     * </p>
     * 
     * @param strName
     *                Mandatory java.lang.String parameter.
     * @param iFlags
     *                Optional parameter. Default value is 0
     * @return Returns a value of type wmi.ISWbemProperty
     */
    @DISPID(0)
    @VTID(8)
    @DefaultMethod
    wmi.ISWbemProperty item(java.lang.String strName, @Optional @DefaultValue("0") int iFlags);

    /**
     * <p>
     * The number of items in this collection
     * </p>
     * <p>
     * Getter method for the COM property "Count"
     * </p>
     * 
     * @return Returns a value of type int
     */
    @DISPID(1)
    @VTID(9)
    int count();

    /**
     * <p>
     * Add a Property to this collection
     * </p>
     * 
     * @param strName
     *                Mandatory java.lang.String parameter.
     * @param iCimType
     *                Mandatory wmi.WbemCimtypeEnum parameter.
     * @param bIsArray
     *                Optional parameter. Default value is false
     * @param iFlags
     *                Optional parameter. Default value is 0
     * @return Returns a value of type wmi.ISWbemProperty
     */
    @DISPID(2)
    @VTID(10)
    wmi.ISWbemProperty add(java.lang.String strName, wmi.WbemCimtypeEnum iCimType, @Optional @DefaultValue("0") boolean bIsArray, @Optional @DefaultValue("0") int iFlags);

    /**
     * <p>
     * Remove a Property from this collection
     * </p>
     * 
     * @param strName
     *                Mandatory java.lang.String parameter.
     * @param iFlags
     *                Optional parameter. Default value is 0
     */
    @DISPID(3)
    @VTID(11)
    void remove(java.lang.String strName, @Optional @DefaultValue("0") int iFlags);
}
