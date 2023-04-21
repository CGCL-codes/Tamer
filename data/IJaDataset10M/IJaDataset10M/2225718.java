package org.wahlzeit.handlers;

import java.net.*;
import java.util.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author driehle
 *
 */
public abstract class AbstractWebPartHandler implements WebPartHandler {

    /**
	 * 
	 */
    protected String tmplName;

    /**
	 * 
	 */
    protected AccessRights neededRights;

    /**
	 * 
	 */
    protected void initialize(String myTmplName, AccessRights myRights) {
        tmplName = myTmplName;
        neededRights = myRights;
    }

    /**
	 * 
	 */
    protected final WebPart createWebPart(UserSession ctx) {
        return createWebPart(ctx, tmplName);
    }

    /**
	 * 
	 */
    protected final WebPart createWebPart(UserSession ctx, String name) {
        WebPartTemplateServer wpts = WebPartTemplateServer.getInstance();
        WebPartTemplate tmpl = wpts.getTemplate(ctx.cfg().getLanguageCode(), name);
        return new WebPart(tmpl);
    }

    /**
	 * 
	 */
    protected String getPhotoThumb(UserSession ctx, Photo photo) {
        String result = null;
        if (photo != null) {
            String imageLink = getPhotoLink(photo, PhotoSize.THUMB);
            result = HtmlUtil.asImg(imageLink, photo.getThumbWidth(), photo.getThumbHeight());
        } else {
            Language langValue = ctx.cfg().getLanguage();
            result = HtmlUtil.asImg(SysConfig.getEmptyImageAsUrlString(langValue));
        }
        return result;
    }

    /**
	 * 
	 */
    protected String getPhotoLink(Photo photo, PhotoSize size) {
        return SysConfig.getPhotosUrlAsString() + photo.getId().asString() + size.asInt() + ".jpg";
    }

    /**
	 * 
	 */
    protected String getPhotoSummary(UserSession ctx, Photo photo) {
        return photo.getSummary(ctx.cfg());
    }

    /**
	 * 
	 */
    protected String getPhotoCaption(UserSession ctx, Photo photo) {
        return photo.getCaption(ctx.cfg());
    }

    /**
	 * 
	 */
    protected final WebFormHandler getFormHandler(String name) {
        return WebPartHandlerManager.getWebFormHandler(name);
    }

    /**
	 * 
	 */
    public final AccessRights getNeededRights() {
        return neededRights;
    }

    /**
	 * 
	 */
    protected boolean hasAccessRights(UserSession ctx, Map args) {
        return ctx.getClient().hasRights(getNeededRights());
    }

    /**
	 * 
	 */
    protected boolean isWellFormedGet(UserSession ctx, String link, Map args) {
        return true;
    }

    /**
	 * 
	 */
    protected boolean hasSavedPhotoId(UserSession ctx) {
        String id = ctx.getAsString(ctx.getSavedArgs(), Photo.ID);
        return !StringUtil.isNullOrEmptyString(id);
    }

    /**
	 * 
	 */
    protected boolean isSavedPhotoVisible(UserSession ctx) {
        String id = ctx.getAsString(ctx.getSavedArgs(), Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);
        return photo.isVisible();
    }

    /**
	 * 
	 */
    protected boolean hasSavedMessage(UserSession ctx) {
        return !StringUtil.isNullOrEmptyString(ctx.getMessage());
    }

    /**
	 * 
	 */
    public final String handleGet(UserSession ctx, String link, Map args) {
        if (!hasAccessRights(ctx, args)) {
            SysLog.logInfo("insufficient rights for GET from: " + ctx.getEmailAddressAsString());
            return getIllegalAccessErrorPage(ctx);
        }
        if (!isWellFormedGet(ctx, link, args)) {
            SysLog.logInfo("received ill-formed GET from: " + ctx.getEmailAddressAsString());
            return getIllegalArgumentErrorPage(ctx);
        }
        try {
            return doHandleGet(ctx, link, args);
        } catch (Throwable t) {
            SysLog.logThrowable(t);
            return getInternalProcessingErrorPage(ctx);
        }
    }

    /**
	 * @param args TODO
	 * 
	 */
    protected String doHandleGet(UserSession ctx, String link, Map args) {
        return link;
    }

    /**
	 * 
	 */
    protected String getIllegalAccessErrorPage(UserSession ctx) {
        ctx.setHeading(ctx.cfg().getInformation());
        String msg1 = ctx.cfg().getIllegalAccessError();
        ctx.setMessage(msg1);
        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

    /**
	 * 
	 */
    protected String getIllegalArgumentErrorPage(UserSession ctx) {
        ctx.setHeading(ctx.cfg().getInformation());
        String msg1 = ctx.cfg().getIllegalArgumentError();
        String msg2 = ctx.cfg().getContinueWithShowPhoto();
        if (ctx.getClient() instanceof User) {
            msg2 = ctx.cfg().getContinueWithShowUserHome();
        }
        ctx.setTwoLineMessage(msg1, msg2);
        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

    /**
	 * 
	 */
    protected String getInternalProcessingErrorPage(UserSession ctx) {
        ctx.setHeading(ctx.cfg().getInformation());
        String msg1 = ctx.cfg().getInternalProcessingError();
        String msg2 = ctx.cfg().getContinueWithShowPhoto();
        if (ctx.getClient() instanceof User) {
            msg2 = ctx.cfg().getContinueWithShowUserHome();
        }
        ctx.setTwoLineMessage(msg1, msg2);
        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }
}
