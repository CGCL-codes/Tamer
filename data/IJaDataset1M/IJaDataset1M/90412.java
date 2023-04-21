package com.skillworld.webapp.web.pages.rest.item;

import java.util.Locale;
import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import com.skillworld.webapp.model.bag.ItemRecord;
import com.skillworld.webapp.model.itemservice.BagFullException;
import com.skillworld.webapp.model.itemservice.ItemService;
import com.skillworld.webapp.model.itemservice.NoLevelException;
import com.skillworld.webapp.web.services.I18n;
import com.skillworld.webapp.web.util.ErrorMessages;
import com.skillworld.webapp.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("text/xml")
public class BuyItem {

    @SuppressWarnings("unused")
    @Property
    private String errorMessage = null;

    @SuppressWarnings("unused")
    @Property
    private ItemRecord entry;

    @SuppressWarnings("unused")
    @Property
    private Locale locale;

    @Inject
    private ItemService itemService;

    @InjectService("I18n")
    private I18n i18n;

    @SessionState(create = false)
    private UserSession userSession;

    private boolean userSessionExists;

    @Inject
    private Request request;

    void onPassivate() {
        return;
    }

    void onActivate() {
        if (!userSessionExists) {
            errorMessage = ErrorMessages.NOT_LOGGED_IN;
            return;
        }
        String itemParam = request.getParameter("item");
        String langParam = request.getParameter("lang");
        if (itemParam == null) {
            errorMessage = ErrorMessages.INVALID_ARGUMENTS;
            return;
        }
        try {
            long itemId = Long.parseLong(itemParam);
            locale = i18n.selectLocale(langParam);
            entry = itemService.buyItem(this.userSession.getUserId(), itemId);
        } catch (InstanceNotFoundException e) {
            this.errorMessage = ErrorMessages.ITEM_NOT_FOUND;
        } catch (BagFullException e) {
            this.errorMessage = ErrorMessages.BAG_IS_FULL;
        } catch (NoLevelException e) {
            this.errorMessage = ErrorMessages.NO_LEVEL_ENOUGTH;
        }
    }
}
