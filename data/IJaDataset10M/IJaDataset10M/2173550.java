package org.compiere.cm.cache;

import org.compiere.model.MTemplate;

/**
 *  Template
 *  we create a cacheObject for the template.
 *
 *  @author Yves Sandfort
 *  @version  $Id$
 */
public class Template extends CO {

    /**
	 * get Template by ID
	 * @param ID
	 * @param CM_WebProject_ID
	 * @return MTemplate
	 */
    public MTemplate getCM_Template(int ID, int CM_WebProject_ID) {
        return getCM_Template("" + ID, CM_WebProject_ID);
    }

    /**
	 * get Template by ID
	 * @param ID
	 * @param CM_WebProject_ID
	 * @return MTemplate
	 */
    public MTemplate getCM_Template(String ID, int CM_WebProject_ID) {
        if (cache.containsKey(ID)) {
            use(ID);
            return (MTemplate) cache.get(ID);
        } else {
            int[] tableKeys = MTemplate.getAllIDs("CM_Template", "CM_Template_ID=" + ID + " AND CM_WebProject_ID=" + CM_WebProject_ID, "WebCM");
            if (tableKeys.length == 0) {
                return null;
            } else if (tableKeys.length == 1) {
                MTemplate thisTemplate = new MTemplate(ctx, tableKeys[0], "WebCM");
                thisTemplate.getPreBuildTemplate();
                put("" + thisTemplate.getCM_Template_ID(), thisTemplate);
                return thisTemplate;
            } else {
                return null;
            }
        }
    }
}
