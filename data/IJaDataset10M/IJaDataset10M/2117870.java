package org.wahlzeit.handlers;

import java.util.*;
import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

/**
 * 
 * @author driehle
 *
 */
public class ShowNotePageHandler extends AbstractWebPageHandler {

    /**
	 *
	 */
    public ShowNotePageHandler() {
        initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
    }

    /**
	 * 
	 */
    protected boolean isWellFormedGet(UserSession ctx, String link, Map args) {
        return hasSavedMessage(ctx);
    }

    /**
	 * 
	 */
    protected void makeWebPageBody(UserSession ctx, WebPart page) {
        String heading = ctx.getHeading();
        heading = StringUtil.isNullOrEmptyString(heading) ? ctx.cfg().getThankYou() : heading;
        page.addString("noteHeading", heading);
        page.addString("note", ctx.getMessage());
    }
}
