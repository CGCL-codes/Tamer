package org.opencms.workplace.explorer.menu;

import org.opencms.file.CmsObject;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.explorer.CmsResourceUtil;

/**
 * Defines a menu item rule that sets the visibility to invisible if the current resource is unlocked.<p>
 * 
 * @author Andreas Zahner  
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.5.6
 */
public class CmsMirPrSameUnlockedInvisible extends A_CmsMenuItemRule {

    /**
     * @see org.opencms.workplace.explorer.menu.I_CmsMenuItemRule#getVisibility(org.opencms.file.CmsObject, CmsResourceUtil[])
     */
    public CmsMenuItemVisibilityMode getVisibility(CmsObject cms, CmsResourceUtil[] resourceUtil) {
        return CmsMenuItemVisibilityMode.VISIBILITY_INVISIBLE;
    }

    /**
     * @see org.opencms.workplace.explorer.menu.I_CmsMenuItemRule#matches(org.opencms.file.CmsObject, CmsResourceUtil[])
     */
    public boolean matches(CmsObject cms, CmsResourceUtil[] resourceUtil) {
        if (resourceUtil[0].isInsideProject()) {
            return (!resourceUtil[0].getProjectState().isLockedForPublishing()) && (CmsStringUtil.isEmptyOrWhitespaceOnly(resourceUtil[0].getLockedByName()));
        }
        return false;
    }
}
