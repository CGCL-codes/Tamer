package com.dcom.remote.wbem;

import com.dcom.exception.AutomationException;
import org.jinterop.dcom.impls.automation.IJIEnumVariant;

public interface ISWbemNamedValueSet extends ISWbem {

    /**
     * get_NewEnum.
     *
     * @return An enumeration.
     * @throws AutomationException If the remote server throws an exception.
     */
    public IJIEnumVariant get_NewEnum() throws AutomationException;

    /**
     * item. Get a named value from this Collection
     *
     * @param strName The strName (in)
     * @param iFlags  The iFlags (in, optional, pass 0 if not required)
     * @return An reference to a ISWbemNamedValue
     * @throws AutomationException If the remote server throws an exception.
     */
    public ISWbemNamedValue item(String strName, int iFlags) throws AutomationException;

    /**
     * getCount. The number of items in this collection
     *
     * @return The iCount
     * @throws AutomationException If the remote server throws an exception.
     */
    public int getCount() throws AutomationException;

    /**
     * add. Add a named value to this collection
     *
     * @param strName  The strName (in)
     * @param varValue A Variant (in)
     * @param iFlags   The iFlags (in, optional, pass 0 if not required)
     * @return An reference to a ISWbemNamedValue
     * @throws AutomationException If the remote server throws an exception.
     */
    public ISWbemNamedValue add(String strName, Object varValue, int iFlags) throws AutomationException;

    /**
     * remove. Remove a named value from this collection
     *
     * @param strName The strName (in)
     * @param iFlags  The iFlags (in, optional, pass 0 if not required)
     * @throws AutomationException If the remote server throws an exception.
     */
    public void remove(String strName, int iFlags) throws AutomationException;

    /**
     * zz_clone. Make a copy of this collection
     *
     * @return An reference to a ISWbemNamedValueSet
     * @throws AutomationException If the remote server throws an exception.
     */
    public ISWbemNamedValueSet zz_clone() throws AutomationException;

    /**
     * deleteAll. Delete all items in this collection
     *
     * @throws AutomationException If the remote server throws an exception.
     */
    public void deleteAll() throws AutomationException;
}
