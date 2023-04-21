package org.opencms.workplace.tools.content;

import org.opencms.file.CmsObject;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsRole;
import org.opencms.workplace.tools.CmsOfflineToolHandler;

/**
 * Element content tool handler that hides the element tools if the current user
 * has not the needed privileges.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.2 $ 
 * 
 * @since 6.0.0 
 */
public class CmsContentToolHandler extends CmsOfflineToolHandler {

    /**
     * @see org.opencms.workplace.tools.A_CmsToolHandler#isEnabled(org.opencms.file.CmsObject)
     */
    public boolean isEnabled(CmsObject cms) {
        return super.isEnabled(cms) && (OpenCms.getRoleManager().hasRole(cms, CmsRole.DEVELOPER) || OpenCms.getRoleManager().hasRole(cms, CmsRole.WORKPLACE_MANAGER));
    }

    /**
     * @see org.opencms.workplace.tools.A_CmsToolHandler#isVisible(org.opencms.file.CmsObject)
     */
    public boolean isVisible(CmsObject cms) {
        return OpenCms.getRoleManager().hasRole(cms, CmsRole.DEVELOPER) || OpenCms.getRoleManager().hasRole(cms, CmsRole.WORKPLACE_MANAGER);
    }
}
