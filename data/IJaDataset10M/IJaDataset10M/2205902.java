package edu.psu.its.lionshare.share.gnutella;

import com.limegroup.gnutella.xml.LimeXMLDocument;
import com.limegroup.gnutella.RemoteFileDesc;
import java.util.List;
import java.util.Set;
import edu.psu.its.lionshare.database.UserData;

public class LionShareRemoteFileDesc extends RemoteFileDesc {

    private transient List metadata = null;

    private String base64_preview = null;

    private String m_sUserName = null;

    private List security_attributes = null;

    private List chat_names = null;

    private UserData m_userData = null;

    public LionShareRemoteFileDesc(String host, int port, long index, String filename, int size, byte[] clientGUID, int speed, boolean chat, int quality, boolean browseHost, LimeXMLDocument xmlDoc, Set urns, boolean replyToMulticast, boolean firewalled, String vendor, long timestamp, Set proxies, long createTime, int fwtsupported, List metadata, String preview, String sUserName, List security_attr, UserData userData, List chat_names) {
        super(host, port, index, filename, size, clientGUID, speed, chat, quality, browseHost, null, urns, replyToMulticast, firewalled, vendor, timestamp, proxies, createTime, fwtsupported);
        this.metadata = metadata;
        this.base64_preview = preview;
        m_sUserName = sUserName;
        this.security_attributes = security_attr;
        this.chat_names = chat_names;
        m_userData = userData;
    }

    public List getChatNamesAsList() {
        return chat_names;
    }

    public List getMetadataAsList() {
        return metadata;
    }

    public String getBase64Preview() {
        return this.base64_preview;
    }

    /**
   *
   * This method returns a List<String> of authz user attributes require
   * in order to have access to the content that this RFD describes.
   *
   * @return List<String> - A list of attributes, could return null if none
   *                        are required for access to this file.
   *
   */
    public List getAuthzAttributesAsList() {
        return this.security_attributes;
    }

    /**
   * Returns the user name associated with this remote file desc.
   * @return the user name string. 
   */
    public String getUserName() {
        if (m_sUserName != null) {
            return new String(m_sUserName);
        } else {
            return "UNKNOWN";
        }
    }

    public UserData getUserProfile() {
        return m_userData;
    }
}
