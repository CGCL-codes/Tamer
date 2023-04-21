package org.easyrec.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.easyrec.soap.nodomain.exception.EasyRecSoapException;
import org.springframework.web.servlet.ModelAndView;
import org.easyrec.model.web.Item;
import org.easyrec.model.web.Message;
import org.easyrec.model.web.RemoteTenant;
import org.easyrec.model.web.Session;
import org.easyrec.service.web.nodomain.ShopRecommenderService;
import org.easyrec.store.dao.web.ItemDAO;
import org.easyrec.store.dao.web.OperatorDAO;
import org.easyrec.store.dao.web.RemoteTenantDAO;
import org.easyrec.utils.MyUtils;
import org.easyrec.utils.io.Text;
import org.easyrec.utils.servlet.ServletUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * Get Recommendations for a user and a base item.
 * The recommendations are returned as a javascript snippet, which can be
 * directly included on the webpage via ajax or as a customizeable widget
 *
 *  
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: phlavac $<br/> $Date: 2008-07-21
 * 17:32:41 +0200 (Mo, 21 Jul 2008) $<br/> $Revision: 15901 $
 * </p>
 * 
 * @author phlavac
 * @version <CURRENT PROJECT VERSION>
 * @since <PROJECT VERSION ON FILE CREATION>
 */
public class RecommenderController extends MultiActionController {

    private ShopRecommenderService shopRecommenderService;

    private ItemDAO itemDAO;

    private OperatorDAO operatorDAO;

    private RemoteTenantDAO remoteTenantDAO;

    public void setShopRecommenderService(ShopRecommenderService shopRecommenderService) {
        this.shopRecommenderService = shopRecommenderService;
    }

    public void setRemoteTenantDAO(RemoteTenantDAO remoteTenantDAO) {
        this.remoteTenantDAO = remoteTenantDAO;
    }

    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public void setOperatorDAO(OperatorDAO operatorDAO) {
        this.operatorDAO = operatorDAO;
    }

    private ModelAndView security(HttpServletRequest request, String actionType) {
        ModelAndView mav = new ModelAndView("page");
        List<Message> messages = new ArrayList<Message>();
        String sessionId = ServletUtils.getSafeParameterDecoded(request, "sessionid", "");
        Session session = new Session(sessionId, request);
        String apiKey = ServletUtils.getSafeParameter(request, "apikey", "");
        String tenantId = ServletUtils.getSafeParameterDecoded(request, "tenantid", "");
        String userId = ServletUtils.getSafeParameterDecoded(request, "userid", "");
        String itemId = ServletUtils.getSafeParameterDecoded(request, "itemid", "");
        String widgetId = ServletUtils.getSafeParameterDecoded(request, "widgetid", "");
        session.setSessionId(sessionId);
        Integer coreTenantId = operatorDAO.getTenantId(apiKey, tenantId);
        if (coreTenantId != null) {
            RemoteTenant remoteTenant = remoteTenantDAO.get(coreTenantId);
            String tenantStringId = tenantId;
            Item baseItem = itemDAO.get(remoteTenant, itemId, Item.DEFAULT_STRING_ITEM_TYPE);
            if (baseItem != null && baseItem.isActive()) {
                logger.debug(new StringBuilder(tenantId).append(": ").append((!userId.equals("") ? userId : "anonymous")).append(" requesting similar ").append(actionType).append(" Items for ").append(baseItem.getDescription()).append(" (id:").append(itemId).append(")"));
                List<Item> items = null;
                userId = Text.isEmpty(userId) ? "-1" : userId;
                if (!Text.isEmpty(widgetId)) {
                    mav.setViewName("../tenant/" + tenantStringId + "/" + widgetId);
                } else {
                    if (ShopRecommenderService.ACTION_OTHER_USERS_ALSO_VIEWED.equals(actionType)) {
                        try {
                            items = shopRecommenderService.alsoViewedItems(coreTenantId, userId, itemId, Item.DEFAULT_STRING_ITEM_TYPE, Item.DEFAULT_STRING_ITEM_TYPE, session).getRecommendedItems();
                        } catch (EasyRecSoapException ex) {
                            Logger.getLogger(RecommenderController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (ShopRecommenderService.ACTION_OTHER_USERS_ALSO_BOUGHT.equals(actionType)) {
                        try {
                            items = shopRecommenderService.alsoBoughtItems(coreTenantId, userId, itemId, Item.DEFAULT_STRING_ITEM_TYPE, Item.DEFAULT_STRING_ITEM_TYPE, session).getRecommendedItems();
                        } catch (EasyRecSoapException ex) {
                            Logger.getLogger(RecommenderController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (ShopRecommenderService.ACTION_ITEMS_RATED_GOOD_BY_OTHER_USERS.equals(actionType)) {
                        try {
                            items = shopRecommenderService.alsoGoodRatedItems(coreTenantId, userId, itemId, Item.DEFAULT_STRING_ITEM_TYPE, Item.DEFAULT_STRING_ITEM_TYPE, session).getRecommendedItems();
                        } catch (EasyRecSoapException ex) {
                            Logger.getLogger(RecommenderController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    mav.setViewName("jsRecommender/viewSimilarItems");
                }
                mav.addObject("action", actionType);
                mav.addObject("itemCount", MyUtils.sizeOf(items));
                mav.addObject("items", items);
                mav.addObject("tenantId", tenantStringId);
                mav.addObject("apikey", apiKey);
                mav.addObject("item", baseItem);
                mav.addObject("userId", userId);
                mav.addObject("sessionId", sessionId);
                return mav;
            } else {
                messages.add(Message.ITEM_NOT_EXISTS);
            }
        } else {
            messages.add(Message.TENANT_WRONG_TENANT_APIKEY);
        }
        mav.setViewName("flot/dataOutput");
        return mav;
    }

    public ModelAndView otherusersalsoviewed(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return security(request, "otherUsersAlsoViewed");
    }

    public ModelAndView otherusersalsobought(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return security(request, "otherUsersAlsoBought");
    }

    public ModelAndView itemsratedgoodbyotherusers(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return security(request, "itemsRatedGoodByOtherUsers");
    }
}
