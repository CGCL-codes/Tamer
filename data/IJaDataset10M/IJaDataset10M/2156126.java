package com.android.im.plugin;

import java.util.Map;

/**
 * The methods used to map presence value sent in protocol to predefined
 * presence status.
 */
public interface PresenceMapping {

    /**
     * Tells if the mapping needs all presence values sent in protocol. If this
     * method returns true, the framework will pass all the presence values
     * received from the server when map to the predefined status.
     *
     * @return true if needs; false otherwise.
     */
    boolean requireAllPresenceValues();

    /**
     * Map the presence values sent in protocol to the predefined presence
     * status.
     *
     * @param onlineStatus The value of presence &lt;OnlineStatus&gt; received
     *            from the server.
     * @param userAvailability The value of presence &lt;UserAvailibility&gt;
     *            received from the server.
     * @param allValues The whole presence values received from the server.
     * @return a predefined status.
     * @see #requireAllPresenceValues()
     */
    int getPresenceStatus(boolean onlineStatus, String userAvailability, Map<String, Object> allValues);

    /**
     * Gets the value of &lt;OnlineStatus&gt; will be sent to the server when
     * update presence to the predefined status.
     *
     * @param status the predefined status.
     * @return The value of &lt;OnlineStatus&gt; will be sent to the server
     */
    boolean getOnlineStatus(int status);

    /**
     * Gets the value of &lt;UserAvaibility&gt; will be sent to the server when
     * update presence to the predefined status.
     *
     * @param status the predefined status.
     * @return The value of &lt;UserAvaibility&gt; will be sent to the server
     */
    String getUserAvaibility(int status);

    /**
     * Gets the extra presence values other than &lt;OnlineStatus&gt; and
     * &lt;UserAvaibility&gt; will be sent to the server when update presence to
     * the predefined status.
     *
     * @param status the predefined status.
     * @return The extra values that will be sent to the server.
     */
    Map<String, Object> getExtra(int status);

    /**
     * Gets an array of the supported presence status. The client can only update
     * presence to the values in the array.
     *
     * @return an array of the supported presence status.
     */
    int[] getSupportedPresenceStatus();
}
