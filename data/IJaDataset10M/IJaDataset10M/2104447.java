package org.opencms.workplace.commons;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsResourceFilter;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.workplace.explorer.CmsResourceUtil;
import org.opencms.workplace.list.A_CmsListExplorerDialog;
import org.opencms.workplace.list.A_CmsListResourceCollector;
import org.opencms.workplace.list.CmsListItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;

/**
 * Collector for resources with links that could get broken after publishing.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.5.5 
 */
public class CmsPublishBrokenRelationsCollector extends A_CmsListResourceCollector {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsPublishBrokenRelationsCollector.class);

    /** Parameter of the default collector name. */
    public static final String COLLECTOR_NAME = "potentialBrokenResources";

    /**
     * Constructor, creates a new instance.<p>
     * 
     * @param wp the workplace object
     * @param resources list of locked resources
     */
    public CmsPublishBrokenRelationsCollector(A_CmsListExplorerDialog wp, List resources) {
        super(wp);
        setResourcesParam(resources);
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCollectorNames()
     */
    public List getCollectorNames() {
        List names = new ArrayList();
        names.add(COLLECTOR_NAME);
        return names;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListResourceCollector#getResources(org.opencms.file.CmsObject, java.util.Map)
     */
    public List getResources(CmsObject cms, Map params) {
        String siteRoot = cms.getRequestContext().getSiteRoot();
        if (siteRoot == null) {
            siteRoot = "";
        }
        List resources = new ArrayList();
        try {
            cms.getRequestContext().setSiteRoot("");
            Iterator itResourceNames = getResourceNamesFromParam(params).iterator();
            while (itResourceNames.hasNext()) {
                String resName = (String) itResourceNames.next();
                try {
                    resources.add(cms.readResource(resName, CmsResourceFilter.ALL));
                } catch (CmsException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        } finally {
            cms.getRequestContext().setSiteRoot(siteRoot);
        }
        return resources;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListResourceCollector#setAdditionalColumns(org.opencms.workplace.list.CmsListItem, org.opencms.workplace.explorer.CmsResourceUtil)
     */
    protected void setAdditionalColumns(CmsListItem item, CmsResourceUtil resUtil) {
    }
}
