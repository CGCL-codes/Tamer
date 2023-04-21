package openvend.servlet;

/**
 * Contains keys to get/set request attributes.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public class OvRequestAttributes {

    /** Transient request attribute: portlet phase bridge data. */
    public static final String TRANSIENT_REQUEST_ATTRIB_PORTLET_LIFECYCLE_DATA = "portlet-lifecycle-data";

    /** Permanent request attribute: FOP factory. */
    public static final String PERMANENT_REQUEST_ATTRIB_FOW_FACTORY = "fop-facory";

    private OvRequestAttributes() {
    }
}
