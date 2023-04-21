package org.wahlzeit.handlers;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.webparts.WebPart;

/**
 * 
 * @author driehle
 *
 */
public class ShowInfoPageHandler extends AbstractWebPageHandler {

    /**
	 * 
	 */
    protected String infoTmplName = null;

    /**
	 *
	 */
    public ShowInfoPageHandler(AccessRights myRights, String myInfoTmplName) {
        initialize(PartUtil.SHOW_INFO_PAGE_FILE, myRights);
        infoTmplName = myInfoTmplName;
    }

    /**
	 * 
	 */
    protected void makeWebPageBody(UserSession ctx, WebPart page) {
        page.addWritable("info", createWebPart(ctx, infoTmplName));
    }
}
