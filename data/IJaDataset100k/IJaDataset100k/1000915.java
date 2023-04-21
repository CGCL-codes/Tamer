package ca.cbc.sportwire.dochandler;

import org.jdom.Document;
import org.jdom.DocType;
import javax.xml.transform.Transformer;
import ca.cbc.sportwire.WireFeederProperties;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Category;

/**
 * <p><code>ToNewsMLFilter</code> is used by the DocQueue to
 * pre-process non-NewsML items into NewsML by searching for a XSL
 * transform matching the system ID.  Transformed documents are then
 * replaced on the queue (as JDOM objects) and set for processing with
 * a secondary DocHandler.</p>
 *
 * NOTE: there is only one filter object shared by all threads.
 *
 * <p>Created: Wed Nov 14 15:04:36 2001</p>
 *
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: ToNewsMLFilter.java,v 1.19 2006-02-28 15:45:33 garym Exp $
 */
public class ToNewsMLFilter extends XSLFilter {

    /**
	 * Set up a reporting category in Log4J
	 */
    static Category cat = Category.getInstance(ToNewsMLFilter.class.getName());

    private DocHandler _instance = null;

    public DocHandler getInstance(ExtendedProperties config) {
        if (_instance == null) {
            _instance = (DocHandler) new ToNewsMLFilter(config);
        }
        return _instance;
    }

    /**
	 * <code>ToNewsMLFilter</code> constructor stores the config so
	 * we can get at connection details later
	 *
	 * @param config an <code>ExtendedProperties</code> value
	 */
    public ToNewsMLFilter(ExtendedProperties config) {
        super(config);
        setXslfile(config.getString(TSN_XSL_FILENAME_PROPERTY, DEFAULT_TSN_XSL_FILENAME));
    }

    protected String setDocParameter(Transformer xslt, Document dom, String doctag) {
        DocType dtd = dom.getDocType();
        if (dtd != null && dtd.getSystemID() != null) {
            cat.debug("PublicID = " + dtd.getPublicID() + " : SystemID = " + dtd.getSystemID());
        } else {
            cat.error("cannot read DocType/SystemID from " + doctag);
            return null;
        }
        String sysid = dtd.getSystemID();
        if (sysid != null && sysid.endsWith(".dtd")) {
            int endpath = sysid.lastIndexOf('/');
            sysid = sysid.substring(((endpath >= 0) ? endpath + 1 : 0), sysid.lastIndexOf(".dtd"));
        } else {
            cat.warn("SysID does not end in .dtd: " + sysid);
        }
        cat.debug("Set param " + XSL_SYSID_PARAM_PROPERTY + " = " + sysid);
        xslt.setParameter(XSL_SYSID_PARAM_PROPERTY, sysid);
        return sysid;
    }
}
