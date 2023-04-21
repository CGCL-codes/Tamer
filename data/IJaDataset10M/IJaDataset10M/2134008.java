package wsl.fw.msgserver;

import wsl.fw.util.Log;
import wsl.fw.util.Util;

public class MSExchangeInterface {

    /**
	 * get the address lists for a login
	 */
    public static native AddressList[] getAddressLists(String loginUrl) throws MessageServerException;

    /**
	 * Get the folders for an info store
	 */
    public native Folder[] getFolders(String loginUrl, String infoStoreId) throws MessageServerException;

    /**
	 * Get the info stores
	 */
    public native InfoStore[] getInfoStores(String loginUrl) throws MessageServerException;

    /**
	 *	Logout the user from the Exchange Server
	 */
    public static native void logout(String loginUrl) throws MessageServerException;

    /**
	 * Send a mail message
	 */
    public static native boolean sendMailMessage(String loginUrl, String sender, String recipient, String subject, String body) throws MessageServerException;

    /**
	 *	Forward a mail message and its attachements
	 */
    public static native void forwardMailMessage(String loginUrl, String folderId, String messageId, String sender, String recipient, String subject, String body) throws MessageServerException;

    /**
	 *	Delete a mail message
	 */
    public static native void deleteMailMessage(String loginUrl, String folderId, String messageId) throws MessageServerException;

    /**
	 *	Mark a mail message as read
	 */
    public static native void updateReadMailMessage(String loginUrl, String folderId, String messageId) throws MessageServerException;

    /**
	 * Iterator support
	 */
    public static native void createMessageIterator(String loginUrl, int itrId, String folderId, boolean forwardOrder) throws MessageServerException;

    public static native void createContactMsgIterator(String loginUrl, int itrId) throws MessageServerException;

    public static native void createApptMsgIterator(String loginUrl, String date, int itrId) throws MessageServerException;

    public static native Message getNextMessage(String loginUrl, int itrId) throws MessageServerException;

    public static native boolean hasNextMessage(String loginUrl, int itrId) throws MessageServerException;

    public static native void createSubfolderIterator(String loginUrl, int itrId, String folderId) throws MessageServerException;

    public static native void createContactSubfolderIterator(String loginUrl, int itrId) throws MessageServerException;

    public static native Folder getNextSubfolder(String loginUrl, int itrId) throws MessageServerException;

    public static native boolean hasNextSubfolder(String loginUrl, int itrId) throws MessageServerException;

    /**
	 * Specific Iterator support for Address lists
	 */
    public static native void createAddrIterator(String loginUrl, int itrId, String folderId) throws MessageServerException;

    public static native Contact getNextAddr(String loginUrl, int itrId) throws MessageServerException;

    public static native boolean hasNextAddr(String loginUrl, int itrId) throws MessageServerException;

    /**
	 * Build a login url
	 */
    public static String buildLoginUrl(String sessionId, MessageServer ms, String user, String password) {
        return Util.noNullStr(sessionId + ";" + ms.getServerName()) + ";" + Util.noNullStr(user) + ";" + Util.noNullStr(password) + ";" + (ms.getType() == null ? MsExchangeMsgServer.MST_MSEX2000 : ms.getType()) + ";" + Util.noNullStr(ms.getPropTags()) + ";";
    }
}
