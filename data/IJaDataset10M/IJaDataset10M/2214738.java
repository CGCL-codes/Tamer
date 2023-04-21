package org.kablink.teaming.gwt.client;

import org.kablink.teaming.gwt.client.rpc.shared.VibeRpcResponseData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class is used in rpc calls and represents a serializable File class.
 * @author jwootton
 *
 */
public class GwtAttachment implements IsSerializable, VibeRpcResponseData {

    private String m_fileName;

    private String m_viewUrl;

    /**
	 * 
	 */
    public GwtAttachment() {
        m_fileName = null;
        m_viewUrl = null;
    }

    /**
	 * 
	 */
    public String getFileName() {
        return m_fileName;
    }

    /**
	 * Return the url that can be used to view this file.
	 */
    public String getViewFileUrl() {
        return m_viewUrl;
    }

    /**
	 * 
	 */
    public void setFileName(String fileName) {
        m_fileName = fileName;
    }

    /**
	 * Set the url that can be used to view this file.
	 */
    public void setViewFileUrl(String url) {
        m_viewUrl = url;
    }
}
