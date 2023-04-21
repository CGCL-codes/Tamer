package org.mobilecontrol.syncML.message;

/**
 * This class represent a SyncML protocol command element 
 * allows the originator to notify the recipient
 */
public final class mcAlert extends mcSyncMLCommand {

    /** the SyncML tag */
    public static final String TAG = "Alert";

    /** the SyncML protocol alert codes */
    public static final int SERVER_INITIATED_MGMT = 1200;

    public static final int CLIENT_INITIATED_MGMT = 1201;

    public static final int NEXT_MESSAGE = 1222;

    public static final int SESSION_ABORT = 1223;

    public static final int CLIENT_EVENT = 1224;

    public static final int DISPLAY = 1100;

    public static final int CONTINUE_OR_ABOR = 1101;

    public static final int TEXT_INPUT = 1102;

    public static final int SINGLE_CHOICE = 1103;

    public static final int MULTIPLE_CHOICE = 1104;

    private mcNoResp noResp;

    private mcCred cred;

    private mcData data;

    private mcItem[] items;

    /**
     * Constructs a mcAlert object
     *
     * @param cmdID the command identifier
     * @param noResp the flag for response information (may be null)
     * @param cred the credentials information (may be null)
     * @param data the alert data (may be null)
     * @param items the parameters for alert command (may be null) 
     *
     * @throws mcMessageException if cmdID not available
     */
    public mcAlert(final mcCmdID cmdID, final mcNoResp noResp, final mcCred cred, final mcData data, final mcItem[] items) throws mcMessageException {
        super(cmdID);
        this.noResp = noResp;
        this.cred = cred;
        this.data = data;
        this.items = items;
    }

    /**
     * Get the name of the syncML element
     *
     * @return the name of the syncML element
     */
    public String getName() {
        return TAG;
    }

    /**
     * Get the object indicates if a response not required
     *
     * @return the no response object if available, otherwise null
     */
    public mcNoResp getNoResp() {
        return this.noResp;
    }

    /**
     * Get the autentication credential
     *
     * @return the autentication credential object if available, otherwise null
     */
    public mcCred getCred() {
        return this.cred;
    }

    /**
     * Get the data
     *
     * @return the data object if available, otherwise null
     */
    public mcData getData() {
        return this.data;
    }

    /**
     * Get the items
     *
     * @return the vector with items objects if available, otherwise null
     */
    public mcItem[] getItems() {
        return this.items;
    }

    /**
     * Get the XML string representation of the mcAlert object
     *
     * return the XML string representation of the mcAlert object
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("<" + mcAlert.TAG + ">\n");
        str.append(indent(this.cmdID.toString()));
        if (this.noResp != null) str.append(indent(this.noResp.toString()));
        if (this.cred != null) str.append(indent(this.cred.toString()));
        if (this.data != null) str.append(indent(this.data.toString()));
        if (this.items != null) {
            for (int i = 0; i < this.items.length; i++) str.append(indent(this.items[i].toString()));
        }
        str.append("</" + mcAlert.TAG + ">\n");
        return str.toString();
    }
}
