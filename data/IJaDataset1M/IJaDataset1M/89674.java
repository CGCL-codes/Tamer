package com.dcom.remote.wbem;

import com.dcom.exception.AutomationException;

public interface ISWbemPrivilege extends ISWbem {

    /**
     * isEnabled. Whether the Privilege is to be enabled or disabled
     *
     * @return The bIsEnabled
     * @throws AutomationException If the remote server throws an exception.
     */
    public boolean isEnabled() throws AutomationException;

    /**
     * setIsEnabled. Whether the Privilege is to be enabled or disabled
     *
     * @param bIsEnabled The bIsEnabled (in)
     * @throws AutomationException If the remote server throws an exception.
     */
    public void setIsEnabled(boolean bIsEnabled) throws AutomationException;

    /**
     * getName. The name of the Privilege
     *
     * @return The strDisplayName
     * @throws AutomationException If the remote server throws an exception.
     */
    public String getName() throws AutomationException;

    /**
     * getDisplayName. The display name of the Privilege
     *
     * @return The strDisplayName
     * @throws AutomationException If the remote server throws an exception.
     */
    public String getDisplayName() throws AutomationException;

    /**
     * getIdentifier. The Privilege identifier
     *
     * @return A com.dcom.remote.wbemdisp.WbemPrivilegeEnum constant
     * @throws AutomationException If the remote server throws an exception.
     */
    public int getIdentifier() throws AutomationException;
}
