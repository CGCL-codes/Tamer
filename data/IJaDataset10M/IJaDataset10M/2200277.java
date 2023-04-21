package jreceiver.client.mgr.struts;

import java.net.URL;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jreceiver.client.common.struts.JRecEditForm;
import jreceiver.common.JRecException;
import jreceiver.common.rec.security.User;

/**
 * Form bean to add/edit a single site record.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/09/24 20:51:36 $
 */
public final class SiteEditForm extends JRecEditForm {

    public int getSiteId() {
        return m_site_id;
    }

    public void setSiteId(int site_id) {
        m_site_id = site_id;
    }

    public String getName() {
        return m_name != null ? m_name : "";
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getRpcUrlStr() {
        return m_rpc_url != null ? m_rpc_url.toExternalForm() : "";
    }

    public void setRpcUrlStr(String s_url) throws MalformedURLException {
        if (s_url != null && s_url.trim().length() > 0) m_rpc_url = new URL(s_url); else m_rpc_url = null;
    }

    public String getContentSystemID() {
        return m_content_url != null ? m_content_url.toExternalForm() : "";
    }

    public void setContentSystemID(String s_url) throws MalformedURLException {
        if (s_url != null && s_url.trim().length() > 0) m_content_url = new URL(s_url); else m_content_url = null;
    }

    public URL getRpcURL() {
        return m_rpc_url;
    }

    public void setRpcURL(URL rpc_url) {
        m_rpc_url = rpc_url;
    }

    public URL getContentURL() {
        return m_content_url;
    }

    public void setContentURL(URL content_url) {
        m_content_url = content_url;
    }

    /**
    * field validation
    */
    public void onValidate(User user, HttpServletRequest request, ActionErrors errors) throws JRecException {
    }

    /** */
    protected int m_site_id = 0;

    /** */
    protected String m_name = null;

    /** */
    protected URL m_rpc_url = null;

    /** */
    protected URL m_content_url = null;

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(SiteEditForm.class);
}
