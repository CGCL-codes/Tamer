package whf.shop.web;

import java.util.List;
import whf.framework.entity.AbstractEntity;
import whf.framework.web.struts.WebData;
import whf.framework.web.struts.WebDispatchAction;
import whf.framework.web.struts.WebForward;
import whf.shop.entity.Delivery;
import whf.shop.entity.Member;
import whf.shop.service.DeliveryServiceImp;
import whf.shop.utils.ShopUtils;

/**
 * @author wanghaifeng
 * @email king@126.com
 * @modify 2007-01-11
 */
public class DeliveryAction extends WebDispatchAction {

    public WebForward selectDeliveries(AbstractEntity baseForm, WebData data) throws Exception {
        Member member = ShopUtils.getCurrentMember(data.getSession());
        List<Delivery> deliveries = DeliveryServiceImp.getDeliveryService().findByMember(member);
        data.setAttribute("deliveries", deliveries);
        long deliveryId = data.getLongParameter("deliveryId", 0);
        if (deliveryId == 0) {
            deliveryId = data.getLongAttribute("deliveryId", 0);
        }
        Delivery currentDelivery = null;
        if (deliveryId > 0) {
            currentDelivery = DeliveryServiceImp.getDeliveryService().findByPrimaryKey(deliveryId);
        }
        data.setAttribute("currentDelivery", currentDelivery == null ? new Delivery() : currentDelivery);
        return data.forward("viewCart");
    }

    public WebForward createDelivery(AbstractEntity baseForm, WebData data) throws Exception {
        Delivery delivery = (Delivery) baseForm;
        delivery.setMember(ShopUtils.getCurrentMember(data.getSession()));
        DeliveryServiceImp.getDeliveryService().create(delivery);
        data.setAttribute("deliveryId", new Long(delivery.getId()));
        return selectDeliveries(baseForm, data);
    }
}
